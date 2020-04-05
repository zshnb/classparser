package com.hdjnb.classparser.info

data class MethodInfo(val accessFlags: String,
                      val nameIndex: Int,
                      val descriptorIndex: Int,
                      val attributesCount: Int,
                      val attributeInfos: List<AttributeInfo>) {

    enum class AccessFlags(val value: String) {
        ACC_PUBLIC("0001"),
        ACC_PRIVATE("0002"),
        ACC_PROTECTED("0004"),
        ACC_STATIC("0008"),
        ACC_FINAL("0010"),
        ACC_SYNCHRONIZED("0020"),
        ACC_BRIDGE("0040"),
        ACC_VARARGS("0080"),
        ACC_NATIVE("0100"),
        ACC_ABSTRACT("0400"),
        ACC_STRICT("0800"),
        ACC_SYNTHETIC("1000")
    }
}

fun MethodInfo.toString(constantPool: ConstantPool): String =
    "${constantPool[nameIndex].bytes} ${constantPool[descriptorIndex].bytes}"

