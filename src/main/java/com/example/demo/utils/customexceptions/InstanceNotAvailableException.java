package com.example.demo.utils.customexceptions;

public class InstanceNotAvailableException extends RuntimeException {
  public InstanceNotAvailableException(String message) {
    super(message);
  }
}
