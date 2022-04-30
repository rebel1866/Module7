package com.epam.esm.converter;

import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.User;

public class UserDtoToEntityConverter {
    public static User convert(UserDto userDto){
        return User.builder().userName(userDto.getUserName()).userSurname(userDto.getUserSurname()).
                login(userDto.getLogin()).email(userDto.getEmail()).phoneNumber(userDto.getPhoneNumber())
                .build();
    }
}
