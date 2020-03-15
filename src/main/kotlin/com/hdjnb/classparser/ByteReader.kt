package com.hdjnb.classparser

class ByteReader(private val filePath: String) {
    private var bytes: MutableList<Byte> = FileUtil.readBytes(filePath).toMutableList()

    @ExperimentalStdlibApi
    fun readU1(): Byte = bytes.removeFirst()

    fun readU2(): List<Byte> {
        val u2 = bytes.slice(IntRange(0, 1))
        bytes.removeAll(u2)
        return u2
    }

    fun readU4(): List<Byte> {
        val u4 = bytes.slice(IntRange(0, 3))
        bytes.removeAll(u4)
        return u4
    }

    fun readU8(): List<Byte> {
        val u8 = bytes.slice(IntRange(0, 7))
        bytes.removeAll(u8)
        return u8
    }
}