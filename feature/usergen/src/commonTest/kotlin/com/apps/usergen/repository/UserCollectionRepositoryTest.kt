package com.apps.usergen.repository

import com.apps.database.dao.UserCollectionDao
import com.apps.database.data.UserCollectionEntity
import com.apps.usergen.data.UserCollection
import dev.mokkery.MockMode
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

class UserCollectionRepositoryTest {

    private val mockUserCollectionDao = mock<UserCollectionDao>(MockMode.autoUnit)

    private lateinit var repository: UserCollectionRepository

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeTest
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
        repository = RealUserCollectionRepository(
            userCollectionDao = mockUserCollectionDao,
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun given_addUserCollection_Then_SaveToDB() = runTest {
        val id = "id"
        val name = "test"
        val count = 10
        val userCollection = UserCollection(id, name, count)

        repository.addUserCollection(userCollection)

        verifySuspend(VerifyMode.exactly(1)) {
            mockUserCollectionDao.insertUserCollection(UserCollectionEntity(
                id = id,
                name = name,
                count = count,
            ))
        }
    }
}