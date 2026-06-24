package com.innowise.authservice.exception.handler;


import com.innowise.authservice.dto.ErrorResponseDto;
import com.innowise.authservice.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDto> handleUsernameAlreadyExistsException(
            UsernameAlreadyExistsException ex,
            HttpServletRequest request
    ) {
        ErrorResponseDto responseDto = new ErrorResponseDto(
                ex.getMessage(),
                HttpStatus.CONFLICT,
                request.getRequestURI()
        );

        return new ResponseEntity<>(responseDto, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleUsernameNotFoundException(
            UsernameNotFoundException ex,
            HttpServletRequest request
    ) {
        ErrorResponseDto responseDto = new ErrorResponseDto(
                ex.getMessage(),
                HttpStatus.UNAUTHORIZED,
                request.getRequestURI()
        );

        return new ResponseEntity<>(responseDto, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(WrongPasswordException.class)
    public ResponseEntity<ErrorResponseDto> handleWrongPasswordException(
            WrongPasswordException ex,
            HttpServletRequest request
    ) {
        ErrorResponseDto responseDto = new ErrorResponseDto(
                ex.getMessage(),
                HttpStatus.UNAUTHORIZED,
                request.getRequestURI()
        );

        return new ResponseEntity<>(responseDto, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(KeycloakCreateUserException.class)
    public ResponseEntity<ErrorResponseDto> handleKeycloakCreateUserException(
            KeycloakCreateUserException ex,
            HttpServletRequest request
    ) {
        ErrorResponseDto responseDto = new ErrorResponseDto(
                ex.getMessage(),
                HttpStatus.CONFLICT,
                request.getRequestURI()
        );

        return new ResponseEntity<>(responseDto, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(KeycloakBadRequestException.class)
    public ResponseEntity<ErrorResponseDto> handleKeycloakBadRequestException(
            KeycloakBadRequestException ex,
            HttpServletRequest request
    ) {
        ErrorResponseDto responseDto = new ErrorResponseDto(
                ex.getMessage(),
                HttpStatus.UNAUTHORIZED,
                request.getRequestURI()
        );

        return new ResponseEntity<>(responseDto, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(KeycloakUnavailableException.class)
    public ResponseEntity<ErrorResponseDto> handleKeycloakUnavailableException(
            KeycloakUnavailableException ex,
            HttpServletRequest request
    ) {
        ErrorResponseDto responseDto = new ErrorResponseDto(
                ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                request.getRequestURI()
        );

        return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(KeycloakTokenException.class)
    public ResponseEntity<ErrorResponseDto> handleKeycloakTokenException(
            KeycloakTokenException ex,
            HttpServletRequest request
    ) {
        ErrorResponseDto responseDto = new ErrorResponseDto(
                ex.getMessage(),
                HttpStatus.FORBIDDEN,
                request.getRequestURI()
        );

        return new ResponseEntity<>(responseDto, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(UserCreatingException.class)
    public ResponseEntity<ErrorResponseDto> handleUserCreatingException(
            UserCreatingException ex,
            HttpServletRequest request
    ) {
        ErrorResponseDto responseDto = new ErrorResponseDto(
                ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                request.getRequestURI()
        );

        return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .findFirst()
                .orElse("Invalid request");

        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                message,
                HttpStatus.BAD_REQUEST,
                request.getRequestURI()
        );
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(errorResponseDto);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponseDto> handleConstraintViolationException(
            ConstraintViolationException ex,
            HttpServletRequest request
    ) {

        String message = ex.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .findFirst()
                .orElse("Invalid request");


        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                message,
                HttpStatus.BAD_REQUEST,
                request.getRequestURI()
        );
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(errorResponseDto);
    }

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ErrorResponseDto> handleException(
//            HttpServletRequest request
//    ) {
//        ErrorResponseDto responseDto = new ErrorResponseDto(
//                "An unexpected error occurred",
//                HttpStatus.INTERNAL_SERVER_ERROR,
//                request.getRequestURI()
//        );
//
//        return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
//    }

}

