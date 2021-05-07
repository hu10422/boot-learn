package com.example.repository;

import com.example.model.Toutiao;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TouTiaoRepository extends MongoRepository<Toutiao, String> {


}
