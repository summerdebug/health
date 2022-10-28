package com.example.health.service.csv;

import com.example.health.exception.HealthException;
import com.example.health.model.HealthInfo;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.QuoteMode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;


public class CsvHelper {

  private static final String TYPE = "text/csv";
  private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyy");
  private static final String[] headerArr = {"source", "codeListCode", "code", "displayValue",
      "longDescription", "fromDate", "toDate", "sortingPriority"};
  private static final List<String> headers = Arrays.asList(headerArr);

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
      throw new HealthException("Cannot parse CSV.", e);
    }
  }

  public static ByteArrayInputStream healthInfoToCsv(List<HealthInfo> records) {
    CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.ALL);
    try (ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format)) {
      csvPrinter.printRecord(headers);
      for (HealthInfo record : records) {
        List<String> data = getCsvFields(record);
        csvPrinter.printRecord(data);
      }
      csvPrinter.flush();
      return new ByteArrayInputStream(out.toByteArray());
    } catch (IOException e) {
      throw new HealthException("Failed to export data to CSV file.", e);
    }
  }

  private static List<String> getCsvFields(HealthInfo record) {
    return Arrays.asList(
        record.getSource(),
        record.getCodeListCode(),
        record.getCode(),
        record.getDisplayValue(),
        record.getLongDescription(),
        getDateAsStr(record.getFromDate()),
        getDateAsStr(record.getToDate()),
        getIntAsStr(record.getSortingPriority())
    );
  }

  private static String getDateAsStr(LocalDate date) {
    return date == null ? "" : date.format(formatter);
  }

  private static String getIntAsStr(Integer value) {
    return value == null ? "" : value.toString();
  }

  private static HealthInfo getHealthInfo(CSVRecord csvRecord) {
    return HealthInfo.builder()
        .source(csvRecord.get("source"))
        .codeListCode(csvRecord.get("codeListCode"))
        .code(csvRecord.get("code"))
        .displayValue(csvRecord.get("displayValue"))
        .longDescription(csvRecord.get("longDescription"))
        .fromDate(getDate(csvRecord, "fromDate"))
        .toDate(getDate(csvRecord, "toDate"))
        .sortingPriority(getInt(csvRecord))
        .build();
  }

  private static Integer getInt(CSVRecord csvRecord) {
    String str = csvRecord.get("sortingPriority");
    if (StringUtils.isBlank(str)) {
      return null;
    }
    return Integer.parseInt(str);
  }

  private static LocalDate getDate(CSVRecord csvRecord, String name) {
    String str = csvRecord.get(name);
    if (StringUtils.isBlank(str)) {
      return null;
    }
    return LocalDate.parse(str, formatter);
  }

}
