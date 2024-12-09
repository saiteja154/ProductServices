package org.saiteja.productservice.advices;

import org.saiteja.productservice.dtos.ErrorDTO;
import org.saiteja.productservice.exceptions.ProductNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

@org.springframework.web.bind.annotation.ControllerAdvice
public class ControllerAdvice {
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleProductNotFoundException(ProductNotFoundException productNotFoundException){
        ErrorDTO errorDTO = new ErrorDTO();

        errorDTO.setMessage(productNotFoundException.getMessage());

        ResponseEntity<ErrorDTO> responseEntity = new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);

        return responseEntity;
    }
}