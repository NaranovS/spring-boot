package com.naranov.spring_boot_v1.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, proxyTargetClass = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService customUserDetailsService;

    @Autowired
    private DataSource dataSource;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()

                .antMatchers("/api/**").permitAll()

                .antMatchers("/", "/list")
                .access("hasRole('USER') or hasRole('ADMIN') or hasRole('DBA')")

                .antMatchers("/newUser/**", "/deleteUser-*")
                .access("hasRole('ADMIN')")

                .antMatchers("/editUser-*")
                .access("hasRole('ADMIN') or hasRole('DBA')")


                .and()

                .formLogin().loginPage("/login")
                .loginProcessingUrl("/login").usernameParameter("login").passwordParameter("password")

                                .and()
                    .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .logoutSuccessUrl("/login").deleteCookies("JSESSIONID")
                    .invalidateHttpSession(true)

//                .and()
//
//                .rememberMe()
//                .rememberMeParameter("remember-me")
//                .tokenRepository(tokenRepository)
//                .tokenValiditySeconds(60 * 60 * 24)

                .and()

                .exceptionHandling().accessDeniedPage("/Access_Denied")

                .and()

                .logout().permitAll()

                .and()

                    // csrf conflicts with PUT, DELETE, POST. need to configure for working
                    .csrf().disable();
    }

    PersistentTokenRepository persistentTokenRepository(){
        JdbcTokenRepositoryImpl tokenRepositoryImpl = new JdbcTokenRepositoryImpl();
        tokenRepositoryImpl.setDataSource(dataSource);
        return tokenRepositoryImpl;
    }
}