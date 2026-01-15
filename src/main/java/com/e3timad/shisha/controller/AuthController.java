package com.e3timad.shisha.controller;

import com.e3timad.shisha.model.User;
import com.e3timad.shisha.repository.UserRepository;
import com.e3timad.shisha.security.JwtUtil;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final UserRepository userRepository;

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody User loginRequest) {

        Map<String, Object> response = new HashMap<>();

        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElse(null);

        if (user == null || !user.getPassword().equals(loginRequest.getPassword())) {
            response.put("success", false);
            response.put("message", "بيانات الدخول غير صحيحة");
            return response;
        }

        if (!user.getRole().equals("ADMIN")) {
            response.put("success", false);
            response.put("message", "ليس لديك صلاحية الدخول");
            return response;
        }

        String token = JwtUtil.generateToken(user.getUsername(), user.getRole());

        response.put("success", true);
        response.put("token", token);

        return response;
    }

}
