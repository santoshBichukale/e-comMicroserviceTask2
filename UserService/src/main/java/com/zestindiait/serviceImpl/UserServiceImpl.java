package com.zestindiait.serviceImpl;

import com.zestindiait.customeexecption.UserAlreadyExistsException;
import com.zestindiait.customeexecption.UserNotFoundException;
import com.zestindiait.dto.LoginDto;
import com.zestindiait.entities.User;
import com.zestindiait.repository.UserRepository;
import com.zestindiait.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

  @Autowired
    private PasswordEncoder passwordEncoder;




    @Override
    public User saveUser(User user) {

        if (userRepository.findByUserName(user.getUserName()).isPresent()) {
            throw new UserAlreadyExistsException("User already exists with username: " + user.getUserName());
        }

        if (user.getUserId() == null || user.getUserId().trim().isEmpty()) {
            user.setUserId(UUID.randomUUID().toString());
        }
        user.setUserPassword(passwordEncoder.encode(user.getUserPassword()));
        return userRepository.save(user);
    }


    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(String id) throws UserNotFoundException {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    @Override
    public User login(LoginDto request) {
        Optional<User> userOptional = userRepository.findByUserName(request.getUserName());

        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("User not found: " + request.getUserName());
        }

        User user = userOptional.get();


        return user;
    }

    @Override
    public void deleteUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
        userRepository.delete(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByUserName(username);

        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("User not found: " + username);
        }

        User user = userOptional.get();

        return new org.springframework.security.core.userdetails.User(
                user.getUserName(),
                user.getUserPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
        );
    }

}
