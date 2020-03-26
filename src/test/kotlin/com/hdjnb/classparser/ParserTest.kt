package com.hdjnb.classparser

import com.hdjnb.classparser.info.*
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

    @Test
    fun testParseConstantPool() {
        testParseMinorAndMajorVersion()
        val constants = parser.parseConstPool()
        assertThat(constants.size, Matchers.equalTo(39))
        constants.forEach {
            when {
                it is ConstantClassInfo -> {
                    assertThat(it.tag, Matchers.equalTo(Tag.CONSTANT_CLASS_INFO))
                    assertThat(it.nameIndex, Matchers.lessThan(65535))
                }
                it is ConstantUtf8Info -> {
                    assertThat(it.tag, Matchers.equalTo(Tag.CONSTANT_UTF8_INFO))
                    assertThat(it.length, Matchers.greaterThan(0))
                    assertThat(it.bytes, Matchers.isA(String::class.java))
                }
            }
        }
    }
}