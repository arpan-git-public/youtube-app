package com.stream.app.youtubeapp.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.stream.app.youtubeapp.dto.UserDto;
import com.stream.app.youtubeapp.model.User;
import com.stream.app.youtubeapp.model.Video;
import com.stream.app.youtubeapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mapping.model.PropertyNameFieldNamingStrategy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Value("${auth0.userinfo.endpoint}")
    private String userInfoEndpoint;

    public void register(String token) {
        //make user info endpoint
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(userInfoEndpoint))
                .header("Authorization", String.format("Bearer %s",token))
                .build();

        HttpClient httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            String body = response.body();

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
            UserDto userDto = objectMapper.readValue(body,UserDto.class);

            User user = new User();
            user.setFirstName(userDto.getGivenName());
            user.setLastName(userDto.getFamilyName());
            user.setFullName(userDto.getName());
            user.setEmailAddress(userDto.getEmail());
            user.setSub(userDto.getSub());

            this.userRepository.save(user);

        } catch (Exception e) {
            throw new RuntimeException("Exception occured while calling user info endpoint.",e);
        }
        //fetch user details and persist to database
    }


    public User getCurrentUser(){
        var sub = ((Jwt)(SecurityContextHolder.getContext().getAuthentication().getPrincipal())).getClaim("sub");
        return userRepository.findBySub(sub).orElseThrow(()->new IllegalArgumentException("Can not find the user with sub - "+ sub));
    }

    public void addToLikedVideos(String videoId) {
        User currentUser = getCurrentUser();
        currentUser.addToLikeVideos(videoId);
        userRepository.save(currentUser);
    }

    public void addToDisLikedVideos(String videoId) {
        User currentUser = getCurrentUser();
        currentUser.addToDisLikeVideos(videoId);
        userRepository.save(currentUser);
    }

    public boolean ifLikedVideo(String videoId) {
        return getCurrentUser().getLikedVideos().stream().anyMatch(likeVideo -> likeVideo.equals(videoId));
    }
    public boolean ifdislikedVideo(String videoId) {
        return getCurrentUser().getDislikedVideos().stream().anyMatch(dislikeVideo -> dislikeVideo.equals(videoId));
    }

    public void removeFromLikeVideos(String videoId) {
        User currentUser = getCurrentUser();
        currentUser.removeFromLikeVideos(videoId);
        userRepository.save(currentUser);
    }

    public void removeFromDisLikeVideos(String videoId) {
        User currentUser = getCurrentUser();
        currentUser.removeFromDisLikeVideos(videoId);
        userRepository.save(currentUser);
    }

    public void addVideoToHistory(String videoId) {
        User currentUser = getCurrentUser();
        currentUser.addToVideoHistory(videoId);
        userRepository.save(currentUser);
    }

    public void subScribeUser(String userId) {
        //retrieve currentUser and add the userId to the subscribed to users set.
        //retrieve target user and add current user to subscribers list.
        User currentUser = getCurrentUser();
        currentUser.addToSubscribedToUsers(userId);
        User user = userRepository.findById(userId).orElseThrow(()-> new IllegalArgumentException("Cannot find userId. "+ userId));
        user.addSubScribers(currentUser.getId());
        userRepository.save(currentUser);
        userRepository.save(user);
    }

    public void unsubScribeUser(String userId) {
        //retrieve currentUser and remove the userId to the subscribed to users set.
        //retrieve target user and remove current user to subscribers list.
        User currentUser = getCurrentUser();
        currentUser.removeFromSubscribedToUsers(userId);
        User user = userRepository.findById(userId).orElseThrow(()-> new IllegalArgumentException("Cannot find userId. "+ userId));
        user.removeSubScribers(currentUser.getId());
        userRepository.save(currentUser);
        userRepository.save(user);
    }

    public Set<String> userHistory(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new IllegalArgumentException("Cannot find user with userId "+ userId));
        return user.getVideoHistory();
    }
}
