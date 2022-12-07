package com.healthcare.chat.repository;

import com.healthcare.chat.model.entity.MessageBox;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 *
 * @author tungn
 */
@Repository
public interface MessageBoxRepository extends CrudRepository<MessageBox, Long>, JpaRepository<MessageBox, Long>, PagingAndSortingRepository<MessageBox, Long> {

    @Query(value = "select mb from MessageBox as mb where mb.fromUser.id in (:fromUser,:toUser) and mb.toUser in (:fromUser,:toUser) order by mb.createdAt desc")
    Page<MessageBox> getMessageBoxListFromTo(@Param("fromUser") long fromAdminId, @Param("toUser") long toAdminId, Pageable pageable);

    @Query(value = "select mb from MessageBox as mb where mb.toUser = -1 and mb.group.id is null order by mb.createdAt desc")
    Page<MessageBox> getMessageBoxListAllToAdmin(Pageable pageable);

    @Query(value = "select mb from MessageBox as mb where mb.group.id =:groupId order by mb.createdAt desc")
    Page<MessageBox> getMessageBoxListByGroupId(@Param("groupId") long groupId, Pageable pageable);

    @Query(value = "select mb from MessageBox as mb where mb.fromUser.id in (:fromUser,:toUser) and mb.toUser in (:fromUser,:toUser) and mb.createdAt >=:fromDate and mb.createdAt <=:toDate order by mb.createdAt desc")
    List<MessageBox> getMessageBoxListByFromToDate(@Param("fromUser") long fromAdminId, @Param("toUser") long toAdminId, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate);

    @Query(value = "select mb from MessageBox as mb Where createdAt <=:toDate order by createdAt asc")
    List<MessageBox> getMessageBoxToDate(@Param("toDate") Date toDate, Pageable pageable);
}
