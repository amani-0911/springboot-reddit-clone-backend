package org.sid.springreact.service;

import lombok.AllArgsConstructor;
import org.sid.springreact.dto.AuthenticationResponse;
import org.sid.springreact.dto.LoginRequest;
import org.sid.springreact.dto.RefreshTokenRequest;
import org.sid.springreact.dto.RegisterRequest;
import org.sid.springreact.entities.NotificationEmail;
import org.sid.springreact.entities.User;
import org.sid.springreact.entities.VerificationToken;
import org.sid.springreact.exceptions.SpringRedditException;
import org.sid.springreact.repository.UserRepository;
import org.sid.springreact.repository.VerificationTokenRepository;
import org.sid.springreact.security.JWTProvider;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
public class AuthService {

    private  final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
   private final JWTProvider jwtProvider;
   private final RefreshTokenService refreshTokenService;
    public void  signup(RegisterRequest registerRequest){
        User user=new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setCreated(Instant.now());
        user.setEnabled(false);

        userRepository.save(user);
        String token=generateVerificationToken(user);
       mailService.sendMail(new NotificationEmail("Please Active your Account",user.getEmail(),
               "Thank you for signing up to Spring Reddit, " +
                       "please click on the below url to activate your account : " +
                       "http://localhost:8080/api/auth/accountVerification/" + token ));
    }


    private String generateVerificationToken(User user){
       String token= UUID.randomUUID().toString();
       VerificationToken verificationToken=new VerificationToken();
       verificationToken.setToken(token);
       verificationToken.setUser(user);

       verificationTokenRepository.save(verificationToken);
       return token;
    }

    @Transactional(readOnly = true)
    public User getCurrentUser() {
       //org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.
           //     getContext().getAuthentication().getPrincipal();

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
             username = ((UserDetails)principal).getUsername();
        } else {
             username = principal.toString();
        }
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User name not found - " + username));
    }

    public void verifyAccount(String token) {
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
        verificationToken.orElseThrow(()->new SpringRedditException("Invalid Token"));
fetchUserAndEnable(verificationToken.get());
    }

    private void fetchUserAndEnable(VerificationToken verificationToken) {
     String username=verificationToken.getUser().getUsername();
     User user=userRepository.findByUsername(username).orElseThrow(()->new SpringRedditException("User not Found with name: "+username));
    user.setEnabled(true);
    userRepository.save(user);
    }

    public AuthenticationResponse login(LoginRequest loginRequest) {
  Authentication authentication= authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
           loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
      String token=jwtProvider.generateToken(authentication);
   return  AuthenticationResponse.builder().authenticationtoken(token)
           .refreshToken(refreshTokenService.generateRefreshToken().getToken())
           .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
           .username(loginRequest.getUsername())
           .build();
    }

    public boolean isLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
     refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
        String token = jwtProvider.generateTokenwithUserName(refreshTokenRequest.getUsername());
        return AuthenticationResponse.builder()
                .authenticationtoken(token)
                .refreshToken(refreshTokenRequest.getRefreshToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                .username(refreshTokenRequest.getUsername())
                .build();
    }
}
