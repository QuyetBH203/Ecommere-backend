package com.project.shopApp.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.shopApp.models.User;
import lombok.*;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterResponse {
    @JsonProperty("message")
    private String message;

    @JsonProperty("user")
    private User user;

}
