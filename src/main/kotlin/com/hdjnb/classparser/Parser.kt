package com.hdjnb.classparser

import com.hdjnb.classparser.BaseUtil.Companion.convertHexToInt
import com.hdjnb.classparser.BaseUtil.Companion.convertHexToString
import com.hdjnb.classparser.info.*
import com.sun.org.apache.bcel.internal.classfile.ConstantInteger

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
                Tag.CONSTANT_CLASS_INFO.flag -> constants.add(parseConstantClassInfo())
                Tag.CONSTANT_UTF8_INFO.flag -> constants.add(parseConstantUtf8Info())
                Tag.CONSTANT_INTEGER_INFO.flag -> constants.add()
            }
        }

        return constants
    }

    private fun parseConstantClassInfo(): ConstantClassInfo {
        val nameIndex = byteReader.readU2().toInt(10)
        return ConstantClassInfo(Tag.CONSTANT_CLASS_INFO, nameIndex)
    }

    private fun parseConstantUtf8Info(): ConstantUtf8Info {
        val length = convertHexToInt(byteReader.readU2())
        val stringBuilder = StringBuilder()
        for (i in 0 until length) {
            stringBuilder.append(byteReader.readU1())
        }
        return ConstantUtf8Info(Tag.CONSTANT_UTF8_INFO, length, convertHexToString(stringBuilder.toString()))
    }

    private fun parseConstantIntegerInfo(): ConstantInteger
}