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
		auth.userDetailsService(userDetailsService)
		    .passwordEncoder(passwordEncoder);
	}

	@Override
	public void configure(WebSecurity http) {
		http.ignoring()
		    .antMatchers("/static/**");
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
		    .mvcMatchers(
		    		HttpMethod.GET, 
		    		"/study_programs/details"
		    		).permitAll()
		    .mvcMatchers(
		    		HttpMethod.GET, 
		    		"/departments", 
		    		"/study_programs", 
		    		"/subjects", 
		    		"/students", 
		    		"/exams",
		    		"/exams/search", 
		    		"/students/exams",
		    		"/users/edit"
		    		).authenticated()
		    .mvcMatchers(
		    		HttpMethod.POST, 
		    		"/users/update"
		    		).authenticated()
		    .mvcMatchers(
		    		HttpMethod.POST, 
		    		"/users/save"
		    		).hasAuthority("ADMIN")
		    .mvcMatchers(
		    		HttpMethod.POST, 
		    		"/*/save"
		    		).hasAnyAuthority("USER", "ADMIN")
			.mvcMatchers(
					HttpMethod.GET, 
					"/departments/**", 
					"/study_programs/**",
					"/subjects/**",
					"/professors/**",
			        "/students/**", 
			        "/exams/**"
			        ).hasAnyAuthority("USER", "ADMIN")
			
			.mvcMatchers(
					HttpMethod.GET,
					"/users/**"
					).hasAnyAuthority("ADMIN")
			
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
