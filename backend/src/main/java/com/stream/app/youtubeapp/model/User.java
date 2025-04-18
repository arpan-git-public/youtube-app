package com.stream.app.youtubeapp.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Document(value = "User")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String fullName;
    private String emailAddress;
    private String sub;
    private Set<String> subscribedToUsers = ConcurrentHashMap.newKeySet();
    private Set<String> subscribers = ConcurrentHashMap.newKeySet();
    private Set<String> videoHistory = ConcurrentHashMap.newKeySet();
    private Set<String> likedVideos = ConcurrentHashMap.newKeySet();
    private Set<String> dislikedVideos = ConcurrentHashMap.newKeySet();

    public void addToLikeVideos(String videoId) {
        likedVideos.add(videoId);
    }

    public void addToDisLikeVideos(String videoId) {
        dislikedVideos.add(videoId);
    }

    public void removeFromLikeVideos(String videoId) {
        likedVideos.remove(videoId);
    }

    public void removeFromDisLikeVideos(String videoId) {
        dislikedVideos.remove(videoId);
    }

    public void addToVideoHistory(String videoId) {
        videoHistory.add(videoId);
    }

    public void addToSubscribedToUsers(String userId) {
        subscribedToUsers.add(userId);
    }

    public void addSubScribers(String userid) {
        subscribers.add(userid);
    }

    public void removeFromSubscribedToUsers(String userId) {
        subscribedToUsers.remove(userId);
    }

    public void removeSubScribers(String userid) {
        subscribers.remove(userid);
    }
}
