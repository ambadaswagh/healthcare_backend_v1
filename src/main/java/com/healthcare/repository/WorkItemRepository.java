package com.healthcare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.healthcare.model.entity.WorkItem;

/**
 * Created by Mostafa Hamed on 30/06/17.
 * @author mhamed
 * @version 1.0
 */


@Repository
public interface WorkItemRepository extends JpaRepository<WorkItem, Long>, JpaSpecificationExecutor<WorkItem>{

}
