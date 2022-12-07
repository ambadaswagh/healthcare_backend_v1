package com.healthcare.chat.repository;

import com.healthcare.chat.model.entity.MessageBoxBackup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author tungn
 */
@Repository
public interface MessageBoxBackupRepository extends CrudRepository<MessageBoxBackup, Long>, JpaRepository<MessageBoxBackup, Long>, PagingAndSortingRepository<MessageBoxBackup, Long> {

}
