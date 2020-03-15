package com.hdjnb.classparser

import com.hdjnb.classparser.TestUtil.Companion.filePath
import org.hamcrest.Matchers.greaterThan
import org.junit.Assert.assertThat
import org.junit.Test

class FileUtilTest {
    @Test
    fun testReadBytes() {
        val bytes = FileUtil.readBytes(filePath)
        assertThat("byte array size is 0", bytes.size, greaterThan(0))
    }
}