/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fran.dae.ujabank.seguridad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 *
 * @author Fran
 */
@Configuration
@EnableWebSecurity
public class ConfiguracionSeguridad extends WebSecurityConfigurerAdapter{
 
    @Autowired
    ServicioDatosUsuarioUJABank servicioDatosUsuarioUJABank;
    

    
    @Override
    public void configure(HttpSecurity http) throws Exception{
        http.csrf().disable();
        http.httpBasic();
        
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/ujabank/usuarios").permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/ujabank/usuarios/{DNI}/**").
                access("hasRole('USUARIO') and #DNI == principal.username");
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/ujabank").permitAll();
    }
    
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(servicioDatosUsuarioUJABank).passwordEncoder(new BCryptPasswordEncoder());
    }
}
