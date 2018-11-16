package com.tabelini.demoapi.web

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class RestResponseEntityExceptionHandler: ResponseEntityExceptionHandler() {
    @ExceptionHandler(RestResourceNotFound::class)
    fun handleNotFoundException(ex:RestResourceNotFound, request:WebRequest):ResponseEntity<Map<String,Any>> {
        return ResponseEntity(mapOf("message" to "${ex.message}", "id" to ex.id), HttpStatus.NOT_FOUND)
    }
}

class RestResourceNotFound(msg:String, val id:String) : Exception(msg)
