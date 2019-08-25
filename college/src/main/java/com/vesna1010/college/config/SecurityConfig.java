package com.vesna1010.college.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsService userDetailsService;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
	}

	@Override
	public void configure(WebSecurity http) throws Exception {
		http.ignoring().antMatchers("/static/**");
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
		        .antMatchers("/study_programs/details").permitAll()
		        .regexMatchers("\\/departments\\/?").authenticated()
		        .regexMatchers("\\/study_programs\\/?(\\?id=\\d+)?").authenticated()
		        .regexMatchers("\\/subjects\\/?(\\?id=\\d+)?").authenticated()
		        .regexMatchers("\\/students\\/?(\\?id=\\d+)?").authenticated()
		        .regexMatchers("\\/exams\\/?(\\?id=\\d+)").authenticated()
		        .regexMatchers("\\/exams/search\\/?(\\?id=\\d+)").authenticated()
		        .regexMatchers("\\/students/exams\\/?(\\?id=\\d+)").authenticated()
		        .regexMatchers("\\/users/edit\\/?").authenticated()
				.antMatchers(HttpMethod.POST, "/users/update").authenticated()
				.antMatchers("/departments/**").hasAnyAuthority("USER", "ADMIN")
				.antMatchers("/study_programs/**").hasAnyAuthority("USER", "ADMIN")
				.antMatchers("/subjects/**").hasAnyAuthority("USER", "ADMIN")
				.antMatchers("/professors/**").hasAnyAuthority("USER", "ADMIN")
				.antMatchers("/students/**").hasAnyAuthority("USER", "ADMIN")
				.antMatchers("/exams/**").hasAnyAuthority("USER", "ADMIN")
				.antMatchers("/users/**").hasAnyAuthority("ADMIN")
				.and()
				.formLogin()
				    .loginPage("/login")
				    .loginProcessingUrl("/login")
				    .failureUrl("/login?error=true")
				    .defaultSuccessUrl("/")
				    .usernameParameter("email")
				    .passwordParameter("password")
				.and()
				.logout()
				    .logoutUrl("/logout")
				    .logoutSuccessUrl("/login");
	}

}
