import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UploadVideoComponentComponent } from './upload-video-component.component';

describe('UploadVideoComponentComponent', () => {
  let component: UploadVideoComponentComponent;
  let fixture: ComponentFixture<UploadVideoComponentComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [UploadVideoComponentComponent]
    });
    fixture = TestBed.createComponent(UploadVideoComponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
