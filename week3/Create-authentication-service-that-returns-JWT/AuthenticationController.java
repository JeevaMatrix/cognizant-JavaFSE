package com.cognizant.spring_learn;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthenticationController {

    private static final SecretKey key =
            Keys.secretKeyFor(SignatureAlgorithm.HS256);

    @GetMapping("/authenticate")
    public ResponseEntity<?> authenticate(
            @RequestHeader("Authorization") String authHeader) {

        // Check Authorization Header
        if (authHeader == null || !authHeader.startsWith("Basic ")) {
            return ResponseEntity.status(401)
                    .body("Missing or invalid Authorization header");
        }

        // Decode Base64 credentials
        String base64Credentials = authHeader.substring("Basic ".length());
        byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
        String credentials = new String(credDecoded);

        String[] values = credentials.split(":", 2);

        if (values.length != 2) {
            return ResponseEntity.status(401)
                    .body("Invalid username/password format");
        }

        String username = values[0];
        String password = values[1];

        // Validate credentials
        if (!username.equals("user") || !password.equals("pwd")) {
            return ResponseEntity.status(401)
                    .body("Invalid username or password");
        }

        // Generate JWT Token
        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 600000))
                .signWith(key)
                .compact();

        Map<String, String> response = new HashMap<>();
        response.put("token", token);

        return ResponseEntity.ok(response);
    }
}
