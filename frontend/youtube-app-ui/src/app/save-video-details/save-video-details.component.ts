import { LiveAnnouncer } from '@angular/cdk/a11y';
import { Component, inject, signal } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { MatChipEditedEvent, MatChipInputEvent } from '@angular/material/chips';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { COMMA, ENTER } from '@angular/cdk/keycodes';
import { AcceptedPlugin } from 'postcss';
import { ActivatedRoute } from '@angular/router';
import { VideoService } from '../video.service';
import {MatSnackBar} from '@angular/material/snack-bar';
import { VideoDto } from '../VideoDto';

@Component({
  selector: 'app-save-video-details',
  templateUrl: './save-video-details.component.html',
  styleUrls: ['./save-video-details.component.css']
})
export class SaveVideoDetailsComponent {


  savedVideoDetailForm: FormGroup;
  title: FormControl = new FormControl('');
  description: FormControl = new FormControl('');
  videoStatus: FormControl = new FormControl('');
  selectedFile!: File;
  selectedFileName = '';
  videoId = '';
  videoUrl !: string;
  thumbnailUrl !: string;

  constructor(private activatedRoute:ActivatedRoute, private videoService : VideoService,
      private matSnackBar: MatSnackBar
  ){
    this.videoId = this.activatedRoute.snapshot.params['videoId'];
    this.videoService.getVideoId(this.videoId).subscribe(data => {
      this.videoUrl = data.videoUrl;
      this.thumbnailUrl = data.thumbnailUrl;
    });
    this.savedVideoDetailForm = new FormGroup({
        title:this.title,
        description : this.description,
        videoStatus : this.videoStatus,
    });
  }
  visible = true;
  selectable = true;
  removable = true;
  addOnBlur = true;
  readonly separatorKeysCodes = [ENTER, COMMA] as const;
  tags: string[] = [];
  fileSelected = false;

  add(event: MatChipInputEvent): void {
    const value = (event.value || '').trim();

    // Add our fruit
    if (value) {
      this.tags.push(value);
    }

    // Clear the input value
    event.chipInput!.clear();
  }

  remove(chip: string): void {
    const index = this.tags.indexOf(chip);
    if(index >=0 ) {
      this.tags.splice(index,1);
    }
  }

  edit(tag: string, event: MatChipEditedEvent) {
    const value = event.value.trim();

    // Remove fruit if it no longer has a name
    if (!value) {
      this.remove(tag);
      return;
    }

    // Edit existing fruit
      const index = this.tags.indexOf(tag);
      if (index >= 0) {
        this.tags[index] = value;
        return [...this.tags];
      }
      return this.tags;
  }

  onFileSelected(event : Event) {
    //@ts-ignore
      this.selectedFile = event.target.files[0];
      this.selectedFileName = this.selectedFile.name;
      this.fileSelected = true;
  }

  onUpload(){
      this.videoService.uploadThumbnail(this.selectedFile, this.videoId)
        .subscribe(data => {
          console.log(data);
          //show upload success notification.
          this.matSnackBar.open("Upload Thumbnail Successful", "OK");
        })
  }

  saveVideo() {
    // Make a call to video service.
      const videoMetadata: VideoDto = {
        "id" : this.videoId,
        "title" : this.savedVideoDetailForm.get('title')?.value,
        "description": this.savedVideoDetailForm.get('description')?.value,
        "tags": this.tags,
        "videoStatus": this.savedVideoDetailForm.get('videoStatus')?.value,
        "videoUrl": this.videoUrl,
        "thumbnailUrl": this.thumbnailUrl,
        "likeCount": 0,
        "dislikeCount": 0,
        "viewCount" : 0
      }
      this.videoService.saveVideo(videoMetadata).subscribe(data => {
        this.matSnackBar.open('Video Metadata is updated successfully',"OK");
      });
  }
}
