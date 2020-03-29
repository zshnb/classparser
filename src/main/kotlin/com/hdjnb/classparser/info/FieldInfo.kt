package com.hdjnb.classparser.info

data class FieldInfo(val accessFlags: String,
                     val nameIndex: Int,
                     val descriptorIndex: Int,
                     val attributesCount: Int,
                     val attributeInfos: List<AttributeInfo>)

enum class AccessFlags(val value: String) {
    ACC_PUBLIC("0001"),
    ACC_PRIVATE("0002"),
    ACC_PROTECTED("0004"),
    ACC_STATIC("0008"),
    ACC_FINAL("0010"),
    ACC_VOLATILE("0040"),
    ACC_TRANSIENT("0080"),
    ACC_SYNTHETIC("1000"),
    ACC_ENUM("4000")
}