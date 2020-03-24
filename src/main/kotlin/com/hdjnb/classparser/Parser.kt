package com.hdjnb.classparser

import com.hdjnb.classparser.BaseUtil.Companion.convertHexToInt
import com.hdjnb.classparser.info.*

class Parser(private val byteReader: ByteReader) {
    fun parseMagicNumber(): MagicNumberInfo = MagicNumberInfo(byteReader.readU4())

    fun parseMinorVersion(): MinorVersionInfo =
        MinorVersionInfo(convertHexToInt(byteReader.readU2()))

    fun parseMajorVersion(): MajorVersionInfo =
        MajorVersionInfo(convertHexToInt(byteReader.readU2()))

    fun parseConstPool(): List<ConstantInfo> {
        val constantPoolCount = convertHexToInt(byteReader.readU2())
        val constants = mutableListOf<ConstantInfo>()
        for (i in 1 until constantPoolCount) {
            val flag = convertHexToInt(byteReader.readU1())
            when (flag) {
                Tag.CONSTANT_CLASS_INFO.flag -> constants.add(parseConstantClassInfo(flag))
            }
        }

        return constants
    }

    private fun parseConstantClassInfo(flag: Int): ConstantClassInfo {
        val nameIndex = byteReader.readU2().toInt(10)
        return ConstantClassInfo(Tag.valueOf(flag), nameIndex)
    }
}