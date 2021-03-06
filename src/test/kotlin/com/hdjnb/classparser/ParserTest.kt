package com.hdjnb.classparser

import com.hdjnb.classparser.info.*
import org.hamcrest.Matchers
import org.hamcrest.core.IsEqual
import org.junit.*
import org.junit.Assert.assertThat

class ParserTest {
    private lateinit var parser: Parser

    private lateinit var byteReader: ByteReader

    private lateinit var constantPool: ConstantPool

    @Before
    fun before() {
        byteReader = ByteReader(TestUtil.filePath)
        parser = Parser(byteReader)
    }

    @Test
    fun testParseMagicNumber() {
        val magicNumberInfo = parser.parseMagicNumber()
        assertThat(magicNumberInfo.value, IsEqual.equalTo("CAFEBABE"))
    }

    @Test
    fun testParseMinorAndMajorVersion() {
        parser.parseMagicNumber()
        val minorVersionInfo = parser.parseMinorVersion()
        val majorVersionInfo = parser.parseMajorVersion()
        assertThat(majorVersionInfo.value.toDouble(), Matchers.greaterThan(45.3))
    }

    @Test
    fun testParseConstantPool() {
        parser.parseMagicNumber()
        parser.parseMinorVersion()
        parser.parseMajorVersion()
        constantPool = parser.parseConstPool()
        constantPool.constants.forEach {
            when (it) {
                is ConstantClassInfo -> {
                    assertThat(it.tag, Matchers.equalTo(Tag.CONSTANT_CLASS_INFO))
                    assertThat(it.nameIndex, Matchers.lessThanOrEqualTo(65535))
                }
                is ConstantUtf8Info -> {
                    assertThat(it.tag, Matchers.equalTo(Tag.CONSTANT_UTF8_INFO))
                    assertThat(it.length, Matchers.greaterThan(0))
                    assertThat(it.bytes, Matchers.isA(String::class.java))
                }
                is ConstantIntegerInfo -> {
                    assertThat(it.tag, Matchers.equalTo(Tag.CONSTANT_INTEGER_INFO))
                    assertThat(it.bytes, Matchers.isA(Int::class.java))
                }
                is ConstantFloatInfo -> {
                    assertThat(it.tag, Matchers.equalTo(Tag.CONSTANT_FLOAT_INFO))
                    assertThat(it.bytes, Matchers.isA(Float::class.java))
                }
                is ConstantLongInfo -> {
                    assertThat(it.tag, Matchers.equalTo(Tag.CONSTANT_LONG_INFO))
                    assertThat(it.bytes, Matchers.isA(Long::class.java))
                }
                is ConstantDoubleInfo -> {
                    assertThat(it.tag, Matchers.equalTo(Tag.CONSTANT_DOUBLE_INFO))
                    assertThat(it.bytes, Matchers.isA(Double::class.java))
                }
                is ConstantStringInfo -> {
                    assertThat(it.tag, Matchers.equalTo(Tag.CONSTANT_STRING_INFO))
                    assertThat(it.index, Matchers.lessThanOrEqualTo(65535))
                }
                is ConstantFieldRefInfo -> {
                    assertThat(it.tag, Matchers.equalTo(Tag.CONSTANT_FIELD_REF_INFO))
                    assertThat(it.classInfoIndex, Matchers.lessThanOrEqualTo(constantPool.constantsCount))
                    assertThat(it.nameAndTypeInfoIndex, Matchers.lessThanOrEqualTo(constantPool.constantsCount))
                }
                is ConstantMethodRefInfo -> {
                    assertThat(it.tag, Matchers.equalTo(Tag.CONSTANT_METHOD_REF_INFO))
                    assertThat(it.classInfoIndex, Matchers.lessThanOrEqualTo(constantPool.constantsCount))
                    assertThat(it.nameAndTypeInfoIndex, Matchers.lessThanOrEqualTo(constantPool.constantsCount))
                }
                is ConstantInterfaceMethodRefInfo -> {
                    assertThat(it.tag, Matchers.equalTo(Tag.CONSTANT_INTERFACE_METHOD_REF_INFO))
                    assertThat(it.classInfoIndex, Matchers.lessThanOrEqualTo(constantPool.constantsCount))
                    assertThat(it.nameAndTypeInfoIndex, Matchers.lessThanOrEqualTo(constantPool.constantsCount))
                }
                is ConstantNameAndTypeInfo -> {
                    assertThat(it.tag, Matchers.equalTo(Tag.CONSTANT_NAME_AND_TYPE_INFO))
                    assertThat(it.nameIndex, Matchers.lessThanOrEqualTo(constantPool.constantsCount))
                    assertThat(it.typeIndex, Matchers.lessThanOrEqualTo(constantPool.constantsCount))
                }
                is ConstantMethodHandleInfo -> {
                    assertThat(it.tag, Matchers.equalTo(Tag.CONSTANT_METHOD_HANDLE_INFO))
                    assertThat(it.referenceKind, Matchers.isIn(listOf(1, 2, 3, 4, 5, 6, 7, 8, 9)))
                    assertThat(it.referenceIndex, Matchers.lessThanOrEqualTo(constantPool.constantsCount))
                }
                is ConstantMethodTypeInfo -> {
                    assertThat(it.tag, Matchers.equalTo(Tag.CONSTANT_METHOD_TYPE_INFO))
                    assertThat(it.descriptorIndex, Matchers.lessThanOrEqualTo(constantPool.constantsCount))
                }
                is ConstantDynamicInfo -> {
                    assertThat(it.tag, Matchers.equalTo(Tag.CONSTANT_DYNAMIC_INFO))
        //                    assertThat(it.bootstrapMethodAttrIndex)
                    assertThat(it.nameAndTypeIndex, Matchers.lessThanOrEqualTo(constantPool.constantsCount))
                }
                is ConstantInvokeDynamicInfo -> {
                    assertThat(it.tag, Matchers.equalTo(Tag.CONSTANT_INVOKE_DYNAMIC_INFO))
        //                    assertThat(it.bootstrapMethodAttrIndex)
                    assertThat(it.nameAndTypeIndex, Matchers.lessThanOrEqualTo(constantPool.constantsCount))
                }
                /*
                * TODO I just want parse class file which version is below JDK8, so it's unnecessary to test module and
                *      package info
                * */
            }
        }
    }

    @Test
    fun testParseAccessFlagsInfo() {
        parser.parseMagicNumber()
        parser.parseMinorVersion()
        parser.parseMajorVersion()
        parser.parseConstPool()
        val accessFlagsInfo = parser.parseAccessFlagsInfo()
        assertThat(accessFlagsInfo.length, Matchers.equalTo(4))
    }

    @Test
    fun testParseClassExtensionInfo() {
        parser.parseMagicNumber()
        parser.parseMinorVersion()
        parser.parseMajorVersion()
        parser.parseConstPool()
        parser.parseAccessFlagsInfo()
        val classExtensionInfo = parser.parseClassExtensionInfo()
        assertThat(classExtensionInfo.thisClassIndex, Matchers.greaterThan(0))
        assertThat(classExtensionInfo.thisClassIndex, Matchers.lessThanOrEqualTo(constantPool.constantsCount))
        assertThat(classExtensionInfo.superClassIndex, Matchers.greaterThan(0))
        assertThat(classExtensionInfo.superClassIndex, Matchers.lessThanOrEqualTo(constantPool.constantsCount))
        if (classExtensionInfo.interfaceIndexes.isNotEmpty()) {
            classExtensionInfo.interfaceIndexes.forEach {
                assertThat(it, Matchers.greaterThan(0))
                assertThat(it, Matchers.lessThanOrEqualTo(constantPool.constantsCount))
            }
        }
    }

    @Test
    fun testParseFieldInfos() {
        parser.parseMagicNumber()
        parser.parseMinorVersion()
        parser.parseMajorVersion()
        constantPool = parser.parseConstPool()
        parser.parseAccessFlagsInfo()
        parser.parseClassExtensionInfo()
        val fieldInfos = parser.parseFieldInfos(constantPool)
        fieldInfos.forEach {
            println(it.toString(constantPool))
        }
        assertThat(fieldInfos.size, Matchers.greaterThan(0))
        fieldInfos.forEach {
            assertThat(it.nameIndex, Matchers.lessThanOrEqualTo(constantPool.constantsCount))
            assertThat(it.descriptorIndex, Matchers.lessThanOrEqualTo(constantPool.constantsCount))
            if (it.attributesCount > 0) {
                assertThat(it.attributeInfos.size, Matchers.equalTo(it.attributesCount))
            }
        }
    }

    @Test
    fun testParseMethodInfos() {
        parser.parseMagicNumber()
        parser.parseMinorVersion()
        parser.parseMajorVersion()
        constantPool = parser.parseConstPool()
        parser.parseAccessFlagsInfo()
        parser.parseClassExtensionInfo()
        parser.parseFieldInfos(constantPool)
        val methodInfos = parser.parseMethodInfos(constantPool)
        methodInfos.forEach {
            println(it.toString(constantPool))
        }
        methodInfos.forEach {
            assertThat(it.nameIndex, Matchers.lessThanOrEqualTo(constantPool.constantsCount))
            assertThat(it.descriptorIndex, Matchers.lessThanOrEqualTo(constantPool.constantsCount))
            if (it.attributesCount > 0) {
                assertThat(it.attributeInfos.size, Matchers.equalTo(it.attributesCount))
            }
        }
    }

    @Test
    fun testParseClassAttributeInfo() {
        parser.parseMagicNumber()
        parser.parseMinorVersion()
        parser.parseMajorVersion()
        constantPool = parser.parseConstPool()
        parser.parseAccessFlagsInfo()
        parser.parseClassExtensionInfo()
        parser.parseFieldInfos(constantPool)
        parser.parseMethodInfos(constantPool)
        val attributeInfos = parser.parseAttributeInfos(constantPool)
        attributeInfos.forEach {

        }
    }
}