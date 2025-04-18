package com.stream.app.youtubeapp.repository;

import com.stream.app.youtubeapp.model.User;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.FluentQuery;

import java.util.Optional;
import java.util.function.Function;

public interface UserRepository extends MongoRepository<User,String> {
    Optional<User> findBySub(Object sub);
}
