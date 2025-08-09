package com.govind.ecommerce.service.user;

import com.govind.ecommerce.dto.UserDto;
import com.govind.ecommerce.model.User;
import com.govind.ecommerce.request.CreateUserRequest;
import com.govind.ecommerce.request.UpdateUserRequest;

public interface IUserService {

    User getUserById(Long userId);

    User createUser(CreateUserRequest request);

    User updateUser(UpdateUserRequest request, Long userId);

    void deleteUser(Long userId);

    UserDto convertUserToDto(User user);

    User getAuthenticatedUser();
}
