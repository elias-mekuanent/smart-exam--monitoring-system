package com.senpare.examservice.repository;

import com.senpare.examservice.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CatagoryRepository extends JpaRepository<Category, UUID> {
}
