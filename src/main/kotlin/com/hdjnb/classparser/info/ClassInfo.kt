package com.hdjnb.classparser.info

class ClassInfo(private val magicNumberInfo: MagicNumberInfo,
                private val minorVersionInfo: MinorVersionInfo,
                private val majorVersionInfo: MajorVersionInfo,
                private val constantPool: ConstantPool,
                private val accessFlagsInfo: AccessFlagsInfo,
                private val classExtensionInfo: ClassExtensionInfo,
                private val fieldsCount: Int,
                private val fields: List<FieldInfo>,
                private val methodsCount: Int,
                private val methods: List<MethodInfo>,
                private val attributesCount: Int,
                private val attributes: List<AttributeInfo>) {

    override fun toString(): String {
        return ""
    }
}