package com.example.movie.controller;
import com.example.movie.DTO.SessionDTO;
import com.example.movie.DTO.UserSessionDto;
import com.example.movie.model.ResultBase;
import com.example.movie.model.User;
import com.example.movie.service.UserService;
import com.example.movie.util.ApiMessage;
import com.example.movie.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Value("${movie.public}")
    private String role;

    @Value("${movie.jwt.token-validity-in-seconds}")
    private int expirationToken;

    @Value("${movie.jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenExpiration;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final JwtUtil jwtUtil;

    public AuthController(UserService userService, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    @ApiMessage("Login success")
    public ResponseEntity<ResultBase<UserSessionDto>> login(@Valid @RequestBody SessionDTO sessionDTO) {
        ResultBase<UserSessionDto> response = new ResultBase<>();

        User user = userService.handleGetUserByUsername(sessionDTO.getUsername());
        if (user == null) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setError("User not found");
            response.setMessage("Login failed");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        if (!passwordEncoder.matches(sessionDTO.getPassword(), user.getPassword())) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setError("Invalid credentials");
            response.setMessage("Login failed");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        String token = jwtUtil.generateToken(user.getUsername());
        String refreshToken = jwtUtil.generateRefreshToken(user.getUsername());

        userService.updateUserToken(refreshToken,user.getUsername());

        UserSessionDto userSessionDto = new UserSessionDto();
        userSessionDto.setUsername(user.getUsername());
        userSessionDto.setToken(token);
        userSessionDto.setAuthenication(true);
        if(!user.getRole().equals(role))
        {
            userSessionDto.setAuthorize(true);
        }

        ResponseCookie cookie = ResponseCookie
                .from("jwt", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refreshTokenExpiration)
                .build();

        response.setStatus(HttpStatus.OK.value());
        response.setMessage("Login successful");
        response.setData(userSessionDto);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(response);
    }

    @PostMapping("/logout")
    @ApiMessage("Logout success")
    public ResponseEntity<ResultBase<Object>> Logout() {
        ResponseCookie deleteCookie = ResponseCookie
                .from("jwt", null)
                .build();
        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, deleteCookie.toString())
                .build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<ResultBase<UserSessionDto>> RefreshToken(@RequestBody String accountname) {
        ResultBase<UserSessionDto> response = new ResultBase<>();
        User user = userService.handleGetUserByUsername(accountname);
        String username = jwtUtil.extractUsername(user.getRefreshToken());
        if (username != null && jwtUtil.validateRefreshToken(user.getRefreshToken())) {
            String newAccessToken = jwtUtil.generateToken(username);
            UserSessionDto userSessionDto = new UserSessionDto();
            userSessionDto.setUsername(username);
            userSessionDto.setToken(newAccessToken);

            response.setStatus(HttpStatus.OK.value());
            response.setMessage("Token refreshed successfully");
            response.setData(userSessionDto);
            ResponseCookie cookie = ResponseCookie
                    .from("jwt", newAccessToken)
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .maxAge(refreshTokenExpiration)
                    .build();
            String refreshToken = jwtUtil.generateRefreshToken(username);
            userService.updateUserToken(refreshToken,user.getUsername());
            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, cookie.toString())
                    .body(response);
        }
        else {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setMessage("Account is not exist!");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @PostMapping("/register")
    @ApiMessage("Register success")
    public ResponseEntity<ResultBase<Object>> Register(@Valid @RequestBody User user) {
        return userService.Register(user);
    }
}
