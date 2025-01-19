package com.apps.usergen.data

import com.apps.database.data.UserEntity
import com.apps.model.Gender
import com.apps.model.Nationality
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import kotlin.math.log

@Serializable
data class UserResponse(
    val results: List<User>,
    val info: Info
) {
    @Serializable
    data class User(
        val gender: Gender,
        val name: Name,
        val location: Location,
        val email: String,
        val login: Login,
        val dob: Dob,
        val registered: Registered,
        val phone: String,
        val cell: String,
        val id: Id,
        val picture: Picture,
        val nat: Nationality,
    )

    @Serializable
    data class Name(
        val title: String,
        val first: String,
        val last: String
    )

    @Serializable
    data class Location(
        val street: Street,
        val city: String,
        val state: String,
        val country: String,
        val postcode: String,
        val coordinates: Coordinates,
        val timezone: Timezone
    )

    @Serializable
    data class Street(
        val number: Int,
        val name: String
    )

    @Serializable
    data class Coordinates(
        val latitude: String,
        val longitude: String
    )

    @Serializable
    data class Timezone(
        val offset: String,
        val description: String
    )

    @Serializable
    data class Login(
        val uuid: String,
        val username: String,
        val password: String,
        val salt: String,
        val md5: String,
        val sha1: String,
        val sha256: String
    )

    @Serializable
    data class Dob(
        val date: Instant,
        val age: Int
    )

    @Serializable
    data class Registered(
        val date: Instant,
        val age: Int
    )

    @Serializable
    data class Id(
        val name: String,
        val value: String?
    )

    @Serializable
    data class Picture(
        val large: String,
        val medium: String,
        val thumbnail: String
    )

    @Serializable
    data class Info(
        val seed: String,
        val results: Int,
        val page: Int,
        val version: String
    )
}

fun UserResponse.User.toEntity(
    collectionId: String,
): UserEntity = UserEntity(
    userId = login.uuid,
    collectionId = collectionId,
    id = UserEntity.Id(
        name = id.name,
        value = id.value
    ),
    gender = gender,
    name = UserEntity.Name(
        title = name.title,
        first = name.first,
        last = name.last
    ),
    location = UserEntity.Location(
        street = UserEntity.Street(
            number = location.street.number,
            name = location.street.name
        ),
        city = location.city,
        state = location.state,
        country = location.country,
        postcode = location.postcode,
        coordinates = UserEntity.Coordinates(
            latitude = location.coordinates.latitude,
            longitude = location.coordinates.longitude
        ),
        timezone = UserEntity.Timezone(
            offset = location.timezone.offset,
            description = location.timezone.description
        ),
    ),
    email = email,
    login = UserEntity.Login(
        uuid = login.uuid,
        username = login.username,
        password = login.password,
        salt = login.salt,
        md5 = login.md5,
        sha1 = login.sha1,
        sha256 = login.sha256,
    ),
    dob = UserEntity.Dob(
        date = dob.date,
        age = dob.age
    ),
    registered = UserEntity.Registered(
        date = registered.date,
        age = registered.age
    ),
    phone = phone,
    cell = cell,
    picture = UserEntity.Picture(
        large = picture.large,
        medium = picture.medium,
        thumbnail = picture.thumbnail
    ),
    nat = nat,
)