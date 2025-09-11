package com.example.demo.utils.customexceptions;

public class RequiredResourceNotFoundException extends RuntimeException {
  public RequiredResourceNotFoundException(String message) {
    super(message);
  }
}
