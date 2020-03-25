package com.hdjnb.classparser.info

enum class Tag(val flag: Int) {
    CONSTANT_UTF8_INFO(1),
    CONSTANT_INTEGER_INFO(3),
    CONSTANT_FLOAT_INFO(4),
    CONSTANT_LONG_INFO(5),
    CONSTANT_DOUBLE_INFO(6),
    CONSTANT_CLASS_INFO(7),
    CONSTANT_STRING_INFO(8),
    CONSTANT_FIELD_REF_INFO(9),
    CONSTANT_METHOD_REF_INFO(10),
    CONSTANT_INTERFACE_METHOD_REF_INFO(11),
    CONSTANT_NAME_AND_TYPE_INFO(12),
    CONSTANT_METHOD_HANDLE_INFO(15),
    CONSTANT_METHOD_TYPE_INFO(16),
    CONSTANT_DYNAMIC_INFO(17),
    CONSTANT_INVOKE_DYNAMIC_INFO(18),
    CONSTANT_MODULE_INFO(19),
    CONSTANT_PACKAGE_INFO(20);

    companion object {
        fun valueOf(flag: Int): Tag =
            when (flag) {
                1 -> CONSTANT_CLASS_INFO
                else -> CONSTANT_CLASS_INFO
            }
    }
}

abstract class ConstantInfo

data class ConstantUtf8Info(val tag: Tag,
                            val length: Int,
                            val bytes: String) : ConstantInfo()

data class ConstantClassInfo(val tag: Tag,
                             val nameIndex: Int) : ConstantInfo()

data class ConstantIntegerInfo(val tag: Tag,
                               val bytes: Int): ConstantInfo()

data class ConstantFloatInfo(val tag: Tag,
                             val bytes: Float): ConstantInfo()

data class ConstantLongInfo(val tag: Tag,
                            val bytes: Long): ConstantInfo()

data class ConstantDoubleInfo(val tag: Tag,
                              val bytes: Double): ConstantInfo()

data class ConstantStringInfo(val tag: Tag,
                              val index: Int): ConstantInfo()

data class ConstantFieldRefInfo(val tag: Tag,
                                val classInfoIndex: Int,
                                val nameAndTypeInfoIndex: Int)

data class ConstantMethodRefInfo(val tag: Tag,
                                 val classInfoIndex: Int,
                                 val nameAndTypeInfoIndex: Int)

data class ConstantInterfaceRefInfo(val tag: Tag,
                                    val classInfoIndex: Int,
                                    val nameAndTypeInfoIndex: Int)

data class ConstantNameAndTypeInfo(val tag: Tag,
                                   val nameIndex: Int,
                                   val typeIndex: Int)

data class ConstantMethodHandleInfo(val tag: Tag,
                                    val referenceKind: Int,
                                    val referenceIndex: Int)

data class ConstantMethodTypeInfo(val tag: Tag,
                                  val descriptorIndex: Int)

data class ConstantDynamicInfo(val tag: Tag,
                               val bootstrapMethodAttrIndex: Int,
                               val nameAndTypeIndex: Int)

data class ConstantInvokeDynamic(val tag: Tag,
                                 val bootstrapMethodAttrIndex: Int,
                                 val nameAndTypeIndex: Int)

data class ConstantModuleInfo(val tag: Tag,
                              val nameIndex: Int)

data class ConstantPackageInfo(val tag: Tag,
                               val nameIndex: Int)
