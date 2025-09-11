package com.example.demo.utils.customexceptions;

public class OTPExpiredException extends Exception {
  public OTPExpiredException(String message) {
    super(message);
  }
}
