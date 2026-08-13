package org.zero_consult.timesheet_backend.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zero_consult.timesheet_backend.security.JwtAuthorizationFilter;
import org.zero_consult.timesheet_backend.security.JwtUtil;
import org.zero_consult.timesheet_backend.security.MockJwtAuthorizationFilter;

@Configuration
public class TestConfig {
    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter(JwtUtil jwtUtil) {
        return new MockJwtAuthorizationFilter(jwtUtil);
    }
}
