package com.hdjnb.classparser.info

enum class AccessFlagsInfo(val value: String) {
    ACC_PUBLIC("0001"),
    ACC_FINAL("0010"),
    ACC_SUPER("0020"),
    ACC_INTERFACE("0200"),
    ACC_ABSTRACT("0400"),
    ACC_SYNTHETIC("1000"),
    ACC_ANNOTATION("2000"),
    ACC_ENUM("4000"),
    ACC_MODULE("8000")
}
