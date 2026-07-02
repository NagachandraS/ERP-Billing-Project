package com.example.oilbilling.repository;

import com.example.oilbilling.model.Users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {

    Optional<Users> findUserByUserNameAndPassWord(String userName, String password);

}


