package com.marthina.splitconnect.service;

import com.marthina.splitconnect.exception.EmailAlreadyInUseException;
import com.marthina.splitconnect.exception.UserNotFoundException;
import com.marthina.splitconnect.models.User;
import com.marthina.splitconnect.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User create(User user) {
        if (userRepository.existsByEmail(user.getEmail())){
            throw new EmailAlreadyInUseException(user.getEmail());
        }

        return userRepository.save(user);
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User update(Long id, User updated) {
        User user = findById(id);

        user.setName(updated.getName());
        user.setCountry(updated.getCountry());
        //user.setEmail(updated.getEmail());
        //user.setPassword(updated.getPassword());

        return userRepository.save(user);
    }

    //public User updateEmail()

    //public User updatePassword()

    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
