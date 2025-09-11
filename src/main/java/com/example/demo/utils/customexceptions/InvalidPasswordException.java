package com.example.demo.utils.customexceptions;

public class InvalidPasswordException extends Exception {

  public InvalidPasswordException(String message) {
    super(message);
  }
}
