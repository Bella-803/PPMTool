package com.learnspringboot.ppmtool.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.learnspringboot.ppmtool.domain.User;
import com.learnspringboot.ppmtool.exceptions.UsernameAlreadyExistException;
import com.learnspringboot.ppmtool.repositories.UserRepository;

@Service
public class UserService{

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder bcryptPassword;
	
	public User saveUser(User newUser) {
		
		try {
			
			newUser.setPassword(bcryptPassword.encode(newUser.getPassword()));
			
			//Username has to be unique
			newUser.setUsername(newUser.getUsername());
			
			//Make sure that password and confirm password match
			//we don t persist or show the confirm password
			newUser.setConfirmPassword("");
			return userRepository.save(newUser);
		} catch (Exception e) {
			throw new UsernameAlreadyExistException("Username: '"+newUser.getUsername()+" 'already exists");
		}
		
		
	}

}
