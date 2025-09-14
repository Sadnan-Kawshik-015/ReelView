package com.example.demo.service.exceptionhandler;

import com.example.demo.dtos.output.FieldValidationErrorDTO;
import com.example.demo.dtos.output.ResponseModelDTO;
import com.example.demo.utils.customexceptions.TokenDecryptionException;
import com.example.demo.utils.enums.Status;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.List;

import static com.example.demo.utils.constants.CommonMessage.UNHANDLED_EXCEPTION;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final HttpServletRequest request;
    @Value("${is-debug-mode}")
    private boolean isDebugMode;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<ResponseModelDTO> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e) {

        BindingResult result = e.getBindingResult();
        List<FieldValidationErrorDTO> fieldValidationErrorDTOS = new ArrayList<>();
        result.getFieldErrors()
                .forEach((fieldError) -> fieldValidationErrorDTOS.add(FieldValidationErrorDTO.builder()
                        .field_name(
                                fieldError.getField())
                        .error_message(
                                fieldError.getDefaultMessage())
                        .rejected_value(
                                fieldError.getRejectedValue() != null ? fieldError.getRejectedValue()
                                        .toString() : "")
                        .build()));
        return ResponseEntity.badRequest()
                .body(ResponseModelDTO.builder()
                        .status(Status.ERROR.getValue())
                        .message("field validation failed!")
                        .correlation_id(null)
                        .data(fieldValidationErrorDTOS)
                        .build());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> securityException(Exception e) throws Exception {
        return ResponseEntity.badRequest()
                .body(ResponseModelDTO.builder()
                        .status(Status.ERROR.getValue())
                        .message("Access Denied!")
                        .correlation_id(null)
                        .build());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ResponseModelDTO> httpMessageNotReadableException(HttpMessageNotReadableException e) {

        String message = "field validation failed!";
        FieldValidationErrorDTO validationErrorDTO = null;
        String errorMessage = e.getMessage();
        if (errorMessage != null && errorMessage.contains("java.time.LocalDate")) {
            validationErrorDTO = new FieldValidationErrorDTO("date", "Date must be in format YYYY-MM-DD", null);
        } else if (errorMessage != null && errorMessage.contains("java.time.LocalTime")) {
            validationErrorDTO = new FieldValidationErrorDTO("time", "Time must be in correct format!", null);
        } else if (errorMessage != null && errorMessage.contains("UnrecognizedPropertyException")) {
            validationErrorDTO = handleUnrecognizedPropertyException(errorMessage);
        } else if (errorMessage != null && errorMessage.contains("Unexpected character")) {
            validationErrorDTO = new FieldValidationErrorDTO("json", "Invalid format or unexpected character in the request body.", null);
        } else if (errorMessage != null &&
                (errorMessage.contains("JSON parse error") || errorMessage.contains("Cannot deserialize value of type"))) {
            validationErrorDTO = handleJsonParseError(e);
        } else {
            validationErrorDTO = new FieldValidationErrorDTO("json", "Invalid request format", null);
        }

        return ResponseEntity.badRequest()
                .body(
                        ResponseModelDTO.builder()
                                .status(Status.ERROR.getValue())
                                .message(message)
                                .data(validationErrorDTO)
                                .correlation_id(getCorrelationId())
                                .build()
                );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> unhandledException(Exception e) throws TokenDecryptionException {

        return ResponseEntity.badRequest()
                .body(ResponseModelDTO.builder()
                        .status(Status.ERROR.getValue())
                        .message(
                                isDebugMode ? e.getMessage() : UNHANDLED_EXCEPTION)
                        .correlation_id(getCorrelationId())
                        .build());
    }
    public String getCorrelationId() {
        return this.request.getHeader("x-correlation-id");
    }

    private FieldValidationErrorDTO handleJsonParseError(HttpMessageNotReadableException e) {
        Throwable cause = e.getCause();

        if (cause instanceof JsonMappingException ex) {

            // Type mismatch (e.g., "ff" for Integer)
            if (ex instanceof InvalidFormatException invalidEx) {
                String fieldName = !invalidEx.getPath().isEmpty() ? invalidEx.getPath().get(0).getFieldName() : "unknown";
                String rejectedValue = invalidEx.getValue() != null ? invalidEx.getValue().toString() : "null";
                String expectedType = invalidEx.getTargetType() != null ? invalidEx.getTargetType().getSimpleName() : "UnknownType";
                String errorMessage = "Invalid value for the field: " + fieldName + ". Expected an " + expectedType + " value.";
                return new FieldValidationErrorDTO(fieldName, errorMessage, rejectedValue);
            }

            // Extra/unexpected field
            if (ex instanceof UnrecognizedPropertyException unrecognizedEx) {
                String fieldName = unrecognizedEx.getPropertyName();
                String errorMessage = "Unrecognized field: " + fieldName + ". This field is not allowed.";
                return new FieldValidationErrorDTO(fieldName, errorMessage, null);
            }

            // any other unmapped JsonMappingException
            String fieldName = !ex.getPath().isEmpty() ? ex.getPath().get(0).getFieldName() : "unknown";
            String errorMessage = "Invalid JSON format or mapping issue at field: " + fieldName;
            return new FieldValidationErrorDTO(fieldName, errorMessage, null);
        }

        // default
        return new FieldValidationErrorDTO("json", "Unreadable or invalid JSON input", null);
    }

    private FieldValidationErrorDTO handleUnrecognizedPropertyException(String errorMsg) {
        String message = "Invalid field format";
        String fieldName = "unknown";

        try {
            int startIndex = errorMsg.indexOf("[\"");

            if (startIndex != -1) {
                int fieldNameStart = startIndex + 2; // "[\"".length() = 2
                int fieldNameEnd = errorMsg.indexOf("\"])", fieldNameStart);

                if (fieldNameEnd != -1) {
                    fieldName = errorMsg.substring(fieldNameStart, fieldNameEnd);
                    message = "Unrecognized field: " + fieldName + ". This field is not allowed.";
                }
            }
        } catch (Exception ignored) {}

        return new FieldValidationErrorDTO(fieldName, message, null);
    }
}
