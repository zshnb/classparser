package com.hdjnb.classparser

class TestUtil {
    companion object {
        val filePath = TestUtil::class.java.getResource("/Test.class").path
    }
}