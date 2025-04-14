package com.stream.app.youtubeapp.service;

import com.stream.app.youtubeapp.dto.CommentDto;
import com.stream.app.youtubeapp.dto.UploadVideoResponse;
import com.stream.app.youtubeapp.dto.VideoDto;
import com.stream.app.youtubeapp.model.Comment;
import com.stream.app.youtubeapp.model.Video;
import com.stream.app.youtubeapp.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VideoService {

    private final S3Service s3Service;
    private final UserService userService;
    private final VideoRepository videoRepository;

    public UploadVideoResponse uploadVideo(MultipartFile multipartFile) {
        //upload file to s3
        String videoUrl = s3Service.uploadFile(multipartFile);
        //save metadata to db
        var video = new Video();
        video.setVideoUrl(videoUrl);
        var saveVideo = videoRepository.save(video);
        return new UploadVideoResponse(saveVideo.getId(),saveVideo.getVideoUrl());
    }

    public VideoDto editVideo(VideoDto videoDto) {
        //Find the video by videoId
        var savedVideo = getVideoById(videoDto.getId());

        //Map the VideoDto fields to video
        savedVideo.setTitle(videoDto.getTitle());
        savedVideo.setDescription(videoDto.getDescription());
        savedVideo.setTags(videoDto.getTags());
        savedVideo.setThumbnailUrl(videoDto.getThumbnailUrl());
        savedVideo.setVideoStatus(videoDto.getVideoStatus());

        //save the video to the database
        videoRepository.save(savedVideo);
        return videoDto;
    }

    public String uploadThumbnail(MultipartFile file, String videoId) {
        //Find the video by videoId
        var savedVideo = getVideoById(videoId);

        //upload file to s3
        String thumbnailUrl = s3Service.uploadFile(file);
        //save metadata to db
        //var video = new Video();
        savedVideo.setThumbnailUrl(thumbnailUrl);
        videoRepository.save(savedVideo);

        return thumbnailUrl;
    }

    Video getVideoById(String videoId) {
        return videoRepository.findById(videoId).orElseThrow(() -> new IllegalArgumentException("cannot find video by id : " + videoId));
    }

    public VideoDto getVideoDetails(String videoId) {
        Video savedVideo = getVideoById(videoId);
        increaseVideoCount(savedVideo);
        userService.addVideoToHistory(videoId);

       return  getVideoDto(savedVideo);
    }

    private void increaseVideoCount(Video savedVideo) {
        savedVideo.incrementViewCount();
        videoRepository.save(savedVideo);
    }

    public VideoDto likeVideo(String videoId) {
        //Get video by Id
        var video = getVideoById(videoId);

        //If user has already liked Video, then decrement the like count.
        if(userService.ifLikedVideo(videoId)) {
            video.decrementLikes();
            userService.removeFromLikeVideos(videoId);
        }  //If user already disliked the video, then increment like count and decrement dislike count.
        else if(userService.ifdislikedVideo(videoId)) {
            video.decrementDisLikes();
            userService.removeFromDisLikeVideos(videoId);
            video.incrementLikes();
            userService.addToLikedVideos(videoId);
        } else {
            //Increment like count
            video.incrementLikes();
            userService.addToLikedVideos(videoId);
        }
        videoRepository.save(video);
        return getVideoDto(video);
    }

    private VideoDto getVideoDto(Video video) {
        var videoDto = new VideoDto();
        videoDto.setVideoUrl(video.getVideoUrl());
        videoDto.setThumbnailUrl(video.getThumbnailUrl());
        videoDto.setId(video.getId());
        videoDto.setTitle(video.getTitle());
        videoDto.setTags(video.getTags());
        videoDto.setDescription(video.getDescription());
        videoDto.setLikeCount(video.getLikes().get());
        videoDto.setDislikeCount(video.getDislikes().get());
        videoDto.setViewCount(video.getViewCount().get());
        return videoDto;
    }

    public VideoDto dislikeVideo(String videoId) {

        //Get video by Id
        var video = getVideoById(videoId);

        //If user has already dislike Video, then decrement the dislike count.
        if(userService.ifdislikedVideo(videoId)) {
            video.decrementDisLikes();
            userService.removeFromDisLikeVideos(videoId);
        }  //If user already liked the video, then increment dislike count and decrement like count.
        else if(userService.ifLikedVideo(videoId)) {
            video.decrementLikes();
            userService.removeFromLikeVideos(videoId);
            video.incrementDisLikes();
            userService.addToDisLikedVideos(videoId);
        } else {
            //Increment dislike count
            video.incrementDisLikes();
            userService.addToDisLikedVideos(videoId);
        }
        videoRepository.save(video);
        var videoDto = new VideoDto();
        videoDto.setVideoUrl(video.getVideoUrl());
        videoDto.setThumbnailUrl(video.getThumbnailUrl());
        videoDto.setId(video.getId());
        videoDto.setTitle(video.getTitle());
        videoDto.setTags(video.getTags());
        videoDto.setDescription(video.getDescription());
        videoDto.setLikeCount(video.getLikes().get());
        videoDto.setDislikeCount(video.getDislikes().get());


        return videoDto;
    }

    public void addComment(String videoId, CommentDto commentDto) {
        var video = getVideoById(videoId);
        Comment comment = new Comment();
        comment.setText(commentDto.getCommentText());
        comment.setAuthorId(comment.getAuthorId());
        video.addComment(comment);
        videoRepository.save(video);
    }

    public List<CommentDto> getAllComments(String videoId) {
        var video = getVideoById(videoId);
        List<Comment> commentList = video.getCommentList();
        return commentList.stream().map(this::mapToCommentDto).toList();
    }

    private CommentDto mapToCommentDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setCommentText(comment.getText());
        commentDto.setAuthorId(comment.getAuthorId());
        return commentDto;
    }

    public List<VideoDto> getAllVideos() {
       return videoRepository.findAll().stream().map(this::getVideoDto).toList();
    }
}
