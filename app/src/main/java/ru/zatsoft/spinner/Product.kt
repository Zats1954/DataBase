package ru.zatsoft.spinner

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product (val name: String, val weight: Int, val price: Int) : Parcelable