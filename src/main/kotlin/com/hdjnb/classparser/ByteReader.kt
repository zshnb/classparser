package com.hdjnb.classparser

class ByteReader(private val filePath: String) {
    private var bytes: MutableList<Byte> = FileUtil.readBytes(filePath).toMutableList()

    private fun convertByteToHex(byte: Byte) = String.format("%02X", byte.toUByte().toInt())

    fun readU1(): String = convertByteToHex(bytes.removeAt(0))

    fun readU2(): String {
        val u2 = bytes.slice(IntRange(0, 1))
        bytes.removeAll(u2)
        return u2.map { convertByteToHex(it) }.reduce { sum, element ->
            "$sum$element"
        }
    }

    fun readU4(): String {
        val u4 = bytes.slice(IntRange(0, 3))
        bytes.removeAll(u4)
        return u4.map { convertByteToHex(it) }.reduce { sum, element ->
            "$sum$element"
        }
    }

    fun readU8(): String {
        val u8 = bytes.slice(IntRange(0, 7))
        bytes.removeAll(u8)
        return u8.map { convertByteToHex(it) }.reduce { sum, element ->
            "$sum$element"
        }
    }
}