package com.example.demo.utils.customexceptions;
public class FieldValidationException extends RuntimeException{
    private final String fieldName;
    private final String errorMessage;
    private final String rejectedValue;

    public FieldValidationException(String fieldName, String errorMessage, String rejectedValue) {
        super(errorMessage);
        this.fieldName = fieldName;
        this.errorMessage = errorMessage;
        this.rejectedValue = rejectedValue;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getRejectedValue() {
        return rejectedValue;
    }
}
