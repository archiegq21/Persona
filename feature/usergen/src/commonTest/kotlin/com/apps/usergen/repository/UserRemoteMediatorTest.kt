package com.apps.usergen.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingState
import androidx.paging.RemoteMediator.MediatorResult
import com.apps.database.dao.UserDao
import com.apps.database.data.UserEntity
import com.apps.usergen.data.UserResponse
import com.apps.usergen.data.createNetworkUser
import com.apps.usergen.data.userCollectionEntity
import dev.mokkery.MockMode
import dev.mokkery.answering.returns
import dev.mokkery.answering.throws
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verify.VerifyMode
import dev.mokkery.verifySuspend
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class UserRemoteMediatorTest {

    private val mockUserDao = mock<UserDao>(MockMode.autoUnit)
    private val mockRemoteSource = mock<UserRemoteSource>(MockMode.autoUnit)

    private lateinit var remoteMediator: UserRemoteMediator

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeTest
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
        remoteMediator = UserRemoteMediator(
            userCollection = userCollectionEntity,
            userDao = mockUserDao,
            remoteSource = mockRemoteSource,
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @OptIn(ExperimentalPagingApi::class)
    @Test
    fun given_LoadTypeRefreshSuccess_Then_DataIsPresent() = runTest {
        everySuspend {
            mockRemoteSource.fetchUsers(
                seed = userCollectionEntity.id,
                page = 1,
                countPerPage = 10,
            )
        } returns UserResponse(
            results = buildList {
                repeat(10) {
                    add(createNetworkUser("${it}"))
                }
            },
            info = UserResponse.Info(
                seed = userCollectionEntity.id,
                results = 10,
                page = 1,
                version = "1.4",
            )
        )

        val pagingState = PagingState<Int, UserEntity>(
            listOf(),
            null,
            PagingConfig(10),
            10
        )

        val result = remoteMediator.load(LoadType.REFRESH, pagingState)
        assertTrue { result is MediatorResult.Success }
        assertFalse { (result as MediatorResult.Success).endOfPaginationReached }

        verifySuspend(VerifyMode.exactly(1)) {
            mockRemoteSource.fetchUsers(any(), any(), any())
            mockUserDao.deleteAllAndInsertAll(userCollectionEntity.id, any())
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    @Test
    fun given_LoadTypePrependSuccess_Then_NoMoreData() = runTest {
        val users = buildList {
            repeat(10) {
                add(createNetworkUser("$it"))
            }
        }
        everySuspend {
            mockRemoteSource.fetchUsers(
                seed = any(),
                page = any(),
                countPerPage = any(),
            )
        } returns UserResponse(
            results = users,
            info = UserResponse.Info(
                seed = userCollectionEntity.id,
                results = 10,
                page = 10,
                version = "1.4",
            )
        )

        val pagingState = PagingState<Int, UserEntity>(
            listOf(),
            null,
            PagingConfig(10),
            10
        )

        val repeatsTillEnd = userCollectionEntity.count / 10
        var result: MediatorResult? = null
        repeat(repeatsTillEnd) {
            result = remoteMediator.load(LoadType.APPEND, pagingState)
        }

        assertTrue { result is MediatorResult.Success }
        assertTrue { (result as MediatorResult.Success).endOfPaginationReached }
        verifySuspend(VerifyMode.exactly(repeatsTillEnd)) {
            mockRemoteSource.fetchUsers(any(), any(), any())
            mockUserDao.insertUsers(any())
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    @Test
    fun given_LoadTypeRefreshFailed_Then_Error() = runTest {
        everySuspend {
            mockRemoteSource.fetchUsers(
                seed = any(),
                page = any(),
                countPerPage = any(),
            )
        } throws Exception()

        val pagingState = PagingState<Int, UserEntity>(
            listOf(),
            null,
            PagingConfig(10),
            10
        )

        val result = remoteMediator.load(LoadType.REFRESH, pagingState)

        assertTrue { result is MediatorResult.Error }

        verifySuspend(VerifyMode.exactly(1)) {
            mockRemoteSource.fetchUsers(any(), any(), any())
        }

        verifySuspend(VerifyMode.not) {
            mockUserDao.insertUsers(any())
        }
    }
}