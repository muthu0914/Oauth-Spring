package com.learn.spring.service.impl;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.learn.spring.domain.CustomUserDetails;
import com.learn.spring.domain.User;
import com.learn.spring.repository.UserRepository;

@Service("userDetailsService")
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	private UserRepository userRepository;

	public UserDetailsServiceImpl() {
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> userOptional = userRepository.findByUserName(username);
		if (userOptional.isPresent()) {
			CustomUserDetails customUserDetails = new CustomUserDetails(userOptional.get().getUserName(),
					userOptional.get().getPassWord());
			return customUserDetails;
		} else {
			throw new UsernameNotFoundException("User " + username + " not found");
		}
	}

}
