package com.example.ConfigManagementSystem.service;

import com.example.ConfigManagementSystem.model.ServiceConfig;
import com.example.ConfigManagementSystem.repo.ConfigRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConfigServiceImpl {
    private final ConfigRepo configRepo;
    public void uploadConfig(ServiceConfig config) {
        configRepo.saveConfig(config);
    }

    public List<ServiceConfig> getAllConfigs() {
        return configRepo.getAllConfigs();
    }

    public List<ServiceConfig> getConfigsHistory(String serviceName, String environment) {
        return configRepo.getConfigsHistory(serviceName, environment);
    }

    public void activateConfig(String configId) {
        configRepo.activateConfig(configId);
    }

    public ServiceConfig fetchConfigById(String configId) {
        return configRepo.fetchConfigById(configId);
    }

    public void deleteConfig(String configId) {
        configRepo.deleteConfig(configId);
    }
}
