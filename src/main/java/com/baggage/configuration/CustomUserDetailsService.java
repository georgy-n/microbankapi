package com.baggage.configuration;

import com.baggage.entity.dao.ClientDao;
import com.baggage.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service("customUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private ClientService clientService;

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

		if (userName.trim().isEmpty()) {
			throw new UsernameNotFoundException("username is empty");
		}

		Optional<ClientDao> user = clientService.findByUsername(userName);

		if (!user.isPresent()) {
			throw new UsernameNotFoundException("User " + userName + " not found");
		} else return new org.springframework.security.core.userdetails.User(
				user.get().getLogin(),
				user.get().getPassword(),
				getGrantedAuthorities(user.get()));
	}

	private List<GrantedAuthority> getGrantedAuthorities(ClientDao user) {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		String role = user.getRole();
		authorities.add(new SimpleGrantedAuthority(role));
		return authorities;
	}

}
