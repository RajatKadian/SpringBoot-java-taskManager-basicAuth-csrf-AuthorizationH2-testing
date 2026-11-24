//package com.example.taskManager;
//import java.time.Instant;
//import java.util.List;
//import org.springframework.http.HttpStatus;
//import org.springframework.security.authentication.*;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
//import org.springframework.security.oauth2.jwt.*;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.server.ResponseStatusException;
//
//@RestController
//public class AuthController {
//
//    private final AuthenticationManager authenticationManager;
//    private final JwtEncoder jwtEncoder;
//
//    public AuthController(
//            AuthenticationManager authenticationManager,
//            JwtEncoder jwtEncoder) {
//
//        this.authenticationManager = authenticationManager;
//        this.jwtEncoder = jwtEncoder;
//    }
//
//    @PostMapping("/login")
//    public TokenResponse login(@RequestBody LoginRequest request) {
//
//        // 1. Verify username and password
//        Authentication auth;
//        try {
//            auth = authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(
//                            request.username(), request.password()
//                    )
//            );
//        } catch (AuthenticationException ex) {
//            throw new ResponseStatusException(
//                    HttpStatus.UNAUTHORIZED, "Invalid credentials"
//            );
//        }
//        // 2. Read verified roles
//        List<String> roles = auth.getAuthorities().stream()
//                .map(GrantedAuthority::getAuthority)
//                .filter(role -> role.startsWith("ROLE_"))
//                .map(role -> role.substring(5))
//                .toList();
//
//        // 3. Create JWT claims
//        Instant now = Instant.now();
//
//        JwtClaimsSet claims = JwtClaimsSet.builder()
//                .issuer("task-manager")
//                .subject(auth.getName())
//                .issuedAt(now)
//                .expiresAt(now.plusSeconds(900))
//                .claim("roles", roles)
//                .build();
//
//        // 4. Sign the token
//        String token = jwtEncoder.encode(
//                JwtEncoderParameters.from(
//                        JwsHeader.with(MacAlgorithm.HS256).build(),
//                        claims
//                )
//        ).getTokenValue();
//
//        return new TokenResponse(token, "Bearer", 900);
//    }
//
//    public record LoginRequest(String username, String password) {}
//
//    public record TokenResponse(
//            String accessToken,
//            String tokenType,
//            long expiresIn
//    ) {}
//}
