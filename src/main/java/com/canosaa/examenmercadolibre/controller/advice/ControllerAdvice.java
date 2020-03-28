package com.canosaa.examenmercadolibre.controller.advice;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {
    
    private static final Logger log = LoggerFactory.getLogger(ControllerAdvice.class);
    
    @ExceptionHandler(IllegalArgumentException.class)
    public void handleIllegalArgument(IllegalArgumentException exception, HttpServletResponse response) throws IOException{
        log.error("Error en request: " + exception);
        response.sendError(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
    }
    
    
}
