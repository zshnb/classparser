package com.hdjnb.classparser

import com.hdjnb.classparser.info.*

class Parser(private val byteReader: ByteReader) {
    fun parseMagicNumber(): MagicNumberInfo = MagicNumberInfo(byteReader.readU4())

    fun parseMinorVersion(): MinorVersionInfo = MinorVersionInfo(byteReader.readU2())

    fun parseMajorVersion(): MajorVersionInfo = MajorVersionInfo(byteReader.readU2())
}