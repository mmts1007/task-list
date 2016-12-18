package com.example.handler

import com.example.api.exception.NotFoundException
import com.example.entity.ErrorEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.context.MessageSourceResolvable
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class ApiExceptionHandler : ResponseEntityExceptionHandler() {
    @Autowired
    lateinit var messageSource: MessageSource

    override fun handleMethodArgumentNotValid(
            e: MethodArgumentNotValidException,
            headers: HttpHeaders,
            status: HttpStatus,
            request: WebRequest
    ): ResponseEntity<Any> {
        val errorEntity = createErrorEntity(status)
        for (fieldError in e.bindingResult.fieldErrors) {
            errorEntity.addDetail("${fieldError.field} ${getMessage(fieldError, request)}")
        }

        return super.handleExceptionInternal(e, errorEntity, headers, status, request)
    }

    @ExceptionHandler
    fun handleNotFoundException(e: NotFoundException, request: WebRequest): ResponseEntity<Any> {
        val status = HttpStatus.NOT_FOUND
        val errorEntity = createErrorEntity(e, status, "Specified resource is not found")
        return handleExceptionInternal(e, errorEntity, null, status, request)
    }

    @ExceptionHandler
    fun handleSystemException(e: Exception, request: WebRequest): ResponseEntity<Any> {
        val status = HttpStatus.INTERNAL_SERVER_ERROR
        val errorEntity = createErrorEntity(e, status, "System error is occurred")
        return handleExceptionInternal(e, errorEntity, null, status, request)
    }

    private fun createErrorEntity(e: Exception, status: HttpStatus, defaultMessage: String): ErrorEntity {
        val errorEntity = createErrorEntity(status)
        errorEntity.addDetail(defaultMessage)
        return errorEntity
    }

    private fun createErrorEntity(status: HttpStatus): ErrorEntity {
        val errorEntity = ErrorEntity(status)
        return errorEntity
    }

    private fun getMessage(resolvable: MessageSourceResolvable, request: WebRequest): String {
        return messageSource.getMessage(resolvable, request.locale)
    }
}
