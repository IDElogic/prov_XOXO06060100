package com.xoxo.logistic.security;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

@Service
public class JwtService {
	
	public String createJwtToken(UserDetails principal) {
		 return JWT.create()
			.withSubject(principal.getUsername())
			.withArrayClaim("auth", principal.getAuthorities().stream().map(GrantedAuthority::getAuthority).toArray(String[]::new))
			.withExpiresAt( new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(10)))
			.withIssuer("TransportApp")
			.sign(Algorithm.HMAC256("whatever"));	
	}
}
	
