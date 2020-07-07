package com.learnspringboot.ppmtool.web;


import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learnspringboot.ppmtool.domain.User;
import com.learnspringboot.ppmtool.payload.JWTLoginSuccessResponse;
import com.learnspringboot.ppmtool.payload.LoginRequest;
import com.learnspringboot.ppmtool.repositories.UserRepository;
import com.learnspringboot.ppmtool.security.JwtTokenProvider;
import com.learnspringboot.ppmtool.services.MapValidationErrorService;
import com.learnspringboot.ppmtool.services.UserService;
import com.learnspringboot.ppmtool.validator.UserValidator;

import static com.learnspringboot.ppmtool.security.SecurityConstants.TOKEN_PREFIX;

@RestController
@RequestMapping("/api/users")
public class UserController {
	
	@Autowired
	private MapValidationErrorService mapValidationErrorService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserValidator userValidator;
	
	@Autowired
	private JwtTokenProvider tokenProvider;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@PostMapping("/login")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest, BindingResult result){
		
		ResponseEntity<?> mapError = mapValidationErrorService.mapValidation(result);
		if(mapError != null) return mapError;
		
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						loginRequest.getUsername(),
						loginRequest.getPassword())
				);
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = TOKEN_PREFIX + tokenProvider.generateToken(authentication);
		
	
			
		return new ResponseEntity<JWTLoginSuccessResponse>(new JWTLoginSuccessResponse(true, jwt), HttpStatus.OK);
	}
	

	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@Valid @RequestBody User user,BindingResult result){
		
		//Valid passwords match
		userValidator.validate(user, result);
		
		ResponseEntity<?> mapError = mapValidationErrorService.mapValidation(result);
		if(mapError != null) return mapError;
		
		User newUser = userService.saveUser(user);
		
		return new ResponseEntity<User>(newUser,HttpStatus.CREATED);
		
	}
	
	
}
