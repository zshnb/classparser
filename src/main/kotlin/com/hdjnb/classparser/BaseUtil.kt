package com.hdjnb.classparser

class BaseUtil {
    companion object {
        fun convertByteToHex(byte: Byte) = String.format("%02X", byte.toUByte().toInt())

        fun convertHexToInt(hex: String) = Integer.parseInt(hex, 16)
    }
}