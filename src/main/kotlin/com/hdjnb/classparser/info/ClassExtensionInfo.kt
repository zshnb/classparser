package com.hdjnb.classparser.info

data class ClassExtensionInfo(val thisClassIndex: Int,
                              val superClassIndex: Int,
                              var interfaceIndexes: List<Int> = emptyList())