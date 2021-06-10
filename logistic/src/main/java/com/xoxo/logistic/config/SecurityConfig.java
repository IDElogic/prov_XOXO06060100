package com.xoxo.logistic.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.xoxo.logistic.security.JwtAuthFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class  SecurityConfig extends WebSecurityConfigurerAdapter{

	@Autowired
	JwtAuthFilter jwtAuthFilter;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
		@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		auth.inMemoryAuthentication()
			.passwordEncoder(passwordEncoder())
			.withUser("addressUser").authorities("AddressManager").password(passwordEncoder().encode("passAddress"))
			.and()
			.withUser("transportUser").authorities("TransportManager").password(passwordEncoder().encode("passTransport"))
			.and()
			.withUser("admin").authorities("AddressManager", "TransportManager").password(passwordEncoder().encode("passAdmin"));
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {	
		http
			.csrf().disable()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.authorizeRequests()
			.antMatchers("/api/login/**").permitAll()
			.antMatchers("/api/addresses/**").permitAll()
			.antMatchers("/api/transports/**").hasAuthority("TransportManager")
			.anyRequest().denyAll();
		
		http
			.addFilterBefore(jwtAuthFilter,UsernamePasswordAuthenticationFilter.class);
	}

}



