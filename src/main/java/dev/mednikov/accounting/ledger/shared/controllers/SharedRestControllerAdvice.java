package dev.mednikov.accounting.ledger.shared.controllers;

import dev.mednikov.accounting.ledger.journals.exceptions.NotBalancedEntryException;
import dev.mednikov.accounting.ledger.shared.exceptions.ItemAlreadyExistsException;
import dev.mednikov.accounting.ledger.shared.exceptions.ItemDoesNotExistException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class SharedRestControllerAdvice {

    @ExceptionHandler(ItemAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_CONTENT)
    public String itemAlreadyExistsHandler (ItemAlreadyExistsException ex){
        return ex.getMessage();
    }

    @ExceptionHandler(ItemDoesNotExistException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String itemDoesNotExistHandler (ItemDoesNotExistException ex){
        return ex.getMessage();
    }

    @ExceptionHandler(NotBalancedEntryException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String notBalancedEntryHandler (NotBalancedEntryException ex){
        return ex.getMessage();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> methodArgumentNotValidHandler (MethodArgumentNotValidException ex){
        Map<String, String> result = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            result.put(fieldName, message);
        });
        return result;
    }


}
