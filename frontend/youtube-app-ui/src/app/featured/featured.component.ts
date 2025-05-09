import { Component, OnInit } from '@angular/core';
import { VideoService } from '../video.service';
import { VideoDto } from '../VideoDto';

@Component({
  selector: 'app-featured',
  templateUrl: './featured.component.html',
  styleUrls: ['./featured.component.css']
})
export class FeaturedComponent implements OnInit{

  featureVideos: Array<VideoDto> = [];
  constructor(private videoService: VideoService){

  }
  ngOnInit():void {
      this.videoService.getAllVideos().subscribe(response => {
        this.featureVideos = response;
      })
  }
}
