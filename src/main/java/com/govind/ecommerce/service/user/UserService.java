package com.govind.ecommerce.service.user;

import com.govind.ecommerce.exception.ResourceNotFoundException;
import com.govind.ecommerce.model.User;
import com.govind.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService{

    private final UserRepository userRepository;

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
