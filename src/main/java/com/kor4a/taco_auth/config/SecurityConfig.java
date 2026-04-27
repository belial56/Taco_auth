package com.kor4a.taco_auth.config;


import com.kor4a.taco_auth.model.User;
import com.kor4a.taco_auth.data.UserRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean
    ApplicationRunner dataLoader(UserRepository repo, PasswordEncoder encoder){
        return args -> {
            repo.save(
                    new User("belial", encoder.encode("password"), "ROLE_ADMIN")
            );
        };
    }

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http)
        throws Exception {
        return http.authorizeHttpRequests(authorizeRequest ->
                authorizeRequest.anyRequest().authenticated()).
                formLogin(Customizer.withDefaults()).build();
    }

    @Bean
    UserDetailsService userDetailsService(UserRepository userRepo){
        return username -> userRepo.findByUsername(username);
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
