package com.project.shopApp.services.user;

import com.project.shopApp.dtos.UpdateUserDTO;
import com.project.shopApp.dtos.UserDTO;
import com.project.shopApp.exceptions.DataNotFoundException;
import com.project.shopApp.models.User;

public interface IUserService {
    User createUser(UserDTO userDTO) throws Exception;

    String login(String phoneNumber,String password,Long roleId) throws Exception;

    User getUserDetailsFromToken(String token) throws Exception;
    User getUserDetailsFromRefreshToken(String token) throws Exception;

    User updateUser(Long userId, UpdateUserDTO updatedUserDTO) throws DataNotFoundException;
}
