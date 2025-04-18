package com.stream.app.youtubeapp.controller;

import com.stream.app.youtubeapp.dto.CommentDto;
import com.stream.app.youtubeapp.dto.UploadVideoResponse;
import com.stream.app.youtubeapp.dto.VideoDto;
import com.stream.app.youtubeapp.model.Comment;
import com.stream.app.youtubeapp.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/videos")
@RequiredArgsConstructor
public class VideoController {

    private final VideoService videoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UploadVideoResponse uploadVideo(@RequestParam("file") MultipartFile file) {
        return videoService.uploadVideo(file);
    }

    @PostMapping("/thumbnail")
    @ResponseStatus(HttpStatus.CREATED)
    public String uploadThumbnail(@RequestParam("file") MultipartFile file, @RequestParam("videoId") String videoId) {
        return videoService.uploadThumbnail(file, videoId);
    }


    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public VideoDto editVideoMetadata(@RequestBody VideoDto videoDto) {
        return videoService.editVideo(videoDto);
    }

    @GetMapping("/{videoId}")
    @ResponseStatus(HttpStatus.OK)
    public VideoDto getVideoDetails(@PathVariable String videoId) {
        return  videoService.getVideoDetails(videoId);
    }


    @PostMapping("{videoId}/like")
    @ResponseStatus(HttpStatus.OK)
    public VideoDto likeVide(@PathVariable String videoId) {
        return videoService.likeVideo(videoId);
    }

    @PostMapping("{videoId}/dislike")
    @ResponseStatus(HttpStatus.OK)
    public VideoDto dislikeVide(@PathVariable String videoId) {
        return videoService.dislikeVideo(videoId);
    }


    @PostMapping("{videoId}/comment")
    @ResponseStatus(HttpStatus.OK)
    public void addComment(@PathVariable String videoId, @RequestBody CommentDto commentDto) {
        videoService.addComment(videoId,commentDto);
    }

    @GetMapping("{videoId}/comment")
    @ResponseStatus(HttpStatus.OK)
    public List<CommentDto> getAllComments(String videoId) {
        return videoService.getAllComments(videoId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<VideoDto> getAllVideos() {
        return videoService.getAllVideos();
    }
}
