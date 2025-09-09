package org.example.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsService userDetailsService; // ⚡ AGREGAR ESTO
    private final JwtService jwtService;


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username already taken");
        }

        // Crear usuario nuevo
        AppUser user = new AppUser();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getUsername()); // si no tenés email en AuthRequest
       // user.setRol("RECEPCIONISTA"); // por defecto asignamos "USER"
        user.setRol("ADMIN"); // por defecto asignamos "USER"

        userRepository.save(user);

        // usar el servicio de detalles de usuario
        UserDetails ud = userDetailsService.loadUserByUsername(user.getUsername());
        String token = jwtService.generateToken(ud);

        // armar lista de roles
        List<String> roles = ud.getAuthorities()
                .stream()
                .map(auth -> auth.getAuthority())
                .toList();

        return ResponseEntity.ok(new AuthResponse(token, roles));
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }

        UserDetails ud = userDetailsService.loadUserByUsername(request.getUsername());
        String token = jwtService.generateToken(ud);

        List<String> roles = ud.getAuthorities()
                .stream()
                .map(auth -> auth.getAuthority())
                .toList();

        return ResponseEntity.ok(new AuthResponse(token, roles));
    }

}
