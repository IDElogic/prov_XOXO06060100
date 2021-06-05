package com.xoxo.logistic.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class  SecurityConfigProperties extends WebSecurityConfigurerAdapter{

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
		@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
//		auth.inMemoryAuthentication()
//			.passwordEncoder(passwordEncoder())
//			.withUser("addressUser").authorities("AddressManager").password(passwordEncoder().encode("passadd"))
//			.and()
//			.withUser("transportUser").authorities("TransportManager").password(passwordEncoder().encode("passtrans"))
//			.and()
//			.withUser("admin").authorities("AddressManager", "TransportManager").password(passwordEncoder().encode("passany"));
	}

	private AuthenticationProvider authenticationProvider() {
			return null;
		}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http
			.httpBasic()
			.and()
			.csrf().disable()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.authorizeRequests()
			.antMatchers("/api/login/**").permitAll()
			.antMatchers(HttpMethod.POST, "/api/addresses/**").hasAuthority("AddressManager")
			.antMatchers(HttpMethod.PUT, "/api/addresses/**").hasAuthority("AddressManager")
			.antMatchers(HttpMethod.DELETE, "/api/addresses/**").hasAuthority("AddressManager")
			.antMatchers(HttpMethod.POST, "/api/transports/**").hasAuthority("TransportManager")
			.anyRequest().denyAll();
		
	}

	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {	
		return super.authenticationManagerBean();
	}	
}



