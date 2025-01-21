package com.apps.shared

import com.apps.database.data.UserCollectionEntity
import com.apps.model.Gender
import com.apps.model.Nationality
import com.apps.usergen.data.UserResponse
import kotlinx.datetime.Instant

val testCollection = UserCollectionEntity(
    id = "TestId",
    name = "Test Collection",
    count = 100,
)

fun createNetworkUser(
    id: String = "TestId",
) = UserResponse.User(
    id = UserResponse.Id(
        name = "Name",
        value = "Value",
    ),
    gender = Gender.Male,
    name = UserResponse.Name(
        title = "Title",
        first = "First",
        last = "Last",
    ),
    location = UserResponse.Location(
        street = UserResponse.Street(
            number = 1,
            name = "Street",
        ),
        city = "City",
        state = "State",
        country = "Country",
        postcode = "Postcode",
        coordinates = UserResponse.Coordinates(
            latitude = "Latitude",
            longitude = "Longitude",
        ),
        timezone = UserResponse.Timezone(
            offset = "Offset",
            description = "Description",
        ),
    ),
    email = "sample@example.com",
    login = UserResponse.Login(
        uuid = id,
        username = "Username",
        password = "Password",
        salt = "Salt",
        md5 = "Md5",
        sha1 = "Sha1",
        sha256 = "Sha256",
    ),
    dob = UserResponse.Dob(
        date = Instant.DISTANT_PAST,
        age = 1,
    ),
    registered = UserResponse.Registered(
        date = Instant.DISTANT_PAST,
        age = 1,
    ),
    phone = "09123123123",
    cell = "1231231",
    picture = UserResponse.Picture(
        large = "https://sample.com",
        medium = "https://sample.com",
        thumbnail = "https://sample.com",
    ),
    nat = Nationality.AU,
)