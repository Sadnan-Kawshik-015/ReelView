package com.example.demo.utils.customexceptions;

public class RefreshTokenDoesNotExistException extends RuntimeException {
  public RefreshTokenDoesNotExistException(String message) {
    super(message);
  }
}
