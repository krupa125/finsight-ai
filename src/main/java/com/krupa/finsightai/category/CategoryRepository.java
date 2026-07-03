package com.krupa.finsightai.category;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.krupa.finsightai.model.User;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByUser(User user);

}