package com.cox.auth;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.cox.service.impl.BundleServiceImpl;
import com.cox.service.impl.Configuration;
import com.cox.service.model.User;

public class ServerAuthenticationProvider implements AuthenticationProvider{

	@Autowired
	private BundleServiceImpl service;
	@Autowired
	private Configuration configService;
	
	@Override
	public Authentication authenticate(Authentication authentication)throws AuthenticationException {

		String name = authentication.getName();
        String password = authentication.getCredentials().toString();
        User user = new User(name, password);
        //TODO: verify user and password with corresponding server.
        if (validateUser(user)) {
            List<GrantedAuthority> grantedAuths = new ArrayList<>();
            grantedAuths.add(new SimpleGrantedAuthority("ROLE_USER"));
            Authentication auth = new UsernamePasswordAuthenticationToken(name, password, grantedAuths);
            return auth;
        } else {
            return null;
        }
	}

	private boolean validateUser(User user) {
		return true;/*ContextListener.getServerUserMap().containsValue(user);*/
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}
