package com.krupa.finsightai.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.krupa.finsightai.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
}