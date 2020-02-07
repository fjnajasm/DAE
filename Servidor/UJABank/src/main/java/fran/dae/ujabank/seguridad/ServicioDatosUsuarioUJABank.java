/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fran.dae.ujabank.seguridad;

import fran.dae.ujabank.beans.UJABank;
import fran.dae.ujabank.dto.UsuarioDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 *
 * @author Fran
 */
@Component
public class ServicioDatosUsuarioUJABank implements UserDetailsService{
    
    @Autowired
    UJABank ujabank;

    @Override
    public UserDetails loadUserByUsername(String DNI) throws UsernameNotFoundException {
        
        UsuarioDTO usuDTO = ujabank.buscarUsuario(DNI);
        if(usuDTO == null) {
            throw new UsernameNotFoundException(DNI);
        }
        
        return User.withUsername(DNI).password(usuDTO.getPassword()).roles("USUARIO").build();
        
    }
       
}
