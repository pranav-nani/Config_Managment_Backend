package com.example.ConfigManagementSystem.controller;

import com.example.ConfigManagementSystem.model.ServiceConfig;
import com.example.ConfigManagementSystem.service.ConfigServiceImpl;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/configs")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class ConfigurationController {
    private final ConfigServiceImpl configService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final YAMLMapper yamlMapper = new YAMLMapper();

    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<String> uploadConfig(@RequestParam("file") MultipartFile file,
                                               @RequestParam String serviceName,
                                               @RequestParam String environment,
                                               @RequestParam String createdBy) {
        try {

            String filename = file.getOriginalFilename();
            JsonNode dataNode;
            //Parsing Yaml files
            if (filename != null && (filename.endsWith(".yml") || filename.endsWith(".yaml"))) {
                dataNode = yamlMapper.readTree(file.getInputStream());
            } else {
                //Parsing Json files
                dataNode = objectMapper.readTree(file.getInputStream());
            }
            Map<String, Object> mapData = objectMapper.convertValue(dataNode, Map.class);
            ServiceConfig config = new ServiceConfig();
            config.setConfigData(mapData);
            config.setServiceName(serviceName);
            config.setEnvironment(environment);
            config.setCreatedBy(createdBy);
            config.setCreatedAt(new Date());
            config.setDescription("Updated configuration for " + serviceName + " in " + environment);
            configService.uploadConfig(config);
            return ResponseEntity.ok("Configuration file uploaded successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<ServiceConfig>> getConfigs() {
        List<ServiceConfig> configs = configService.getAllConfigs();
        return ResponseEntity.ok(configs);
    }

    @GetMapping("{serviceName}/{environment}/history")
    public ResponseEntity<List<ServiceConfig>> getConfigHistory(@PathVariable String serviceName, @PathVariable String environment) {
        List<ServiceConfig> configs = configService.getConfigsHistory(serviceName, environment);
        return ResponseEntity.ok(configs);
    }
    @GetMapping("{id}")
    public ResponseEntity<ServiceConfig> getConfigById(@PathVariable String id) {
        ServiceConfig config = configService.fetchConfigById(id);
        return ResponseEntity.ok(config);
    }

    @PutMapping("{configId}/activate")
    public ResponseEntity<String> activateConfig(@PathVariable String configId) {
        try {
            configService.activateConfig(configId);
            return ResponseEntity.ok("Configuration activated successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}