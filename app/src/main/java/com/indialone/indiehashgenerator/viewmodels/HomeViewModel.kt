package com.indialone.indiehashgenerator.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import java.security.MessageDigest

class HomeViewModel : ViewModel() {

    fun getHash(plainText: String, algorithm: String): String {
        val bytes = MessageDigest.getInstance(algorithm).digest(plainText.toByteArray())
        return toHex(byteArray = bytes)
    }

    private fun toHex(byteArray: ByteArray): String {
        Log.e("HomeViewModel", byteArray.joinToString("") { "%02x".format(it) })
        return byteArray.joinToString("") { "%02x".format(it) }
    }

}