package com.example.health.exception;

public class HealthRecordNotFoundException extends HealthException {

  public HealthRecordNotFoundException() {
    super("Code does not exist.");
  }

}
