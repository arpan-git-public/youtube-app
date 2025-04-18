package com.stream.app.youtubeapp.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonAutoDetect
public class UserDto {

    private String id;
    private String sub;
    private String givenName;
    private String familyName;
    private String name;
    private String picture;
    private String email;

}
