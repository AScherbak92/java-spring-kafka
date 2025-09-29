package by.gsu.scherbak.orderService.service;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

/*
 * Service for generating and validating api keys
 *
 * @version 1.0 21.09.2025
 * @author Scherbak Andrey
 * */
@Service
public class ApiKeyService {
    private final SecretKey secret = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final long EXPIRATION = 86400000;

    /*Generating api key method*/
    public String generateKey() {
        return Jwts.builder()
                .setSubject("order")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(secret)
                .compact();
    }

    /*Validating api key*/
    public boolean validateKey(String key) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secret)
                    .build()
                    .parseClaimsJws(key);

            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
