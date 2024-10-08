package ru.practicum.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(final NotFoundException e) {
        return new ErrorResponse("Искомый объект не найден", e.getMessage());
    }

    @ExceptionHandler({ValidationException.class, MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(final Exception e) {
        return new ErrorResponse("Ошибка валидации", e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleException(final Exception e) {

        return new ErrorResponse("Исключения", e.getMessage());
    }

//    @ExceptionHandler(SameEmailException.class)
//    @ResponseStatus(HttpStatus.CONFLICT)
//    public ErrorResponse handleSameEmailException(final SameEmailException e) {
//        return new ErrorResponse("Пользователь с таким email уже существует ", e.getMessage());
//    }
}
