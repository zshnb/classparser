package com.hdjnb.classparser.info

data class MajorVersionInfo(val value: Int) {
    private val majorVersionTable = mapOf(
        46 to "1.2",
        47 to "1.3",
        48 to "1.4",
        49 to "1.5",
        50 to "6",
        51 to "7",
        52 to "8",
        53 to "9",
        54 to "10",
        55 to "11",
        56 to "12",
        57 to "13")
    override fun toString(): String {
        return "版本号: $value\tJDK: ${majorVersionTable[value]}"
    }
}