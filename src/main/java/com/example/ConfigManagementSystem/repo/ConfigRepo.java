package com.example.ConfigManagementSystem.repo;

import com.example.ConfigManagementSystem.model.ServiceConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ConfigRepo {
    private final MongoOperations mongoOperations;

    public void saveConfig(ServiceConfig config) {
        Query versionQuery = new Query();
        versionQuery.addCriteria(Criteria.where("serviceName").is(config.getServiceName())
                .and("environment").is(config.getEnvironment()));
        versionQuery.with(Sort.by(Sort.Direction.DESC, "version")).limit(1);

        ServiceConfig latestConfig = mongoOperations.findOne(versionQuery, ServiceConfig.class, "service_configs");
        int newVersion = (latestConfig == null) ? 1 : latestConfig.getVersion() + 1;

        Query deactivateQuery = new Query(Criteria.where("serviceName").is(config.getServiceName())
                .and("environment").is(config.getEnvironment()));
        Update deactivateUpdate = new Update().set("isActive", false);
        mongoOperations.updateMulti(deactivateQuery, deactivateUpdate, ServiceConfig.class, "service_configs");

        config.setVersion(newVersion);
        config.setCreatedAt(new Date());
        config.setActive(true);

        mongoOperations.save(config, "service_configs");
    }


    public List<ServiceConfig> getAllConfigs() {
        return mongoOperations.findAll(ServiceConfig.class, "service_configs");
    }

    public List<ServiceConfig> getConfigsHistory(String serviceName, String environment) {
        Query query = new Query();
        query.addCriteria(Criteria.where("serviceName").is(serviceName).and("environment").is(environment));
        query.with(Sort.by(Sort.Direction.DESC, "version"));
        return mongoOperations.find(query, ServiceConfig.class, "service_configs");
    }

    public void activateConfig(String id) {
        ServiceConfig targetConfig = mongoOperations.findById(id, ServiceConfig.class, "service_configs");
        if (targetConfig == null) throw new RuntimeException("Configuration not found");

        Query query = new Query(Criteria.where("serviceName").is(targetConfig.getServiceName())
                .and("environment").is(targetConfig.getEnvironment()));
        Update update = new Update().set("isActive", false);
        mongoOperations.updateMulti(query, update, ServiceConfig.class, "service_configs");

        Query activeQuery = new Query(Criteria.where("id").is(id));
        Update activeUpdate = new Update().set("isActive", true);
        mongoOperations.updateFirst(activeQuery, activeUpdate, ServiceConfig.class, "service_configs");
    }

    public ServiceConfig fetchConfigById(String configId) {
        return mongoOperations.findById(configId, ServiceConfig.class, "service_configs");
    }
}
