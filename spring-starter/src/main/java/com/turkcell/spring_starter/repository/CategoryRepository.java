package com.turkcell.spring_starter.repository;

import java.util.Set;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import com.turkcell.spring_starter.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID>
{
	boolean existsByNameIgnoreCase(String name);

	@Query("SELECT c FROM Category c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))")
	Set<Category> search(String name);
}