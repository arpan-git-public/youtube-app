package com.stream.app.youtubeapp.repository;

import com.stream.app.youtubeapp.model.Video;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VideoRepository extends MongoRepository<Video,String> {
}
