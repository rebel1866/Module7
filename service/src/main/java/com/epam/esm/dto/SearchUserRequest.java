package com.epam.esm.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Pattern;

@Getter
@Setter
@Builder
public class SearchUserRequest {
    private String userName;
    private String userSurname;
    private String login;
    @Pattern(regexp = "\\d{4,}", message = "rCode19")
    private String phoneNumber;
    @Pattern(regexp = "[\\w\\d]+@\\w+\\.\\w+", message = "rCode20")
    private String email;
    @Pattern(regexp = "^[1-9]+[0-9]*$", message = "rCode21")
    private String page;
    @Pattern(regexp = "^[1-9]+[0-9]*$", message = "rCode22")
    private String pageSize;
    @Pattern(regexp = "userName|userSurname", message = "rCode4")
    private String sorting;
    @Pattern(regexp = "asc|desc", message = "rCode5")
    private String sortingOrder;
}
