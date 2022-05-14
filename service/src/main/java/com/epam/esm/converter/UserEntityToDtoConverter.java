package com.epam.esm.converter;

import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.User;

import java.util.List;
import java.util.stream.Collectors;
/**
 * This class is used for converting entity objects to dto
 * @author Stanislav Melnikov
 * @version 1.0
 */
public class UserEntityToDtoConverter {
    public static UserDto convert(User user) {
        return UserDto.builder().userId(user.getUserId()).email(user.getEmail()).login(user.getLogin()).
                userSurname(user.getUserSurname()).userName(user.getUserName()).password(user.getPassword()).
                phoneNumber(user.getPhoneNumber()).build();
    }

    public static List<UserDto> convertList(List<User> users) {
        return users.stream().map(UserEntityToDtoConverter::convert).collect(Collectors.toList());
    }
}
