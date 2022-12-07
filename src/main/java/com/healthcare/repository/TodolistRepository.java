package com.healthcare.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.healthcare.model.entity.Todolist;

@Repository
public interface TodolistRepository extends JpaRepository<Todolist, Long> {

    @Query(value = "select * from todolist where admin_id = :adminId and status = :status order by updated_at desc"
            , nativeQuery = true)
    List<Todolist> getTodolistByAdminAndStatus(@Param("adminId") Long adminId, @Param("status") Integer status);
    
    @Query(value = "select * from todolist where admin_id = :adminId order by updated_at desc"
            , nativeQuery = true)
    List<Todolist> getTodolistByAdmin(@Param("adminId") Long adminId);


}