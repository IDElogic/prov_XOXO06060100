package com.xoxo.logistic.security;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.xoxo.logistic.config.SecurityConfigProperties;

@Service
public class JwtService {
	
	@Autowired
	SecurityConfigProperties securityConfigProperties;
	
	private static final int TIMEOUT_MINUTES = 10;
	private static final String AUTH = "auth";
	private Algorithm alg = Algorithm.HMAC256("whatever");
	private String issuer = "TransportApp";
	
	public String createJwtToken(UserDetails principal) {	
		return JWT.create()
			.withSubject(principal.getUsername())
			.withArrayClaim("auth", principal.getAuthorities().stream().map(GrantedAuthority::getAuthority).toArray(String[]::new))
			.withExpiresAt( new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(10)))
			.withIssuer("TransportApp")
			.sign(Algorithm.HMAC256("whatever"));	
	}
	
	public UserDetails parseJwt(String jwtToken) {
		DecodedJWT decodedJwt = JWT.require(alg)
				.withIssuer(issuer)
				.build()
				.verify(jwtToken);
				return new User(decodedJwt.getSubject(), "crazy", 
					decodedJwt.getClaim(AUTH).asList(String.class)
					.stream().map(SimpleGrantedAuthority::new)
					.collect(Collectors.toList()));
		}

	public static int getTimeoutMinutes() {
		return TIMEOUT_MINUTES;
	}
}
	
