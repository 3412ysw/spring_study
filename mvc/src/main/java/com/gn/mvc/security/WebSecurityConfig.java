package com.gn.mvc.security;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import lombok.RequiredArgsConstructor;

@Configuration //환경설정파일
@EnableWebSecurity // 스프링 시큐리티 쓸거에용~~
@RequiredArgsConstructor
public class WebSecurityConfig {
	
	private final DataSource dataSource;
	
	// 요청중에 정적 리소스가 있는 경우 -> Security 비활성화
	@Bean
	WebSecurityCustomizer configure() {
		// 접근 제한자 무조건 public 이여서 뺴고 쓰는 것을 권장
		// 특정 Url을 가지고 있는 애를 무시할거다 ! (경로 찾는 것)
		return web -> web.ignoring()
				.requestMatchers(PathRequest.toStaticResources().atCommonLocations());
	}

	// 특정 요청이 들어왔을 때 어떻게 처리할 것인가
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http, UserDetailsService customUserDetailsService) throws Exception{
		// 3가지 Url 말고 다른 요청들은 인증된 사람만 들어올 수 있게 할거다
			http.userDetailsService(customUserDetailsService)
					.authorizeHttpRequests(requests -> requests
					.requestMatchers("/login","/signup","/logout","/").permitAll()
					.requestMatchers("/admin/**").hasRole("ADMIN")
					.anyRequest().authenticated())
			.formLogin(login -> login.loginPage("/login")
								.successHandler(new MyLoginSuccessHandler())
								.failureHandler(new MyLoginFailureHandler()))
			.logout(logout -> logout.logoutUrl("/logout")
					            .clearAuthentication(true)
								.logoutSuccessUrl("/login")
								.invalidateHttpSession(true)
								.deleteCookies("remember-me"))
			.rememberMe(rememberMe -> rememberMe.rememberMeParameter("remember-me")
												.tokenValiditySeconds(60*60*24*30)
												.alwaysRemember(false)
												.tokenRepository(tokenRepository()));
		return http.build();
	}
	
	// 데이터베이스 접근 Bean 등록
	@Bean
	PersistentTokenRepository tokenRepository() {
		JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
		jdbcTokenRepository.setDataSource(dataSource);
		return jdbcTokenRepository;
	}
	
	//비밀번호 암호화에 사용할 Bean 등록
	@Bean 
	PasswordEncoder passswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	// AuthenticationManger (인증 관리)
	@Bean
	AuthenticationManager authenticationManager(
			AuthenticationConfiguration authenticationConfiguration) throws Exception{
		return authenticationConfiguration.getAuthenticationManager();
	}
	
}
