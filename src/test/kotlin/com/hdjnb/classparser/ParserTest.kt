package com.hdjnb.classparser

import com.hdjnb.classparser.info.*
import org.hamcrest.Matchers
import org.hamcrest.core.IsEqual
import org.junit.*
import org.junit.Assert.assertThat

class ParserTest {
    private lateinit var parser: Parser

    private lateinit var byteReader: ByteReader

    private lateinit var constants: List<ConstantInfo>

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
        testParseMagicNumber()
        val minorVersionInfo = parser.parseMinorVersion()
        val majorVersionInfo = parser.parseMajorVersion()
        assertThat(majorVersionInfo.value.toDouble(), Matchers.greaterThan(45.3))
    }

    @Test
    fun testParseConstantPool() {
        testParseMinorAndMajorVersion()
        parser.parseConstPool()
        constants = parser.constants
        constants.forEach {
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
                    assertThat(it.classInfoIndex, Matchers.lessThanOrEqualTo(constants.size))
                    assertThat(it.nameAndTypeInfoIndex, Matchers.lessThanOrEqualTo(constants.size))
                }
                is ConstantMethodRefInfo -> {
                    assertThat(it.tag, Matchers.equalTo(Tag.CONSTANT_METHOD_REF_INFO))
                    assertThat(it.classInfoIndex, Matchers.lessThanOrEqualTo(constants.size))
                    assertThat(it.nameAndTypeInfoIndex, Matchers.lessThanOrEqualTo(constants.size))
                }
                is ConstantInterfaceMethodRefInfo -> {
                    assertThat(it.tag, Matchers.equalTo(Tag.CONSTANT_INTERFACE_METHOD_REF_INFO))
                    assertThat(it.classInfoIndex, Matchers.lessThanOrEqualTo(constants.size))
                    assertThat(it.nameAndTypeInfoIndex, Matchers.lessThanOrEqualTo(constants.size))
                }
                is ConstantNameAndTypeInfo -> {
                    assertThat(it.tag, Matchers.equalTo(Tag.CONSTANT_NAME_AND_TYPE_INFO))
                    assertThat(it.nameIndex, Matchers.lessThanOrEqualTo(constants.size))
                    assertThat(it.typeIndex, Matchers.lessThanOrEqualTo(constants.size))
                }
                is ConstantMethodHandleInfo -> {
                    assertThat(it.tag, Matchers.equalTo(Tag.CONSTANT_METHOD_HANDLE_INFO))
                    assertThat(it.referenceKind, Matchers.isIn(listOf(1, 2, 3, 4, 5, 6, 7, 8, 9)))
                    assertThat(it.referenceIndex, Matchers.lessThanOrEqualTo(constants.size))
                }
                is ConstantMethodTypeInfo -> {
                    assertThat(it.tag, Matchers.equalTo(Tag.CONSTANT_METHOD_TYPE_INFO))
                    assertThat(it.descriptorIndex, Matchers.lessThanOrEqualTo(constants.size))
                }
                is ConstantDynamicInfo -> {
                    assertThat(it.tag, Matchers.equalTo(Tag.CONSTANT_DYNAMIC_INFO))
        //                    assertThat(it.bootstrapMethodAttrIndex)
                    assertThat(it.nameAndTypeIndex, Matchers.lessThanOrEqualTo(constants.size))
                }
                is ConstantInvokeDynamicInfo -> {
                    assertThat(it.tag, Matchers.equalTo(Tag.CONSTANT_INVOKE_DYNAMIC_INFO))
        //                    assertThat(it.bootstrapMethodAttrIndex)
                    assertThat(it.nameAndTypeIndex, Matchers.lessThanOrEqualTo(constants.size))
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
        testParseConstantPool()
        val accessFlagsInfo = parser.parseAccessFlagsInfo()
        assertThat(accessFlagsInfo.length, Matchers.equalTo(4))
    }

    @Test
    fun testParseClassExtensionInfo() {
        testParseAccessFlagsInfo()
        val classExtensionInfo = parser.parseClassExtensionInfo()
        assertThat(classExtensionInfo.thisClassIndex, Matchers.greaterThan(0))
        assertThat(classExtensionInfo.thisClassIndex, Matchers.lessThanOrEqualTo(constants.size))
        assertThat(classExtensionInfo.superClassIndex, Matchers.greaterThan(0))
        assertThat(classExtensionInfo.superClassIndex, Matchers.lessThanOrEqualTo(constants.size))
        if (classExtensionInfo.interfaceIndexes.isNotEmpty()) {
            classExtensionInfo.interfaceIndexes.forEach {
                assertThat(it, Matchers.greaterThan(0))
                assertThat(it, Matchers.lessThanOrEqualTo(constants.size))
            }
        }
    }

    @Test
    fun testParseFieldInfos() {
        testParseClassExtensionInfo()
        val fieldInfos = parser.parseFieldInfos()
        assertThat(fieldInfos.size, Matchers.greaterThan(0))
        fieldInfos.forEach {
            assertThat(it.nameIndex, Matchers.lessThanOrEqualTo(constants.size))
            assertThat(it.descriptorIndex, Matchers.lessThanOrEqualTo(constants.size))
            if (it.attributesCount > 0) {
                assertThat(it.attributeInfos.size, Matchers.equalTo(it.attributesCount))
            }
        }
    }

    @Test
    fun testParseMethodInfos() {
        testParseFieldInfos()
        val methodInfos = parser.parseMethodInfos()
        methodInfos.forEach {
            assertThat(it.nameIndex, Matchers.lessThanOrEqualTo(constants.size))
            assertThat(it.descriptorIndex, Matchers.lessThanOrEqualTo(constants.size))
            if (it.attributesCount > 0) {
                assertThat(it.attributeInfos.size, Matchers.equalTo(it.attributesCount))
            }
        }
    }
}