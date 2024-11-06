package com.example.movie.controller;
import com.example.movie.model.ResultBase;
import com.example.movie.model.User;
import com.example.movie.service.UserService;
import com.example.movie.util.ApiMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    private UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/system/user1a")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    ResponseEntity<?> User1a(@RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "10") int size,
                                      @RequestParam(required = false) String key) {
        if(key == null || key.isEmpty()) {
            return userService.User1b(page, size);
        }
        else {
            return userService.User2b(page, size, key);
        }
    }

    @GetMapping("/system/user2a")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    ResponseEntity<?> User2a(@RequestParam Integer id) {
        return userService.GetUserById(id);
    }

    @PutMapping("system/user4a")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    @ApiMessage("Update success!")
    ResponseEntity<ResultBase<Object>> User4a(@RequestBody User user) {
        return userService.User4b(user);
    }

    @DeleteMapping("system/user3a")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ApiMessage("Delete success!")
    ResponseEntity<ResultBase<Object>> User3a(@RequestParam int id) {
        return userService.User3b(id);
    }
}
