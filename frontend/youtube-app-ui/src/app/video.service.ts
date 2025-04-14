import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { UploadVideoResonse } from './upload-video-component/UploadVideoResponse';
import { VideoDto } from './videoDto';

@Injectable({
  providedIn: 'root'
})
export class VideoService {


  constructor(private httpClient:HttpClient) {

   }

  uploadVideo(file: File): Observable<UploadVideoResonse> {
      //http post call

      const formData = new FormData()
      formData.append('file', file, file.name);

      return this.httpClient.post<UploadVideoResonse>("http://localhost:8080/api/videos",formData);
   }

   uploadThumbnail(file: File, videoId : string): Observable<string> {
    //http post call

    const formData = new FormData()
    formData.append('file', file);
    formData.append('videoId',videoId);

    return this.httpClient.post("http://localhost:8080/api/videos/thumbnail",formData,{
      responseType : 'text'
   });
 }

  getVideoId(videoId: string): Observable<VideoDto> {
    return  this.httpClient.get<VideoDto>("http://localhost:8080/api/videos/"+videoId)
  }

  saveVideo(videoMetadata: VideoDto):Observable<VideoDto> {
    return this.httpClient.put<VideoDto>("http://localhost:8080/api/videos",videoMetadata);
  }

  getAllVideos(): Observable<Array<VideoDto>> {
    return this.httpClient.get<Array<VideoDto>>("http://localhost:8080/api/videos");
  }

}
