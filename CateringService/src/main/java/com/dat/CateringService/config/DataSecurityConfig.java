package com.dat.CateringService.config;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
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
	public void configure(WebSecurity web) throws Exception {
	    web.ignoring().antMatchers("/resources/**");
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers("/admin/**").hasRole("admin")
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
			.exceptionHandling().accessDeniedPage("/access-denied")
			.and()
//	        .csrf().disable()
//	        .httpBasic().disable()
//	        .formLogin().disable()
//	        .logout().disable()
//	        .headers().frameOptions().disable()
//	        .and()
	        .exceptionHandling().authenticationEntryPoint((request, response, authException) -> response.sendError(HttpServletResponse.SC_NOT_FOUND));
	}
	
	

}