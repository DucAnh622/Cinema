package com.example.movie.service;

import com.example.movie.DTO.UserDTO;
import com.example.movie.common.IdInvalidException;
import com.example.movie.model.ResultBase;
import com.example.movie.model.User;
import com.example.movie.repository.UserRepository;
import com.example.movie.util.CustomUserDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseEntity<?> GetUserById(Integer id) {
        User user =  userRepository.getById(id);
        UserDTO userDTO = new UserDTO();
        if(user != null) {
            userDTO.setId(user.getId());
            userDTO.setUsername(user.getUsername());
            userDTO.setFullname(user.getFullname());
            userDTO.setImage(user.getImage());
            userDTO.setEmail(user.getEmail());
            userDTO.setPhone(user.getPhone());
            userDTO.setAddress(user.getAddress());
            return ResponseEntity.status(HttpStatus.OK).body(userDTO);
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    public ResponseEntity<ResultBase<Object>> Register(User user) {
        ResultBase<Object> response = new ResultBase<>();
        User username = handleGetUserByUsername(user.getUsername());
        User email = handleGetUserByEmail(user.getEmail());
        User phone = handleGetUserByPhone(user.getPhone());
        if(username == null || email == null || phone == null) {
            String newPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(newPassword);
            user.setRole("USER");
            userRepository.save(user);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        else {
            response.setMessage("Email or Phone or Username is used!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    public ResponseEntity<?> User1b (int page, int size) {
        Sort sort = Sort.by(Sort.Direction.ASC, "fullname");
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<User> listUser = userRepository.findAll(pageable);
        if(!listUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(listUser.map(user -> {
                        UserDTO userDto = new UserDTO();
                        userDto.setId(user.getId());
                        userDto.setUsername(user.getUsername());
                        userDto.setFullname(user.getFullname());
                        userDto.setImage(user.getImage());
                        userDto.setEmail(user.getEmail());
                        userDto.setAddress(user.getAddress());
                        userDto.setPhone(user.getPhone());
                        userDto.setRole(user.getRole());
                        return userDto;
                    }));
        }
        else {
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
    }

    public ResponseEntity<?> User2b (int page, int size, String key) {
        Sort sort = Sort.by(Sort.Direction.ASC, "fullname");
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<UserDTO> listUser = userRepository.findByFullnameContainingIgnoreCase(key, pageable);
        if(!listUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(listUser);
        }
        else {
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
    }

    public ResponseEntity<ResultBase<Object>> User4b(User user) throws UsernameNotFoundException, BadCredentialsException {
        if (user == null) {
            throw new UsernameNotFoundException("User not found!");
        }
        Optional<User> actorDetailOptional = userRepository.findById(user.getId());
        if (actorDetailOptional.isPresent()) {
            User userDetail = actorDetailOptional.get();
            userDetail.setUsername(user.getUsername());
            userDetail.setFullname(user.getFullname());
            userDetail.setEmail(user.getEmail());
            userDetail.setPhone(user.getPhone());
            userDetail.setAddress(user.getAddress());
            userDetail.setImage(user.getImage());
            userRepository.save(userDetail);

            return ResponseEntity.status(HttpStatus.OK).body(null);
        } else {
            throw new BadCredentialsException("Something wrongs!");
        }
    }

    public User handleGetUserByUsername(String username) {
        return this.userRepository.findByUsername(username);
    }

    public User handleGetUserByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

    public User handleGetUserByPhone(String phone) {
        return this.userRepository.findByPhone(phone);
    }

    public  ResponseEntity<ResultBase<Object>> User3b (int id) {
        ResultBase<Object> res = new ResultBase<>();
        Optional<User> user = userRepository.findById(id);
        if(user == null) {
            res.setMessage("User is not exist!");
            return  ResponseEntity.badRequest().body(res);
        }
        else {
            userRepository.delete(user.get());
            return  ResponseEntity.ok().body(null);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }
        return new CustomUserDetail(user);
    }

    public void updateUserToken(String token, String username) {
        User currentUser = this.handleGetUserByUsername(username);
        if (currentUser != null) {
            currentUser.setRefreshToken(token);
            this.userRepository.save(currentUser);
        }
    }
}
