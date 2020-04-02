package com.hdjnb.classparser

import com.hdjnb.classparser.BaseUtil.Companion.convertHexToInt
import com.hdjnb.classparser.BaseUtil.Companion.convertHexToLong
import com.hdjnb.classparser.BaseUtil.Companion.convertHexToString
import com.hdjnb.classparser.info.*
import com.hdjnb.classparser.info.AttributeBootstrapMethods.BootstrapMethod
import com.hdjnb.classparser.info.AttributeCodeInfo.ExceptionInfo
import com.hdjnb.classparser.info.AttributeInnerClasses.InnerClassInfo
import com.hdjnb.classparser.info.AttributeLineNumberTable.LineNumberInfo
import com.hdjnb.classparser.info.AttributeLocalVariableTable.LocalVariableInfo
import com.hdjnb.classparser.info.AttributeMethodParameters.Parameter
import com.hdjnb.classparser.info.AttributeRuntimeVisibleAnnotations.Annotation

class Parser(private val byteReader: ByteReader) {
    private val constants: MutableList<ConstantInfo> = mutableListOf()

    fun parseMagicNumber(): MagicNumberInfo = MagicNumberInfo(byteReader.readU4())

    fun parseMinorVersion(): MinorVersionInfo =
        MinorVersionInfo(convertHexToInt(byteReader.readU2()))

    fun parseMajorVersion(): MajorVersionInfo =
        MajorVersionInfo(convertHexToInt(byteReader.readU2()))

    fun parseConstPool() {
        var constantPoolCount = convertHexToInt(byteReader.readU2())
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

    private fun parseAttributeCodeInfo(): AttributeCodeInfo {
        val attributeNameIndex = convertHexToInt(byteReader.readU2())
        val attributeLength = convertHexToInt(byteReader.readU4())
        val maxStack = convertHexToInt(byteReader.readU2())
        val maxLocals = convertHexToInt(byteReader.readU2())
        val codeLength = convertHexToInt(byteReader.readU4())
        val code = (0 until codeLength).map {
            byteReader.readU1()
        }
        val exceptionTableLength = convertHexToInt(byteReader.readU2())
        val exceptionTable = if (exceptionTableLength > 0) {
            (0 until exceptionTableLength).map {
                val startPC = convertHexToInt(byteReader.readU2())
                val endPC = convertHexToInt(byteReader.readU2())
                val handlerPC = convertHexToInt(byteReader.readU2())
                val catchType = convertHexToInt(byteReader.readU2())
                ExceptionInfo(startPC, endPC, handlerPC, catchType)
            }
        } else {
            emptyList()
        }
        val attributeCount = convertHexToInt(byteReader.readU2())
        val attributes = if (attributeCount > 0) {
            (0 until attributeCount).map {
                val attributeNameIndex = convertHexToInt(byteReader.readU2())
                when ((constants[attributeNameIndex] as ConstantUtf8Info).bytes) {
                    "LineNumberTable" -> {
                        parseAttributeLineNumberTable()
                    }
                    "LocalVariableTable" -> {
                        parseAttributeLocalVariableTable()
                    }
                    "StackMapTable" -> {
                        parseAttributeStackMapTable()
                    }
                    else -> {
                        throw Exception("Unsupported attribute type in AttributeCodeInfo")
                    }
                }
            }
        } else {
            emptyList()
        }
        return AttributeCodeInfo(attributeNameIndex, attributeLength, maxStack, maxLocals, codeLength, code,
            exceptionTableLength, exceptionTable, attributeCount, attributes)
    }

    private fun parseAttributeExceptionsInfo(): AttributeExceptionsInfo {
        val attributeNameIndex = convertHexToInt(byteReader.readU2())
        val attributeLength = convertHexToInt(byteReader.readU4())
        val numberOfExceptions = convertHexToInt(byteReader.readU2())
        val exceptionIndexTable = (0 until numberOfExceptions).map {
            convertHexToInt(byteReader.readU2())
        }
        return AttributeExceptionsInfo(attributeNameIndex, attributeLength, numberOfExceptions, exceptionIndexTable)
    }

    private fun parseAttributeLineNumberTable(): AttributeLineNumberTable {
        val attributeNameIndex = convertHexToInt(byteReader.readU2())
        val attributeLength = convertHexToInt(byteReader.readU4())
        val lineNumberTableLength = convertHexToInt(byteReader.readU2())
        val lineNumberTable = (0 until lineNumberTableLength).map {
            val startPC = convertHexToInt(byteReader.readU2())
            val lineNumber = convertHexToInt(byteReader.readU2())
            LineNumberInfo(startPC, lineNumber)
        }
        return AttributeLineNumberTable(attributeNameIndex, attributeLength, lineNumberTableLength, lineNumberTable)
    }

    private fun parseAttributeLocalVariableTable(): AttributeLocalVariableTable {
        val attributeNameIndex = convertHexToInt(byteReader.readU2())
        val attributeLength = convertHexToInt(byteReader.readU4())
        val localVariableTableLength = convertHexToInt(byteReader.readU2())
        val localVariableTable = (0 until localVariableTableLength).map {
            val startPC = convertHexToInt(byteReader.readU2())
            val length = convertHexToInt(byteReader.readU2())
            val nameIndex = convertHexToInt(byteReader.readU2())
            val descriptorIndex = convertHexToInt(byteReader.readU2())
            val index = convertHexToInt(byteReader.readU2())
            LocalVariableInfo(startPC, length, nameIndex, descriptorIndex, index)
        }
        return AttributeLocalVariableTable(attributeNameIndex, attributeLength, localVariableTableLength, localVariableTable)
    }

    private fun parseAttributeSourceFile(): AttributeSourceFile {
        val attributeNameIndex = convertHexToInt(byteReader.readU2())
        val attributeLength = convertHexToInt(byteReader.readU4())
        val sourceFileIndex = convertHexToInt(byteReader.readU2())
        return AttributeSourceFile(attributeNameIndex, attributeLength, sourceFileIndex)
    }

    private fun parseAttributeSourceDebugExtension(): AttributeSourceDebugExtension {
        val attributeNameIndex = convertHexToInt(byteReader.readU2())
        val attributeLength = convertHexToInt(byteReader.readU4())
        val debugExtension = (0 until attributeLength).map {
            byteReader.readU1()
        }
        return AttributeSourceDebugExtension(attributeNameIndex, attributeLength, debugExtension)
    }

    private fun parseAttributeConstantValue(): AttributeConstantValue {
        val attributeNameIndex = convertHexToInt(byteReader.readU2())
        val attributeLength = convertHexToInt(byteReader.readU4())
        val constantValueIndex = convertHexToInt(byteReader.readU2())
        return AttributeConstantValue(attributeNameIndex, attributeLength, constantValueIndex)
    }

    private fun parseAttributeInnerClasses(): AttributeInnerClasses {
        val attributeNameIndex = convertHexToInt(byteReader.readU2())
        val attributeLength = convertHexToInt(byteReader.readU4())
        val numberOfClasses = convertHexToInt(byteReader.readU2())
        val innerClasses = (0 until numberOfClasses).map {
            val innerClassInfoIndex = convertHexToInt(byteReader.readU2())
            val outerClassInfoIndex = convertHexToInt(byteReader.readU2())
            val innerNameIndex = convertHexToInt(byteReader.readU2())
            val innerClassAccessFlags = byteReader.readU2()
            InnerClassInfo(innerClassInfoIndex, outerClassInfoIndex, innerNameIndex, innerClassAccessFlags)
        }
        return AttributeInnerClasses(attributeNameIndex, attributeLength, numberOfClasses, innerClasses)
    }

    private fun parseAttributeDeprecated(): AttributeDeprecated {
        val attributeNameIndex = convertHexToInt(byteReader.readU2())
        val attributeLength = convertHexToInt(byteReader.readU4())
        return AttributeDeprecated(attributeNameIndex, attributeLength)
    }

    private fun parseAttributeSynthetic(): AttributeSynthetic {
        val attributeNameIndex = convertHexToInt(byteReader.readU2())
        val attributeLength = convertHexToInt(byteReader.readU4())
        return AttributeSynthetic(attributeNameIndex, attributeLength)
    }

    private fun parseAttributeStackMapTable(): AttributeStackMapTable {
        val attributeNameIndex = convertHexToInt(byteReader.readU2())
        val attributeLength = convertHexToInt(byteReader.readU4())
        val numberOfEntries = convertHexToInt(byteReader.readU2())
        val stackMapFrameEntries = (0 until numberOfEntries).map {
            byteReader.readU1()
        }
        return AttributeStackMapTable(attributeNameIndex, attributeLength, numberOfEntries, stackMapFrameEntries)
    }

    private fun parseAttributeSignature(): AttributeSignature {
        val attributeNameIndex = convertHexToInt(byteReader.readU2())
        val attributeLength = convertHexToInt(byteReader.readU4())
        val signatureIndex = convertHexToInt(byteReader.readU2())
        return AttributeSignature(attributeNameIndex, attributeLength, signatureIndex)
    }

    private fun parseAttributeBootstrapMethods(): AttributeBootstrapMethods {
        val attributeNameIndex = convertHexToInt(byteReader.readU2())
        val attributeLength = convertHexToInt(byteReader.readU4())
        val numberOfBootstrapMethods = convertHexToInt(byteReader.readU2())
        val bootstrapMethods = (0 until numberOfBootstrapMethods).map {
            val bootstrapMethodRef = convertHexToInt(byteReader.readU2())
            val numberOfBootstrapArguments = convertHexToInt(byteReader.readU2())
            val bootstrapArguments = (0 until numberOfBootstrapArguments).map {
                convertHexToInt(byteReader.readU2())
            }
            BootstrapMethod(bootstrapMethodRef, numberOfBootstrapArguments, bootstrapArguments)
        }
        return AttributeBootstrapMethods(attributeNameIndex, attributeLength, numberOfBootstrapMethods, bootstrapMethods)
    }

    private fun parseAttributeMethodParameters(): AttributeMethodParameters {
        val attributeNameIndex = convertHexToInt(byteReader.readU2())
        val attributeLength = convertHexToInt(byteReader.readU4())
        val parametersCount = convertHexToInt(byteReader.readU1())
        val parameters = (0 until parametersCount).map {
            val nameIndex = convertHexToInt(byteReader.readU2())
            val accessFlags = byteReader.readU2()
            Parameter(nameIndex, accessFlags)
        }
        return AttributeMethodParameters(attributeNameIndex, attributeLength, parametersCount, parameters)
    }

    private fun parseAttributeRuntimeVisibleAnnotations(): AttributeRuntimeVisibleAnnotations {
        val attributeNameIndex = convertHexToInt(byteReader.readU2())
        val attributeLength = convertHexToInt(byteReader.readU4())
        val numberOfAnnotations = convertHexToInt(byteReader.readU2())
        val annotations = (0 until numberOfAnnotations).map {
            val typeIndex = convertHexToInt(byteReader.readU2())
            val numberOfElementValuePairs = convertHexToInt(byteReader.readU2())
            val elementValuePairs = (0 until numberOfElementValuePairs).map {
                byteReader.readU2() to byteReader.readU2()
            }
            Annotation(typeIndex, numberOfElementValuePairs, elementValuePairs)
        }
        return AttributeRuntimeVisibleAnnotations(attributeNameIndex, attributeLength, numberOfAnnotations, annotations)
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