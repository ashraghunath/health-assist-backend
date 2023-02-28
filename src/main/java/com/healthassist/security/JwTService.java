package com.healthassist.security;

import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwTService {
	
	@Value("${jwt.secret}")
    private String secret;

	public String extractUsernameFromToken(String token) {
		// TODO Auto-generated method stub
		return getClaimFromToken(token, Claims::getSubject);
	
	}
	
	public String generateToken(UserDetails user) {
		Map<String, Object> claims = new HashMap<>();
        UserJWT jwtUser = (UserJWT) user;
        if (jwtUser != null) {
            claims.put("id", jwtUser.getId());
            claims.put("sub", user.getUsername());
            claims.put("role", user.getAuthorities());
            claims.put("created", new Date());
        }
		return generateToken(claims, user);
	}
	
	private Claims extractAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret.getBytes(Charset.forName("UTF-8"))).parseClaimsJws(token).getBody();
    }

	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }
	
	private String generateToken(Map<String, Object> extractClaims, UserDetails user) {
		System.out.println();
        return Jwts.builder().setClaims(extractClaims).setSubject(user.getUsername()).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 60 * 1000 * 24)).signWith(SignatureAlgorithm.HS512, secret.getBytes(Charset.forName("UTF-8"))).compact();
    }
	
	public Boolean validateToken(String token, UserDetails userDetails) {
        UserJWT user = (UserJWT) userDetails;
        final String username = extractUsernameFromToken(token);
        final Date created = getIssuedAtDateFromToken(token);
        return (username.equals(user.getUsername()) && !isTokenExpired(token)
                && !isCreatedBeforeLastPasswordReset(created,user.getLastPasswordResetDate()));
    }

	private boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordResetDate) {
		// TODO Auto-generated method stub
		return (lastPasswordResetDate != null && created.before(lastPasswordResetDate));
	}
	

	private boolean isTokenExpired(String token) {
		// TODO Auto-generated method stub
		final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
	
	}

	public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

	public Date getIssuedAtDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getIssuedAt);
    }
}
