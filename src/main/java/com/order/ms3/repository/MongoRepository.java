package com.order.ms3.repository;


import com.order.ms3.entity.CacheEntity;
import org.springframework.data.repository.CrudRepository;

public interface MongoRepository extends CrudRepository<CacheEntity, String> {
    CacheEntity findByDocument(String document);
}
