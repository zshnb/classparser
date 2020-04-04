package com.hdjnb.classparser.info

enum class Tag(val flag: Int,
               val tagName: String) {
    CONSTANT_UTF8_INFO(1, "Utf8"),
    CONSTANT_INTEGER_INFO(3, "Integer"),
    CONSTANT_FLOAT_INFO(4, "Float"),
    CONSTANT_LONG_INFO(5, "Long"),
    CONSTANT_DOUBLE_INFO(6, "Double"),
    CONSTANT_CLASS_INFO(7, "Class"),
    CONSTANT_STRING_INFO(8, "String"),
    CONSTANT_FIELD_REF_INFO(9, "FieldRef"),
    CONSTANT_METHOD_REF_INFO(10, "MethodRef"),
    CONSTANT_INTERFACE_METHOD_REF_INFO(11, "InterfaceMethodRef"),
    CONSTANT_NAME_AND_TYPE_INFO(12, "NameAndType"),
    CONSTANT_METHOD_HANDLE_INFO(15, "MethodHandle"),
    CONSTANT_METHOD_TYPE_INFO(16, "MethodType"),
    CONSTANT_DYNAMIC_INFO(17, "Dynamic"),
    CONSTANT_INVOKE_DYNAMIC_INFO(18, "InvokeDynamic"),
    CONSTANT_MODULE_INFO(19, "Module"),
    CONSTANT_PACKAGE_INFO(20, "Package");
}

data class ConstantPool(val constantsCount: Int,
                        val constants: List<ConstantInfo>) {
    operator fun get(index: Int): ConstantUtf8Info = constants[index] as ConstantUtf8Info

    fun getConstantClassInfo(index: Int): ConstantClassInfo = constants[index] as ConstantClassInfo

    fun getConstantNameAndTypeInfo(index: Int): ConstantNameAndTypeInfo = constants[index] as ConstantNameAndTypeInfo

    override fun toString(): String {
        return constants.filterNotNull().mapIndexed { index, constantInfo ->
            val i = if (index < 10) " #$index" else "#$index"
            when (constantInfo) {
                is ConstantUtf8Info -> "$i $constantInfo"
                is ConstantIntegerInfo -> "$i $constantInfo"
                is ConstantFloatInfo -> "$i $constantInfo"
                is ConstantDoubleInfo -> "$i $constantInfo"
                is ConstantLongInfo -> "$i $constantInfo"
                is ConstantClassInfo -> "$i ${constantInfo.toString(this)}"
                is ConstantNameAndTypeInfo -> "$i ${constantInfo.toString(this)}"
                is ConstantStringInfo -> "$i ${constantInfo.toString(this)}"
                is ConstantFieldRefInfo -> "$i ${constantInfo.toString(this)}"
                is ConstantMethodRefInfo -> "$i ${constantInfo.toString(this)}"
                is ConstantInterfaceMethodRefInfo -> "$i ${constantInfo.toString(this)}"
                is ConstantMethodTypeInfo -> "$i ${constantInfo.toString(this)}"
                else -> ""
            }
        }.joinToString("\n")
    }
}

fun ConstantClassInfo.toString(constantPool: ConstantPool) =
    "${this.tag.tagName}\t\t\t#${this.nameIndex} // ${constantPool[this.nameIndex].bytes}"

fun ConstantNameAndTypeInfo.toString(constantPool: ConstantPool) =
    "${this.tag.tagName}\t\t#${this.nameIndex}.#${this.typeIndex} // " +
            "${constantPool[this.nameIndex].bytes}:${constantPool[this.typeIndex].bytes}"

fun ConstantStringInfo.toString(constantPool: ConstantPool) =
    "${this.tag.tagName}\t\t\t${constantPool[this.index].bytes}"

fun ConstantFieldRefInfo.toString(constantPool: ConstantPool) =
    "${this.tag.tagName}\t\t#${this.classInfoIndex}.#${this.nameAndTypeInfoIndex} // " +
            constantPool[constantPool.getConstantClassInfo(this.classInfoIndex).nameIndex].bytes +
            "${constantPool[constantPool.getConstantNameAndTypeInfo(this.nameAndTypeInfoIndex).nameIndex].bytes}." +
            constantPool[constantPool.getConstantNameAndTypeInfo(this.nameAndTypeInfoIndex).typeIndex].bytes

fun ConstantMethodRefInfo.toString(constantPool: ConstantPool) =
    "${this.tag.tagName}\t\t#${this.classInfoIndex}.#${this.nameAndTypeInfoIndex} // " +
            constantPool[constantPool.getConstantClassInfo(this.classInfoIndex).nameIndex].bytes +
            "${constantPool[constantPool.getConstantNameAndTypeInfo(this.nameAndTypeInfoIndex).nameIndex].bytes}." +
            constantPool[constantPool.getConstantNameAndTypeInfo(this.nameAndTypeInfoIndex).typeIndex].bytes

fun ConstantInterfaceMethodRefInfo.toString(constantPool: ConstantPool) =
    "${this.tag.tagName}\t#${this.classInfoIndex}.#${this.nameAndTypeInfoIndex} // " +
            constantPool.getConstantClassInfo(this.classInfoIndex).toString(constantPool) +
            constantPool.getConstantNameAndTypeInfo(this.nameAndTypeInfoIndex).toString(constantPool)

fun ConstantMethodTypeInfo.toString(constantPool: ConstantPool) =
    "${this.tag.tagName}\t\t\t${constantPool[this.descriptorIndex]}"

abstract class ConstantInfo

data class ConstantUtf8Info(val tag: Tag,
                            val length: Int,
                            val bytes: String) : ConstantInfo() {
    override fun toString(): String {
        return "${tag.tagName}\t\t\t$bytes"
    }
}

data class ConstantClassInfo(val tag: Tag,
                             val nameIndex: Int) : ConstantInfo()

data class ConstantIntegerInfo(val tag: Tag,
                               val bytes: Int): ConstantInfo() {
    override fun toString(): String {
        return "${tag.tagName}\t\t\t$bytes"
    }
}

data class ConstantFloatInfo(val tag: Tag,
                             val bytes: Float): ConstantInfo() {

    override fun toString(): String {
        return "${tag.tagName}\t\t\t$bytes"
    }
}

data class ConstantLongInfo(val tag: Tag,
                            val bytes: Long): ConstantInfo() {

    override fun toString(): String {
        return "${tag.tagName}\t\t\t$bytes"
    }
}

data class ConstantDoubleInfo(val tag: Tag,
                              val bytes: Double): ConstantInfo() {

    override fun toString(): String {
        return "${tag.tagName}\t$bytes"
    }
}

data class ConstantStringInfo(val tag: Tag,
                              val index: Int): ConstantInfo()

data class ConstantFieldRefInfo(val tag: Tag,
                                val classInfoIndex: Int,
                                val nameAndTypeInfoIndex: Int): ConstantInfo()

data class ConstantMethodRefInfo(val tag: Tag,
                                 val classInfoIndex: Int,
                                 val nameAndTypeInfoIndex: Int): ConstantInfo()

data class ConstantInterfaceMethodRefInfo(val tag: Tag,
                                          val classInfoIndex: Int,
                                          val nameAndTypeInfoIndex: Int): ConstantInfo()

data class ConstantNameAndTypeInfo(val tag: Tag,
                                   val nameIndex: Int,
                                   val typeIndex: Int): ConstantInfo()

data class ConstantMethodHandleInfo(val tag: Tag,
                                    val referenceKind: Int,
                                    val referenceIndex: Int): ConstantInfo()

data class ConstantMethodTypeInfo(val tag: Tag,
                                  val descriptorIndex: Int): ConstantInfo()

data class ConstantDynamicInfo(val tag: Tag,
                               val bootstrapMethodAttrIndex: Int,
                               val nameAndTypeIndex: Int): ConstantInfo()

data class ConstantInvokeDynamicInfo(val tag: Tag,
                                 val bootstrapMethodAttrIndex: Int,
                                 val nameAndTypeIndex: Int): ConstantInfo()

data class ConstantModuleInfo(val tag: Tag,
                              val nameIndex: Int): ConstantInfo()

data class ConstantPackageInfo(val tag: Tag,
                               val nameIndex: Int): ConstantInfo()
