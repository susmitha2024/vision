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
public class StudentCreatePayload {

    private String firstName;
    private String lastName;
    private String email;
    private String city;
    private String sportsCategory;
    private String Representing;
    private String InstituteName;
    private String CompetitionLevel;
}
