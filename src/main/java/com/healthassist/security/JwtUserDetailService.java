package com.healthassist.security;

import java.util.Collection;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.healthassist.entity.User;
import com.healthassist.repository.UserRepository;
import com.healthassist.common.AuthorityName;

@Service
public class JwtUserDetailService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByEmailAddress(username);

		if (user == null) {
			throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
		} else {
			return new UserJWT(user.getUserId(), user.getEmailAddress(), user.getPassword(),
					mapToGrantedAuthorities(user.getAuthority()), user.getLastPasswordResetDate());
		}
	}

	private static Collection<? extends GrantedAuthority> mapToGrantedAuthorities(AuthorityName authorityName) {
		return Collections.singleton(new SimpleGrantedAuthority(authorityName.toString()));
	}
}
