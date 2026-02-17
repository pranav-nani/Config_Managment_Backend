package com.example.ConfigManagementSystem.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("service_configs")
public class ServiceConfig {
    @Id
    String id;
    String serviceName;
    String environment;
    int version;
    Date createdAt;
    String createdBy;
    String description;
    int activeVersion;
    @JsonProperty("isActive")
    boolean isActive;
    Map<String, Object> configData; // Store the actual configuration as a JSON object
}
