package com.hdjnb.classparser

import org.hamcrest.Matchers
import org.hamcrest.core.IsEqual
import org.junit.*
import org.junit.Assert.assertThat

class ParserTest {
    private lateinit var parser: Parser

    private lateinit var byteReader: ByteReader

    @Before
    fun before() {
        byteReader = ByteReader(TestUtil.filePath)
        parser = Parser(byteReader)
    }

    @Test
    fun testParseMagicNumber() {
        val magicNumberInfo = parser.parseMagicNumber()
        assertThat(magicNumberInfo.value, IsEqual.equalTo("CAFEBABE"))
    }

    @Test
    fun testParseMinorAndMajorVersion() {
        testParseMagicNumber()
        val minorVersionInfo = parser.parseMinorVersion()
        val majorVersionInfo = parser.parseMajorVersion()
        assertThat(majorVersionInfo.value.toDouble(), Matchers.greaterThan(45.3))
    }
}