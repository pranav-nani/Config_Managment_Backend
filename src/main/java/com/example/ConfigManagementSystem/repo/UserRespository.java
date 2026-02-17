package com.example.ConfigManagementSystem.repo;

import com.example.ConfigManagementSystem.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;


@Repository
@RequiredArgsConstructor
public class UserRespository{
    private final MongoOperations mongoOperations;

    public User findByEmail(String email) {
        Query query = new Query();
        query.addCriteria(Criteria.where("email").is(email));
        return mongoOperations.findOne(query, User.class);
    }

    public User save(User user) {
        mongoOperations.save(user);
        return user;
    }
}
