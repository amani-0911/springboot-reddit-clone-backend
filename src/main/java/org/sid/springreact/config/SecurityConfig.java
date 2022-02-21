package org.sid.springreact.config;

import lombok.AllArgsConstructor;
import org.sid.springreact.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
      private final UserDetailsService userDetailsService;
      private final PasswordEncoder passwordEncoder;
      private final JwtAuthenticationFilter jwtAuthenticationFilter;


    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
          http.csrf().disable();
          http.headers().frameOptions().disable();
        http.authorizeRequests().antMatchers("/api/auth/**","/h2-console/**").permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/subreddit")
                .permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/posts/")
                .permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/posts/**")
                .permitAll();
        http.authorizeRequests().antMatchers("/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**")
                .permitAll();

        http.authorizeRequests().anyRequest().authenticated();
       http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }



 @Autowired
  public void  configureGlobal(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
  authenticationManagerBuilder.userDetailsService(userDetailsService)
          .passwordEncoder(passwordEncoder);
  }

}
