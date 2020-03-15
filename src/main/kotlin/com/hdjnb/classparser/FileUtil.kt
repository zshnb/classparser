package com.hdjnb.classparser

import org.apache.commons.io.FileUtils

class FileUtil {
    fun readBytes(filePath: String): ByteArray {
        val classFile = FileUtils.getFile(filePath)
        return FileUtils.readFileToByteArray(classFile)
    }
}