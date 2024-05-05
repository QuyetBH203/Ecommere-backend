package com.project.shopApp.dtos;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserDTO {
    @JsonProperty("fullname")
    private String fullName;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("address")
    private String address;

    @JsonProperty("password")
    private String password;

    @JsonProperty("retype_password")
    private String retypePassword;

    @JsonProperty("google_account_id")
    private int googleAccountId;

    @JsonProperty("facebook_account_id")
    private int facebookAccountId;

    @JsonProperty("date_of_birth")
    private Date dateOfBirth;
}
