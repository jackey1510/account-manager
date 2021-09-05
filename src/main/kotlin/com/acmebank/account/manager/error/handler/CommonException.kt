package com.acmebank.account.manager.error.handler

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
abstract class CommonException(message: String?) : RuntimeException(message) {
}