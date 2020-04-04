package com.hdjnb.classparser.info

abstract class AttributeInfo

enum class Attribute(val attributeName: String) {
    CODE("Code"),
    CONSTANT_VALUE("ConstantValue"),
    DEPRECATED("Deprecated"),
    EXCEPTIONS("Exceptions"),
    INNER_CLASSES("InnerClasses"),
    LINE_NUMBER_TABLE("LineNumberTable"),
    LOCAL_VARIABLE_TABLE("LocalVariableTable"),
    STACK_MAP_TABLE("StackMapTable"),
    SIGNATURE("Signature"),
    SOURCE_FILE("SourceFile"),
    SOURCE_DEBUG_EXTENSION("SourceDebugExtension"),
    SYNTHETIC("Synthetic"),
    BOOTSTRAP_METHODS("BootstrapMethods"),
    METHOD_PARAMETERS("MethodParameters"),
}
data class AttributeCodeInfo(val attributeNameIndex: Int,
                             val attributeLength: Int,
                             val maxStack: Int,
                             val maxLocals: Int,
                             val codeLength: Int,
                             val code: List<String>,
                             val exceptionTableLength: Int,
                             val exceptionTable: List<ExceptionInfo>,
                             val attributeCount: Int,
                             val attributes: List<AttributeInfo>) : AttributeInfo() {

    data class ExceptionInfo(val startPC: Int,
                             val endPC: Int,
                             val handlerPC: Int,
                             val catchType: Int)
}

data class AttributeExceptionsInfo(val attributeNameIndex: Int,
                                   val attributeLength: Int,
                                   val numberOfExceptions: Int,
                                   val exceptionIndexTable: List<Int>) : AttributeInfo()

data class AttributeLineNumberTable(val attributeNameIndex: Int,
                           val attributeLength: Int,
                           val lineNumberTableLength: Int,
                           val lineNumberTable: List<LineNumberInfo>) : AttributeInfo() {
    data class LineNumberInfo(val startPC: Int,
                              val lineNumber: Int)
}

data class AttributeLocalVariableTable(val attributeNameIndex: Int,
                                       val attributeLength: Int,
                                       val localVariableTableLength: Int,
                                       val localVariableTable: List<LocalVariableInfo>) : AttributeInfo() {
    data class LocalVariableInfo(val startPC: Int,
                                 val length: Int,
                                 val nameIndex: Int,
                                 val descriptorIndex: Int,
                                 val index: Int)
}

data class AttributeSourceFile(val attributeNameIndex: Int,
                               val attributeLength: Int,
                               val sourceFileIndex: Int) : AttributeInfo()

data class AttributeSourceDebugExtension(val attributeNameIndex: Int,
                                         val attributeLength: Int,
                                         val debugExtension: List<String>) : AttributeInfo()

data class AttributeConstantValue(val attributeNameIndex: Int,
                                  val attributeLength: Int,
                                  val constantValueIndex: Int) : AttributeInfo()

data class AttributeInnerClasses(val attributeNameIndex: Int,
                                 val attributeLength: Int,
                                 val numberOfInnerClasses: Int,
                                 val innerClasses: List<InnerClassInfo>) : AttributeInfo() {
    data class InnerClassInfo(val innerClassInfoIndex: Int,
                              val outerClassInfoIndex: Int,
                              val innerClassNameIndex: Int,
                              val innerClassAccessFlags: String)
}

data class AttributeDeprecated(val attributeNameIndex: Int,
                               val attributeLength: Int) : AttributeInfo()

data class AttributeSynthetic(val attributeNameIndex: Int,
                              val attributeLength: Int) : AttributeInfo()

data class AttributeStackMapTable(val attributeNameIndex: Int,
                         val attributeLength: Int,
                         val numberOfEntries: Int,
                         val stackMapFrameEntries: List<String>) : AttributeInfo()

data class AttributeSignature(val attributeNameIndex: Int,
                              val attributeLength: Int,
                              val signatureIndex: Int) : AttributeInfo()

data class AttributeBootstrapMethods(val attributeNameIndex: Int,
                                     val attributeLength: Int,
                                     val numberOfBootstrapMethods: Int,
                                     val bootstrapMethods: List<BootstrapMethod>) : AttributeInfo() {
    data class BootstrapMethod(val bootstrapMethodRef: Int,
                               val numberOfBootstrapArguments: Int,
                               val bootstrapArguments: List<Int>)
}

data class AttributeMethodParameters(val attributeNameIndex: Int,
                                     val attributeLength: Int,
                                     val parameterCount: Int,
                                     val parameters: List<Parameter>) : AttributeInfo() {
    data class Parameter(val nameIndex: Int,
                         val accessFlags: String)
}

data class AttributeRuntimeVisibleAnnotations(val attributeNameIndex: Int,
                                              val attributeLength: Int,
                                              val numberOfAnnotation: Int,
                                              val annotations: List<Annotation>) : AttributeInfo() {
    data class Annotation(val typeIndex: Int,
                          val numberOfElementValuePairs: Int,
                          val elementValuePairs: List<Pair<String, String>>)
}
