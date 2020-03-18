package com.hdjnb.classparser

import com.hdjnb.classparser.info.MagicNumberInfo

class Parser(private val byteReader: ByteReader) {
    fun parseMagicNumber(): MagicNumberInfo = MagicNumberInfo(byteReader.readU4())
}