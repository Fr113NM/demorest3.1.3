package com.crud.demorest.configs;

import com.crud.demorest.configs.handler.LoginSuccessHandler;
import com.crud.demorest.services.UserDetailServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private LoginSuccessHandler loginSuccessHandler;
    private UserDetailServiceImp userDetailServiceImp;

    @Autowired
    public void setLoginSuccessHandler(LoginSuccessHandler loginSuccessHandler) {
        this.loginSuccessHandler = loginSuccessHandler;
    }

    @Autowired
    public void setUserService(UserDetailServiceImp userDetailServiceImp) {
        this.userDetailServiceImp = userDetailServiceImp;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .formLogin()
            .loginPage("/login")
            .permitAll()
            .successHandler(loginSuccessHandler);

        http
            .authorizeRequests()
            .antMatchers("/").permitAll()
            .antMatchers("/login").permitAll()
            .antMatchers("/user").hasAnyRole("USER", "ADMIN")
            .antMatchers("/admin").hasRole("ADMIN")
            .antMatchers("/webjars").permitAll()
            .antMatchers("/resources").permitAll();

        http.logout()
            .permitAll()
            .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
            .logoutSuccessUrl("/")
            .and().csrf().disable();

        http.httpBasic()
            .and()
            .authorizeRequests()
            .antMatchers("/api/home")
            .hasRole("ADMIN")
            .antMatchers("/api/product/*")
            .hasRole("ADMIN");

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
            .ignoring()
            .antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/img/**", "/icon/**");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        authenticationProvider.setUserDetailsService(userDetailServiceImp);
        return authenticationProvider;
    }
}
