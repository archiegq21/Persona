package com.apps.usergen.repository

import app.cash.turbine.test
import com.apps.database.dao.UserCollectionDao
import com.apps.database.dao.UserDao
import com.apps.database.data.UserEntity
import com.apps.model.Gender
import com.apps.model.Nationality
import com.apps.usergen.data.toModel
import dev.mokkery.MockMode
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.mock
import dev.mokkery.verify.VerifyMode
import dev.mokkery.verifySuspend
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.datetime.Instant
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class UserRepositoryTest {

    private lateinit var repository: UserRepository

    private val mockUserCollectionDao: UserCollectionDao = mock(MockMode.autoUnit)
    private val mockRemoteSource: UserRemoteSource = mock(MockMode.autoUnit)
    private val mockUserDao: UserDao = mock(MockMode.autoUnit)

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeTest
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
        repository = RealUserRepository(
            userRemoteSource = mockRemoteSource,
            userCollectionDao = mockUserCollectionDao,
            userDao = mockUserDao,
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun given_GetUserById_Then_ReturnEntityUser() = runTest {
        every { mockUserDao.getUserBy(USER_ID) } returns flowOf(userEntity)

        repository.getUserBy(USER_ID).test {
            assertEquals(userEntity.toModel(), awaitItem())
            awaitComplete()
        }

        verifySuspend(VerifyMode.exhaustive) {
            mockUserDao.getUserBy(USER_ID)
        }
    }

    companion object {
        private const val USER_ID = "TestId"
        private const val COLLECTION_ID = "COLLECTION_ID"
        private val userEntity = UserEntity(
            userId = USER_ID,
            collectionId = COLLECTION_ID,
            id = UserEntity.Id(
                name = "Name",
                value = "Value",
            ),
            gender = Gender.Male,
            name = UserEntity.Name(
                title = "Title",
                first = "First",
                last = "Last",
            ),
            location = UserEntity.Location(
                street = UserEntity.Street(
                    number = 1,
                    name = "Street",
                ),
                city = "City",
                state = "State",
                country = "Country",
                postcode = "Postcode",
                coordinates = UserEntity.Coordinates(
                    latitude = "Latitude",
                    longitude = "Longitude",
                ),
                timezone = UserEntity.Timezone(
                    offset = "Offset",
                    description = "Description",
                ),
            ),
            email = "sample@example.com",
            login = UserEntity.Login(
                uuid = "Uuid",
                username = "Username",
                password = "Password",
                salt = "Salt",
                md5 = "Md5",
                sha1 = "Sha1",
                sha256 = "Sha256",
            ),
            dob = UserEntity.Dob(
                date = Instant.DISTANT_PAST,
                age = 1,
            ),
            registered = UserEntity.Registered(
                date = Instant.DISTANT_PAST,
                age = 1,
            ),
            phone = "09123123123",
            cell = "1231231",
            picture = UserEntity.Picture(
                large = "https://sample.com",
                medium = "https://sample.com",
                thumbnail = "https://sample.com",
            ),
            nat = Nationality.AU,
        )
    }
}