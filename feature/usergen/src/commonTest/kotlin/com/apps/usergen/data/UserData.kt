package com.apps.usergen.data

import com.apps.database.data.UserCollectionEntity
import com.apps.model.Gender
import com.apps.model.Nationality
import kotlinx.datetime.Instant

val userCollectionEntity = UserCollectionEntity(
    id = "COLLECTION_ID",
    name = "Test",
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

fun createUser(
    id: String = "TestId",
    collectionId: String = "COLLECTION_ID",
) = createNetworkUser(id)
    .toEntity(collectionId)
    .toModel()