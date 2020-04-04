package com.hdjnb.classparser

import com.hdjnb.classparser.info.*
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers

class AttributeInfoChecker(val constants: List<ConstantInfo>) {

    fun checkAttributeCodeInfo(attributeCodeInfo: AttributeCodeInfo) {
        assertThat((constants[attributeCodeInfo.attributeNameIndex] as ConstantUtf8Info).bytes, Matchers.equalTo("Code"))

    }
}
