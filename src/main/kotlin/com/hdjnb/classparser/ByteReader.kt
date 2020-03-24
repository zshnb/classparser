package com.hdjnb.classparser

import com.hdjnb.classparser.BaseUtil.Companion.convertByteToHex

class ByteReader(private val filePath: String) {
    private var bytes: MutableList<Byte> = FileUtil.readBytes(filePath).toMutableList()

    fun readU1(): String = convertByteToHex(bytes.removeAt(0))

    fun readU2(): String {
        val u2 = bytes.slice(IntRange(0, 1))
        bytes = bytes.drop(2).toMutableList()
        return u2.map { convertByteToHex(it) }.reduce { sum, element ->
            "$sum$element"
        }
    }

    fun readU4(): String {
        val u4 = bytes.slice(IntRange(0, 3))
        bytes = bytes.drop(4).toMutableList()
        return u4.map { convertByteToHex(it) }.reduce { sum, element ->
            "$sum$element"
        }
    }

    fun readU8(): String {
        val u8 = bytes.slice(IntRange(0, 7))
        bytes = bytes.drop(8).toMutableList()
        return u8.map { convertByteToHex(it) }.reduce { sum, element ->
            "$sum$element"
        }
    }
}