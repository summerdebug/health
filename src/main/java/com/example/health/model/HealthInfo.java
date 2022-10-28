package com.example.health.model;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Data;

@Entity
@Table(name = "health_info")
@Data
@Builder
public class HealthInfo {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private long id;

  @Column(name = "source")
  private String source;

  @Column(name = "code_list_code")
  private String codeListCode;

  @Column(name = "code")
  private String code;

  @Column(name = "display_value")
  private String displayValue;

  @Column(name = "long_description")
  private String longDescription;

  @Column(name = "from_date")
  private LocalDate fromDate;

  @Column(name = "to_date")
  private LocalDate toDate;

  @Column(name = "sorting_priority")
  private Integer sortingPriority;

}
