package com.xoxo.logistic.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xoxo.logistic.dto.LoginDto;

@RestController
public class JwtLoginController {

		@Autowired
		AuthenticationManager authenticationManager;
		
		@Autowired
		JwtService jwtService;

		@PostMapping("/api/login")
		public String login(LoginDto loginDto) {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginDto.getUsername(), 
							loginDto.getPassword()));
			return jwtService.createJwtToken((UserDetails)authentication.getPrincipal());
			
	}
}		

