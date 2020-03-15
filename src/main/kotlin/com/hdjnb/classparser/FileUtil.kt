package com.hdjnb.classparser

import org.apache.commons.io.FileUtils

class FileUtil {
    companion object {
        fun readBytes(filePath: String): ByteArray {
            val classFile = FileUtils.getFile(filePath)
            return FileUtils.readFileToByteArray(classFile)
        }
    }
}