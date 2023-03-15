package com.dat.CateringService.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class DataSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication()
			.dataSource(dataSource)
			.usersByUsernameQuery("SELECT staffID,password,enabled FROM staff WHERE staffID=?")
			.authoritiesByUsernameQuery("SELECT staffID,role FROM staff WHERE staffID=?")
			.passwordEncoder(passwordEncoder);
			
	}
	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers("/admin/**").hasRole("admin")
			.antMatchers("/suggestion").hasRole("admin")
			.antMatchers("/employee/**").hasRole("operator")
			.antMatchers("/resources/**").permitAll()
			.and()
			.formLogin()	
				.loginPage("/showMyLoginPage")
				.loginProcessingUrl("/authenticateTheUser")
				.permitAll()
			.and()
			.logout()
			.logoutUrl("/logout") 
			.invalidateHttpSession(true) // invalidate the user's session
			.deleteCookies("JSESSIONID") // delete the JSESSIONID cookie
			.logoutSuccessUrl("/showMyLoginPage?logout")
			.permitAll()
			.and()
			.exceptionHandling().accessDeniedPage("/access-denied");
	}
	
	

}