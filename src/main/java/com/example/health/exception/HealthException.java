package com.example.health.exception;

public class HealthException extends RuntimeException {

  public HealthException(String msg, Throwable cause) {
    super(msg, cause);
  }

  public HealthException(String msg) {
    super(msg);
  }

}
