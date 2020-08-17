package com.unis.kotlin.api

import com.unis.kotlin.api.result.BaseResult

class BaseObjectResult<T> : BaseResult(){

    private var data: T? = null

}