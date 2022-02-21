package org.sid.springreact.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.sid.springreact.exceptions.SpringRedditException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;
import java.time.Instant;
import java.util.Date;

import static io.jsonwebtoken.Jwts.parser;

@Service
public class JWTProvider {

    private KeyStore keyStore;
    @Value("${jwt.expiration.time}")
    private Long jwtExpirationInMillis;

   @PostConstruct
    public void init(){
        try {
            keyStore=KeyStore.getInstance("JKS");
            InputStream resourceStream=getClass().getResourceAsStream("/springblog.jks");
            keyStore.load(resourceStream,"secret".toCharArray());

        }catch(KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e){
            throw new SpringRedditException("Exception occured while loading Keystore");

        }
    }


    public  String generateToken(Authentication authentication){
       User  principal= (User)authentication.getPrincipal();
      return Jwts.builder()
              .setSubject(principal.getUsername())
              .setIssuedAt(Date.from(Instant.now()))
              .signWith(getPrivateKey())
              .setExpiration(Date.from(Instant.now().plusMillis(jwtExpirationInMillis)))
              .compact();
    }
    public String generateTokenwithUserName(String username){
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(Date.from(Instant.now()))
                .signWith(getPrivateKey())
                .setExpiration(Date.from(Instant.now().plusMillis(jwtExpirationInMillis)))
                .compact();
    }

    private PrivateKey getPrivateKey() {
       try {
            return (PrivateKey) keyStore.getKey("springblog","secret".toCharArray());
        } catch (KeyStoreException | NoSuchAlgorithmException |UnrecoverableKeyException e) {
          throw new SpringRedditException("Exception occured while retrieving private key from key store");
        }
    }

    public boolean validateToken(String jwt){
       parser().setSigningKey(getPublicKey()).parseClaimsJws(jwt);
      return true;
    }
    public String getUsernameFromJwt(String token){
        Claims claims=parser()
                .setSigningKey(getPublicKey())
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();

    }
    private PublicKey getPublicKey(){
        try {
            return  keyStore.getCertificate("springblog").getPublicKey();
        } catch (KeyStoreException  e) {
            throw new SpringRedditException("Exception occured while retrieving public key from key store");
        }
    }

    public Long getJwtExpirationInMillis() {
        return jwtExpirationInMillis;
    }
}
