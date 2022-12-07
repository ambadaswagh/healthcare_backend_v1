package com.healthcare.repository;

import com.healthcare.model.entity.Media;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MediaRepository extends JpaRepository<Media, Long>, JpaSpecificationExecutor<Media> {

    @Query(value = "select u from Media u " +
            " where  " +
            "  (u.agency.id = :agency) " +
            "   and (u.company.id = :company)" )
    Page<Media> findAllMedia(@Param("agency") Long agencyId,
                             @Param("company") Long companyId,
                             Pageable pageable);
}
