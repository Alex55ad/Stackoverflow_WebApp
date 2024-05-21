package com.utcn.demo.service;

import com.utcn.demo.entity.User;
import com.utcn.demo.repository.UserRepository;
import com.utcn.demo.service.validations.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
@RequiredArgsConstructor
public class UserService {
   private final UserValidator userValidator = new UserValidator();
   private final AnswerService answerService;
   private final QuestionService questionService;
   private final UserRepository userRepository;
   // PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();


    public List<User> retrieveUsers() {
        return (List<User>) this.userRepository.findAll();
    }

    public User insertUser(User user) {
        return this.userRepository.save(user);
    }

    public void deleteUserById(Long id) {
            if(userRepository.findById(id).isEmpty())
                throw new RuntimeException("User not found");
            else
                this.userRepository.deleteById(id);
    }

    public User loginUser(String username, String password) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            String hashedPassword = hashPassword(password);
            if (user.getPassword().equals(hashedPassword)) {
                return user;
            }
        }
        return null;
    }

    public User createUser(User user) {
        if (!userValidator.validateUsername(user.getUsername())) {
            throw new RuntimeException("Invalid username");
        }
        String passwordValidationResult = userValidator.validatePassword(user.getPassword());
        if (!passwordValidationResult.equals("Password OK")) {
            throw new RuntimeException(passwordValidationResult);
        }
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }
        String hashedPassword = hashPassword(user.getPassword());
        user.setPassword(hashedPassword);
        return this.userRepository.save(user);
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder(2 * encodedHash.length);
            for (byte b : encodedHash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    public void encryptExistingPasswords() {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            String hashedPassword = hashPassword(user.getPassword());
            user.setPassword(hashedPassword);
            userRepository.save(user);
        }
    }

    public User updateUserScore(String author) {
        Optional<User> optionalUser = userRepository.findByUsername(author);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            double questionScore = questionService.calculateQuestionScore(author);
            double answerScore = answerService.calculateAnswerScore(author);
            double totalScore = questionScore + answerScore;
            user.setScore(totalScore);
            return this.userRepository.save(user);
        } else {
            return null;
        }
    }


}
