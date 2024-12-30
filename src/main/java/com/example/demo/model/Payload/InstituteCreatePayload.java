package com.example.demo.model.Payload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InstituteCreatePayload {

    private String instituteName;
    private String location;
    private String email;
    private String sports;
    private String area;
    private String contactNumber;
    private String coachName;
}
