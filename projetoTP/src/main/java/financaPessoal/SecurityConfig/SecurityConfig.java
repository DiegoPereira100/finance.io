package financaPessoal.SecurityConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Desativa CSRF (não recomendado em produção sem validação extra)
                .authorizeHttpRequests(authz -> authz
                        // Libera acesso às rotas da API e Swagger
                        .requestMatchers(
                                "/users/register",
                                "/users/login",
                                "/conta",
                                "/conta/**",
                                "/analise-geral",
                                "/transacao",
                                "/transacao/**",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-ui.html").permitAll()
                        // Requer autenticação para todas as outras rotas
                        .anyRequest().authenticated()
                );
        return http.build();
    }
}
