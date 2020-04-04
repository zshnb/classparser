package com.hdjnb.classparser.info

data class ClassExtensionInfo(val thisClassIndex: Int,
                              val superClassIndex: Int,
                              val interfacesCount: Int,
                              var interfaceIndexes: List<Int> = emptyList()) {
    override fun toString(): String {
        return ""
    }
}