package com.example.repository;

import com.example.model.ToutiaoContent;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TouTiaoContentRepository extends MongoRepository<ToutiaoContent, String> {
}
