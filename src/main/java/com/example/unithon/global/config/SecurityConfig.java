package com.example.unithon.global.config;

import com.example.unithon.global.jwt.JwtFilter;
import com.example.unithon.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // CSRF 비활성화
                .csrf(AbstractHttpConfigurer::disable)

                // 세션을 사용 x -> STATELESS
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 인증 설정
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("OPTIONS", "/**").permitAll()
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/swagger-resources/**",
                                "/v3/api-docs/**",
                                "/v3/api-docs",
                                "/webjars/**",
                                "/actuator/**",
                                "/favicon.ico",
                                "/error"
                        ).permitAll()
                        // 회원 - 회원가입/로그인/토큰갱신은 인증 X
                        .requestMatchers(HttpMethod.POST, "/api/members/signup", "/api/members/login", "/api/members/refresh").permitAll()
                        // 회원 - 목록 조회, 마이페이지와 프로필 관리는 인증 필요
                        .requestMatchers(HttpMethod.GET, "/api/members/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/members/me").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/members/me/profile").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/members/me/profile-image").authenticated()

                        // 게시글 - 조회는 인증 X, 관리는 ADMIN만
                        .requestMatchers(HttpMethod.GET, "/api/posts/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/posts").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/posts/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/posts/**").authenticated()

                        // 게시글 좋아요 - 모든 작업에 인증 필요
                        .requestMatchers("/api/posts/*/likes").authenticated()

                        // 동아리 - 조회는 인증 X, 관리는 ADMIN만
                        .requestMatchers(HttpMethod.GET, "/api/clubs/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/clubs").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/clubs/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/clubs/**").hasRole("ADMIN")

                        // 채용 공고 - 조회는 인증 X, 관리는 ADMIN만
                        .requestMatchers(HttpMethod.GET, "/api/jobs/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/jobs").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/jobs/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/jobs/**").hasRole("ADMIN")

                        // 알림 - 모든 작업에 인증 필요
                        .requestMatchers("/api/notifications/**").authenticated()

                        .anyRequest().authenticated()
                )

                // JWT 검증 필터 삽입
                .addFilterBefore(new JwtFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOriginPattern("*");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
