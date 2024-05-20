package com.example.applicationletmecode.critics.detail_critics

import android.os.Parcel
import android.os.Parcelable

data class DetailCriticData(
    val title: String,
    val subtitle: String,
    val url: String,
    var critic: String) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(subtitle)
        parcel.writeString(url)
        parcel.writeString(critic)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DetailCriticData> {
        override fun createFromParcel(parcel: Parcel): DetailCriticData {
            return DetailCriticData(parcel)
        }

        override fun newArray(size: Int): Array<DetailCriticData?> {
            return arrayOfNulls(size)
        }
    }
}
