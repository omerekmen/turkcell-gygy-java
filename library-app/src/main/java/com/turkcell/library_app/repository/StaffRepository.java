package com.turkcell.library_app.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.turkcell.library_app.entity.Staff;

@Repository
public interface StaffRepository extends JpaRepository<Staff, UUID> {
    boolean existsByStaffNumber(String staffNumber);
    boolean existsByUserId(UUID userId);
}
