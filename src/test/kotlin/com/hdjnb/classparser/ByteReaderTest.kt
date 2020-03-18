package com.hdjnb.classparser

import com.hdjnb.classparser.TestUtil.Companion.filePath
import org.hamcrest.CoreMatchers.*
import org.junit.*
import org.junit.Assert.assertThat

class ByteReaderTest {
    private lateinit var byteReader: ByteReader

    @Before
    fun before() {
        byteReader = ByteReader(filePath)
    }

    @Test
    fun testReadU1() {
        val u1 = byteReader.readU1()
        assertThat("u1 is not a hex string", u1, `is`(String::class.java))
        assertThat("u1 is not a single byte", u1.length, equalTo(2))
    }

    @Test
    fun testReadU2() {
        val u2 = byteReader.readU2()
        assertThat("u2 is not a hex string", u2, `is`(String::class.java))
        assertThat("u2 is not double byte", u2.length, equalTo(4))
    }

    @Test
    fun testReadU4() {
        val u4 = byteReader.readU4()
        assertThat("u4 is not a hex string", u4, `is`(String::class.java))
        assertThat("u4 is not four byte", u4.length, equalTo(8))
    }

    @Test
    fun testReadU8() {
        val u8 = byteReader.readU8()
        assertThat("u8 is not a hex string", u8, `is`(String::class.java))
        assertThat("u8 is not eight byte", u8.length, equalTo(16))
    }
}