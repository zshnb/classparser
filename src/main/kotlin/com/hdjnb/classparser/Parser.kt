package com.hdjnb.classparser

import com.hdjnb.classparser.BaseUtil.Companion.convertHexToInt
import com.hdjnb.classparser.BaseUtil.Companion.convertHexToLong
import com.hdjnb.classparser.BaseUtil.Companion.convertHexToString
import com.hdjnb.classparser.info.*

class Parser(private val byteReader: ByteReader) {
    fun parseMagicNumber(): MagicNumberInfo = MagicNumberInfo(byteReader.readU4())

    fun parseMinorVersion(): MinorVersionInfo =
        MinorVersionInfo(convertHexToInt(byteReader.readU2()))

    fun parseMajorVersion(): MajorVersionInfo =
        MajorVersionInfo(convertHexToInt(byteReader.readU2()))

    fun parseConstPool(): List<ConstantInfo> {
        var constantPoolCount = convertHexToInt(byteReader.readU2())
        val constants = mutableListOf<ConstantInfo>()
        while (constantPoolCount >= 0) {
            val flag = convertHexToInt(byteReader.readU1())
            when (flag) {
                Tag.CONSTANT_CLASS_INFO.flag -> {
                    constants.add(parseConstantClassInfo())
                    constantPoolCount -= 1
                }
                Tag.CONSTANT_UTF8_INFO.flag -> {
                    constants.add(parseConstantUtf8Info())
                    constantPoolCount -= 1
                }
                Tag.CONSTANT_INTEGER_INFO.flag -> {
                    constants.add(parseConstantIntegerInfo())
                    constantPoolCount -= 1
                }
                Tag.CONSTANT_DOUBLE_INFO.flag -> {
                    constants.add(parseConstantDoubleInfo())
                    constantPoolCount -= 2
                }
                Tag.CONSTANT_FLOAT_INFO.flag -> {
                    constants.add(parseConstantFloatInfo())
                    constantPoolCount -= 1
                }
                Tag.CONSTANT_LONG_INFO.flag -> {
                    constants.add(parseConstantLongInfo())
                    constantPoolCount -= 2
                }
                Tag.CONSTANT_STRING_INFO.flag -> {
                    constants.add(parseConstantStringInfo())
                    constantPoolCount -= 1
                }
                Tag.CONSTANT_FIELD_REF_INFO.flag -> {
                    constants.add(parseConstantFieldRefInfo())
                    constantPoolCount -= 1
                }
                Tag.CONSTANT_METHOD_REF_INFO.flag -> {
                    constants.add(parseConstantMethodRefInfo())
                    constantPoolCount -= 1
                }
                Tag.CONSTANT_INTERFACE_METHOD_REF_INFO.flag -> {
                    constants.add(parseConstantInterfaceMethodRefInfo())
                    constantPoolCount -= 1
                }
                Tag.CONSTANT_NAME_AND_TYPE_INFO.flag -> {
                    constants.add(parseConstantNameAndTypeInfo())
                    constantPoolCount -= 1
                }
                Tag.CONSTANT_METHOD_HANDLE_INFO.flag -> {
                    constants.add(parseConstantMethodHandleInfo())
                    constantPoolCount -= 1
                }
                Tag.CONSTANT_METHOD_TYPE_INFO.flag -> {
                    constants.add(parseConstantMethodTypeInfo())
                    constantPoolCount -= 1
                }
                Tag.CONSTANT_DYNAMIC_INFO.flag -> {
                    constants.add(parseConstantDynamicInfo())
                    constantPoolCount -= 1
                }
                Tag.CONSTANT_INVOKE_DYNAMIC_INFO.flag -> {
                    constants.add(parseConstantInvokeDynamicInfo())
                    constantPoolCount -= 1
                }
                Tag.CONSTANT_MODULE_INFO.flag -> {
                    constants.add(parseConstantModuleInfo())
                    constantPoolCount -= 1
                }
                Tag.CONSTANT_PACKAGE_INFO.flag -> {
                    constants.add(parseConstantPackageInfo())
                    constantPoolCount -= 1
                }
            }
        }

        return constants
    }

    fun parseAccessFlagsInfo(): String = byteReader.readU2()

    fun parseClassExtensionInfo(): ClassExtensionInfo {
        val thisClassIndex = convertHexToInt(byteReader.readU2())
        val superClassIndex = convertHexToInt(byteReader.readU2())
        val classExtensionInfo = ClassExtensionInfo(thisClassIndex, superClassIndex)
        val interfaceCount = convertHexToInt(byteReader.readU2())
        if (interfaceCount > 0) {
            val interfaceIndexes = mutableListOf<Int>()
            for (i in 0 until interfaceCount) {
                interfaceIndexes.add(convertHexToInt(byteReader.readU2()))
            }
            classExtensionInfo.interfaceIndexes = interfaceIndexes
        }
        return classExtensionInfo
    }

    fun parseFieldInfos(): List<FieldInfo> {
        val fieldCount = convertHexToInt(byteReader.readU2())
        val fieldInfos = mutableListOf<FieldInfo>()
        for (i in 0 until fieldCount) {
            val accessFlags = byteReader.readU2()
            val nameIndex = convertHexToInt(byteReader.readU2())
            val descriptorIndex = convertHexToInt(byteReader.readU2())
            // TODO waiting to parse attributeInfo
            fieldInfos.add(FieldInfo(accessFlags, nameIndex, descriptorIndex, 0, emptyList()))
        }
        return fieldInfos
    }

    fun parseMethodInfos(): List<MethodInfo> {
        val methodCount = convertHexToInt(byteReader.readU2())
        val methodInfos = mutableListOf<MethodInfo>()
        for (i in 0 until methodCount) {
            val accessFlags = byteReader.readU2()
            val nameIndex = convertHexToInt(byteReader.readU2())
            val descriptorIndex = convertHexToInt(byteReader.readU2())
            // TODO waiting to parse attributeInfo
            methodInfos.add(MethodInfo(accessFlags, nameIndex, descriptorIndex, 0, emptyList()))
        }
        return methodInfos
    }

    private fun parseConstantClassInfo(): ConstantClassInfo {
        val nameIndex = convertHexToInt(byteReader.readU2())
        return ConstantClassInfo(Tag.CONSTANT_CLASS_INFO, nameIndex)
    }

    private fun parseConstantUtf8Info(): ConstantUtf8Info {
        val length = convertHexToInt(byteReader.readU2())
        val stringBuilder = StringBuilder()
        for (i in 0 until length) {
            stringBuilder.append(byteReader.readU1())
        }
        return ConstantUtf8Info(Tag.CONSTANT_UTF8_INFO, length, convertHexToString(stringBuilder.toString()))
    }

    private fun parseConstantIntegerInfo(): ConstantIntegerInfo {
        val bytes = convertHexToInt(byteReader.readU4())
        return ConstantIntegerInfo(Tag.CONSTANT_INTEGER_INFO, bytes)
    }

    private fun parseConstantFloatInfo(): ConstantFloatInfo {
        val bytes = convertHexToInt(byteReader.readU4()).toFloat()
        return ConstantFloatInfo(Tag.CONSTANT_FLOAT_INFO, bytes)
    }

    private fun parseConstantDoubleInfo(): ConstantDoubleInfo {
        val bytes = convertHexToLong(byteReader.readU8()).toDouble()
        return ConstantDoubleInfo(Tag.CONSTANT_DOUBLE_INFO, bytes)
    }

    private fun parseConstantLongInfo(): ConstantLongInfo {
        val bytes = convertHexToLong(byteReader.readU8())
        return ConstantLongInfo(Tag.CONSTANT_LONG_INFO, bytes)
    }

    private fun parseConstantStringInfo(): ConstantStringInfo {
        val index = convertHexToInt(byteReader.readU2())
        return ConstantStringInfo(Tag.CONSTANT_STRING_INFO, index)
    }

    private fun parseConstantFieldRefInfo(): ConstantFieldRefInfo {
        val classInfoIndex = convertHexToInt(byteReader.readU2())
        val nameAndTypeInfoIndex = convertHexToInt(byteReader.readU2())
        return ConstantFieldRefInfo(Tag.CONSTANT_FIELD_REF_INFO, classInfoIndex, nameAndTypeInfoIndex)
    }

    private fun parseConstantMethodRefInfo(): ConstantMethodRefInfo {
        val classInfoIndex = convertHexToInt(byteReader.readU2())
        val nameAndTypeInfoIndex = convertHexToInt(byteReader.readU2())
        return ConstantMethodRefInfo(Tag.CONSTANT_METHOD_REF_INFO, classInfoIndex, nameAndTypeInfoIndex)
    }

    private fun parseConstantInterfaceMethodRefInfo(): ConstantInterfaceMethodRefInfo {
        val classInfoIndex = convertHexToInt(byteReader.readU2())
        val nameAndTypeInfoIndex = convertHexToInt(byteReader.readU2())
        return ConstantInterfaceMethodRefInfo(Tag.CONSTANT_INTERFACE_METHOD_REF_INFO,
            classInfoIndex, nameAndTypeInfoIndex)
    }

    private fun parseConstantNameAndTypeInfo(): ConstantNameAndTypeInfo {
        val nameIndex = convertHexToInt(byteReader.readU2())
        val typeIndex = convertHexToInt(byteReader.readU2())
        return ConstantNameAndTypeInfo(Tag.CONSTANT_NAME_AND_TYPE_INFO, nameIndex, typeIndex)
    }

    private fun parseConstantMethodHandleInfo(): ConstantMethodHandleInfo {
        val referenceKind = convertHexToInt(byteReader.readU1())
        val referenceIndex = convertHexToInt(byteReader.readU2())
        return ConstantMethodHandleInfo(Tag.CONSTANT_METHOD_HANDLE_INFO, referenceKind, referenceIndex)
    }

    private fun parseConstantMethodTypeInfo(): ConstantMethodTypeInfo {
        val descriptorIndex = convertHexToInt(byteReader.readU2())
        return ConstantMethodTypeInfo(Tag.CONSTANT_METHOD_TYPE_INFO, descriptorIndex)
    }

    private fun parseConstantDynamicInfo(): ConstantDynamicInfo {
        val bootstrapMethodAttrIndex = convertHexToInt(byteReader.readU2())
        val nameAndTypeIndex = convertHexToInt(byteReader.readU2())
        return ConstantDynamicInfo(Tag.CONSTANT_DYNAMIC_INFO, bootstrapMethodAttrIndex, nameAndTypeIndex)
    }

    private fun parseConstantInvokeDynamicInfo(): ConstantInvokeDynamicInfo {
        val bootstrapMethodAttrIndex = convertHexToInt(byteReader.readU2())
        val nameAndTypeIndex = convertHexToInt(byteReader.readU2())
        return ConstantInvokeDynamicInfo(Tag.CONSTANT_INVOKE_DYNAMIC_INFO, bootstrapMethodAttrIndex, nameAndTypeIndex)
    }

    private fun parseConstantModuleInfo(): ConstantModuleInfo {
        val nameIndex = convertHexToInt(byteReader.readU2())
        return ConstantModuleInfo(Tag.CONSTANT_MODULE_INFO, nameIndex)
    }

    private fun parseConstantPackageInfo(): ConstantPackageInfo {
        val nameIndex = convertHexToInt(byteReader.readU2())
        return ConstantPackageInfo(Tag.CONSTANT_PACKAGE_INFO, nameIndex)
    }


}