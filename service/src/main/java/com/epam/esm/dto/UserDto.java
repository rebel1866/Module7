package com.epam.esm.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDto extends RepresentationModel<UserDto> {
    private Integer userId;
    @NotBlank(message = "rCode23")
    @Size(min = 3, message = "rCode23")
    private String userName;
    @NotBlank(message = "rCode24")
    @Size(min = 3, message = "rCode24")
    private String userSurname;
    @NotBlank(message = "rCode25")
    @Size(min = 5, message = "rCode25")
    private String login;
    @Pattern(regexp = "(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,}", message = "rCode26")
    private String password;
    @Pattern(regexp = "\\d{4,}", message = "rCode19")
    private String phoneNumber;
    @Pattern(regexp = "[\\w\\d]+@\\w+\\.\\w+", message = "rCode20")
    private String email;

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }
}
