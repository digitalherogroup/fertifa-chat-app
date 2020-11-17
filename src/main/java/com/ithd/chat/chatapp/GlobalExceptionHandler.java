package com.ithd.chat.chatapp;

import com.ithd.chat.chatapp.exceptions.ObjectNullException;
import com.ithd.chat.chatapp.exceptions.ResponseConverterException;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResponseConverterException.class)
    public ResponseEntity<String> responseConverterException(ResponseConverterException responseConverterException) {
        return new ResponseEntity<>(responseConverterException.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ObjectNullException.class)
    public ResponseEntity<String> responseConverterException(ObjectNullException objectNullException) {
        return new ResponseEntity<>(objectNullException.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FileUploadException.class)
    public ResponseEntity<String> responseUploadException(FileUploadException fileUploadException) {
        return new ResponseEntity<>(fileUploadException.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
