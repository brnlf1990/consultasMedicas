package com.example.consultaMedica.service;

import com.example.consultaMedica.model.User;
import com.example.consultaMedica.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserLog implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    private UserRepository repo;

    @Autowired
    private PasswordEncoder encoder;

  @Override
public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = repo.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));

    return org.springframework.security.core.userdetails.User
        .withUsername(user.getUsername())
        .password(user.getPassword())
        .roles(user.getRole().name().replace("ROLE_", "")) // Ex: "ROLE_MEDICO" → "MEDICO"
        .build();
}

    public void save(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        repo.save(user);
    }
    public User findByUsername(String username) {
        return repo.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));
    }

    public boolean existsByUsername(String username) {
        return repo.findByUsername(username).isPresent();
    }

}
