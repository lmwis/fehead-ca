package com.fehead.open.ca.advice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fehead.lang.error.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @Description: 异常处理类
 * @Author lmwis
 * @Date 2019-11-13 14:08
 * @Version 1.0
 */
@ControllerAdvice
public class ExceptionAdvice {

    private final ObjectMapper objectMapper;

    public ExceptionAdvice(ObjectMapper objectMapper){
        this.objectMapper = objectMapper;
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity handleBusinessException(BusinessException e) throws JsonProcessingException {


        return new ResponseEntity<>(objectMapper.writeValueAsString(e)
                , HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity handleJsonProcessingException(JsonProcessingException e) {


        return new ResponseEntity<>(e.getMessage()
                , HttpStatus.BAD_REQUEST);
    }

}
