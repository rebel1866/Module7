package com.epam.esm.dto;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto extends RepresentationModel<UserDto> {
    private Integer userId;
    private String userName;
    private String userSurname;
    private String login;
    private String password;
    private String phoneNumber;
    private String email;
}
