package com.xuyang.OnlyApp.controller;

import com.qiniu.util.Auth;
import com.xuyang.OnlyApp.entity.User;
import com.xuyang.OnlyApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class Homepage {

    @Autowired
    private UserRepository userRepository;

    @GetMapping(path = "/")
    public String hello() {
        return "hello world";
    }

    @GetMapping(path = "/time")
    public String getTime() {
        return new Date().toString();
    }

    @PostMapping("/qiniu_token")
    public String getQiniuToken(@RequestBody Map<String, Object> data) {
        String AccessKey = data.get("AccessKey").toString();
        String SecretKey = data.get("SecretKey").toString();
        String Bucket = data.get("Bucket").toString();

        Auth auth = Auth.create(AccessKey, SecretKey);
        return auth.uploadToken(Bucket);
    }

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));
        System.out.println("接收到一个用户： " + user);
        return userRepository.save(user);
    }

    @PostMapping("/login")
    public User login(@RequestBody Map<String, Object> data) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String username = data.get("username").toString();
        String password = data.get("password").toString();
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent() && encoder.matches(password, user.get().getPassword())) {
            return user.get();
        }
        return new User((long) -1, "NULL", "", "");
    }
}
