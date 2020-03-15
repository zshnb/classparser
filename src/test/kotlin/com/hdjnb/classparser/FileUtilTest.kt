package com.hdjnb.classparser

import org.hamcrest.Matchers.greaterThan
import org.hamcrest.core.AllOf.allOf
import org.junit.*
import org.junit.Assert.assertThat

class FileUtilTest {
    private lateinit var fileUtil: FileUtil

    @Before
    fun before() {
        fileUtil = FileUtil()
    }

    @Test
    fun testReadBytes() {
        val filePath = this.javaClass.getResource("/Test.class").path
        val bytes = fileUtil.readBytes(filePath)
        assertThat("byte array size is 0", bytes.size, greaterThan(0))
    }
}