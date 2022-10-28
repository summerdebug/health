package com.example.health.exception;

import com.example.health.message.ResponseMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@ControllerAdvice
@Slf4j
public class HealthExceptionHandler {

  @ExceptionHandler(HealthRecordNotFoundException.class)
  public ResponseEntity<ResponseMessage> recordNotFound(HealthRecordNotFoundException e) {
    log.error("Record not found exception", e);
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(new ResponseMessage(e.getMessage()));
  }

  @ExceptionHandler(HealthException.class)
  public ResponseEntity<ResponseMessage> healthException(HealthException e) {
    log.error("Health application exception", e);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new ResponseMessage(e.getMessage()));
  }

  @ExceptionHandler(MaxUploadSizeExceededException.class)
  public ResponseEntity<ResponseMessage> handleMaxSizeException(MaxUploadSizeExceededException e) {
    return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(
        "File too large!"));
  }

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<ResponseMessage> runtimeException(RuntimeException e) {
    log.error("Health application exception", e);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        // In general case we don't reveal the details to the user for security reason.
        .body(new ResponseMessage("Something went wrong."));
  }
}