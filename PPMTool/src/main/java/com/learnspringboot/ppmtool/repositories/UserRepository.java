package com.learnspringboot.ppmtool.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.learnspringboot.ppmtool.domain.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long>{

	public User findByUsername(String username);
	
	public User getById(Long id);
}
