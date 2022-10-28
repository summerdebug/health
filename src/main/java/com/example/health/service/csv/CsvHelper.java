package com.example.health.service.csv;

import com.example.health.model.HealthInfo;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;


public class CsvHelper {

  private static final String TYPE = "text/csv";

  public static boolean hasCsvFormat(MultipartFile file) {
    return TYPE.equals(file.getContentType());
  }

  public static List<HealthInfo> csvToHealthInfo(InputStream is) {
    try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(
        is, StandardCharsets.UTF_8));
        CSVParser csvParser = new CSVParser(fileReader,
            CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {
      List<HealthInfo> healthInfoList = new ArrayList<>();
      Iterable<CSVRecord> csvRecords = csvParser.getRecords();
      for (CSVRecord csvRecord : csvRecords) {
        HealthInfo healthInfo = getHealthInfo(csvRecord);
        healthInfoList.add(healthInfo);
      }
      return healthInfoList;
    } catch (IOException e) {
      throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
    }
  }

  private static HealthInfo getHealthInfo(CSVRecord csvRecord) {
    return HealthInfo.builder()
        .source(csvRecord.get("source"))
        .codeListCode(csvRecord.get("codeListCode"))
        .code(csvRecord.get("code"))
        .displayValue(csvRecord.get("displayValue"))
        .longDescription(csvRecord.get("longDescription"))
        .fromDate(LocalDate.parse(csvRecord.get("fromDate")))
        .toDate(LocalDate.parse(csvRecord.get("toDate")))
        .sortingPriority(Integer.parseInt(csvRecord.get("sortingPriority")))
        .build();
  }

}
