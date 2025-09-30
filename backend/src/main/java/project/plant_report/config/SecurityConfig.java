package project.plant_report.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .authorizeHttpRequests(authz -> authz
               // 인증 불필요 (누구나 접근 가능)
               .requestMatchers("/api/auth/**").permitAll()  // 로그인, 회원가입
               .requestMatchers("/api/user").permitAll()     // 회원가입
               .requestMatchers("/h2-console/**").permitAll() // H2 콘솔
               .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll() // Swagger
               
               // 커뮤니티 - HTTP 메서드별 세밀한 제어
               .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/community/**").permitAll() // 조회는 누구나
               .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/community/**").authenticated() // 작성은 인증 필요
               .requestMatchers(org.springframework.http.HttpMethod.PUT, "/api/community/**").authenticated() // 수정은 인증 필요
               .requestMatchers(org.springframework.http.HttpMethod.DELETE, "/api/community/**").authenticated() // 삭제는 인증 필요
               
               // 식물 관리 - 모두 인증 필요
               .requestMatchers("/api/plant/**").authenticated()
               
               // 나머지는 인증 필요
               .anyRequest().authenticated()
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .headers(headers -> headers.frameOptions().disable()); // H2 콘솔용

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
