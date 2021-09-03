package com.acmebank.account.manager.error.handler

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.NOT_FOUND)
class DataNotFoundException(message: String) : RuntimeException(message) {


//    constructor(message: String?) {
//        super(message)
//
//    }
}