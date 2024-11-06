package com.example.movie.repository;
import com.example.movie.DTO.UserDTO;
import com.example.movie.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);
    User findByEmail(String email);
    User findByPhone(String phone);
    Page<UserDTO> findByFullnameContainingIgnoreCase(String name, Pageable pageable);
}

