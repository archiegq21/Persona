package com.apps.database.data

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.apps.model.Gender
import com.apps.model.Nationality
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Entity(
    tableName = "User",
    foreignKeys = [
        ForeignKey(
            entity = UserCollectionEntity::class,
            parentColumns = ["id"],
            childColumns = ["collectionId"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class UserEntity(
    @PrimaryKey val userId: String,
    @ColumnInfo(index = true) val collectionId: String,
    @Embedded("id") val id: Id,
    val gender: Gender,
    @Embedded("name") val name: Name,
    @Embedded("location") val location: Location,
    val email: String,
    @Embedded("login") val login: Login,
    @Embedded("dob") val dob: Dob,
    @Embedded("registered") val registered: Registered,
    val phone: String,
    val cell: String,
    @Embedded("picture") val picture: Picture,
    val nat: Nationality
) {

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
