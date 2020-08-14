package com.unis.kotlin.api

import java.lang.RuntimeException

class ApiException : RuntimeException{

    private var code: Int = 0

    constructor(code: Int, message: String): super(message){

        this.code = code
    }

    fun getCode(): Int {
        return code
    }

    fun setCode(code: Int) {
        this.code = code
    }

}