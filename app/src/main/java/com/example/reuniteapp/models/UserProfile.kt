package com.example.reuniteapp.models

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "user_profiles",
    indices = [Index(value = ["username"], unique = true)]
)
data class UserProfile(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var name: String = "",
    var contactNumber: String = "",
    val username: String = "", // Added default value to ensure no-arg constructor
    var password: String = "",
    var profileImageUri: String = "",
    var email: String = "",
    val profilePic: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeString(contactNumber)
        parcel.writeString(username)
        parcel.writeString(password)
        parcel.writeString(profileImageUri)
        parcel.writeString(email)
        parcel.writeString(profilePic)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserProfile> {
        override fun createFromParcel(parcel: Parcel): UserProfile {
            return UserProfile(parcel)
        }

        override fun newArray(size: Int): Array<UserProfile?> {
            return arrayOfNulls(size)
        }
    }
}