package com.shiep.jwt.config;

import io.jsonwebtoken.SignatureException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class PermissionHandler {
    @ExceptionHandler(value = { SignatureException.class })
    @ResponseBody
    public ResponseEntity<String> authorizationException(SignatureException e){
        return ResponseEntity.status(401).build();
        //error(new ExceptionInfoBO(1008,e.getMessage()));
    }
}