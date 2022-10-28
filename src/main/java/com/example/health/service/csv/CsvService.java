package com.example.health.service.csv;

import com.example.health.exception.HealthException;
import com.example.health.exception.HealthRecordNotFoundException;
import com.example.health.model.HealthInfo;
import com.example.health.repository.HealthInfoRepository;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class CsvService {

  @Autowired
  private HealthInfoRepository healthInfoRepository;

  public void save(MultipartFile file) {
    try {
      List<HealthInfo> healthInfoList = CsvHelper.csvToHealthInfo(file.getInputStream());
      healthInfoRepository.saveAll(healthInfoList);
      log.info("Saved " + healthInfoList.size() + " HealthInfo records.");
    } catch (IOException e) {
      throw new HealthException("fail to store csv data", e);
    }
  }

  public ByteArrayInputStream getAll() {
    List<HealthInfo> records = healthInfoRepository.findAll();
    return CsvHelper.healthInfoToCsv(records);
  }

  public ByteArrayInputStream get(String code) {
    List<HealthInfo> records = healthInfoRepository.findByCode(code)
        .map(Collections::singletonList).orElseThrow(HealthRecordNotFoundException::new);
    return CsvHelper.healthInfoToCsv(records);
  }
}