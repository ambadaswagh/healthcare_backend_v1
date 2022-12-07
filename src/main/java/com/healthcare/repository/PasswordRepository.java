package com.healthcare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.healthcare.model.entity.Password;

@Repository
public interface PasswordRepository extends JpaRepository<Password, Long>, JpaSpecificationExecutor<Password> {
}