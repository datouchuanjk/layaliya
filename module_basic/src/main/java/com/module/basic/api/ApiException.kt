package com.module.basic.api

/**
 * 服务器的异常
 */
class ApiException(val code: Int, message: String?) : Exception(message)