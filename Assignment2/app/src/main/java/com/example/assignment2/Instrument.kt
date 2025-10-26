package com.example.assignment2

import android.os.Parcel
import android.os.Parcelable
import java.util.*

data class Instrument(
    val name: String,
    var rating: Float,
    var attribute: String,
    val price: Int,
    val imageRes: Int,
    var borrowedMonths: Int = 0,
    val instrumentType: String? = null,
    val instrumentModel: String? = null,
    val instrumentBrand: String? = null,
    val instrumentColor: String? = null,
    val instrumentCondition: String? = null,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readFloat(),
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),


    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeFloat(rating)
        parcel.writeString(attribute)
        parcel.writeInt(price)
        parcel.writeInt(imageRes)
        parcel.writeInt(borrowedMonths)
        parcel.writeString(instrumentType)
        parcel.writeString(instrumentModel)
        parcel.writeString(instrumentBrand)
        parcel.writeString(instrumentColor)
        parcel.writeString(instrumentCondition)
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<Instrument> {
        override fun createFromParcel(parcel: Parcel) = Instrument(parcel)
        override fun newArray(size: Int): Array<Instrument?> = arrayOfNulls(size)
    }
}
