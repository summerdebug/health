package com.example.health.repository;

import com.example.health.model.HealthInfo;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HealthInfoRepository extends JpaRepository<HealthInfo, Long> {

  Optional<HealthInfo> findByCode(String code);
}
