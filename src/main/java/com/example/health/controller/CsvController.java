package com.example.health.controller;

import com.example.health.message.ResponseMessage;
import com.example.health.service.csv.CsvHelper;
import com.example.health.service.csv.CsvService;
import java.io.ByteArrayInputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin
@Controller
@RequestMapping("/api/csv")
public class CsvController {

  private static final String FILE_NAME = "health-records.csv";

  @Autowired
  private CsvService csvService;

  @PostMapping("/upload")
  public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
    if (CsvHelper.hasCsvFormat(file)) {
      csvService.save(file);
      return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(
          "Uploaded the file successfully: " + file.getOriginalFilename()));
    }
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(
        "Not a CSV file"));
  }

  @GetMapping("/download/all")
  public ResponseEntity<Resource> getAllAsCsv() {
    ByteArrayInputStream stream = csvService.getAll();
    return getCsvFile(stream);
  }

  @GetMapping("/download/code/{code}")
  public ResponseEntity<Resource> get(@PathVariable String code) {
    ByteArrayInputStream stream = csvService.get(code);
    return getCsvFile(stream);
  }

  private ResponseEntity<Resource> getCsvFile(ByteArrayInputStream stream) {
    InputStreamResource file = new InputStreamResource(stream);
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + FILE_NAME)
        .contentType(MediaType.parseMediaType("application/csv"))
        .body(file);
  }

}