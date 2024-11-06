package com.example.movie.common;

import com.example.movie.model.ResultBase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class IdInvalidException extends Exception{
    @ExceptionHandler(value = IdInvalidException.class)
    public ResponseEntity<ResultBase<Object>> handleIdException(IdInvalidException idException) {
        ResultBase<Object> res = new ResultBase<>();
        res.setStatus(HttpStatus.BAD_REQUEST.value());
        res.setError(idException.getMessage());
        res.setMessage("IdInvalidException");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }
}
