package com.example.health.service.csv;

import com.example.health.model.HealthInfo;
import com.example.health.repository.HealthInfoRepository;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CsvService {

  @Autowired
  private HealthInfoRepository healthInfoRepository;

  public void save(MultipartFile file) {
    try {
      List<HealthInfo> healthInfoList = CsvHelper.csvToHealthInfo(file.getInputStream());
      healthInfoRepository.saveAll(healthInfoList);
    } catch (IOException e) {
      throw new RuntimeException("fail to store csv data: " + e.getMessage());
    }
  }

  public List<HealthInfo> getAll() {
    return healthInfoRepository.findAll();
  }
}