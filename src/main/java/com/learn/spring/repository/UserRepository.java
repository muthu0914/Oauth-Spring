package com.learn.spring.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.learn.spring.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID>  {

	Optional<User> findByUserName(String username); 
}
