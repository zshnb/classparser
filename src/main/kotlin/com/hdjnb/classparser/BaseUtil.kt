package com.hdjnb.classparser

import javax.xml.bind.DatatypeConverter

class BaseUtil {
    companion object {
        fun convertByteToHex(byte: Byte) = String.format("%02X", byte.toUByte().toInt())

        fun convertHexToInt(hex: String) = Integer.parseInt(hex, 16)

        fun convertHexToString(hex: String): String = String(DatatypeConverter.parseHexBinary(hex))

    }
}