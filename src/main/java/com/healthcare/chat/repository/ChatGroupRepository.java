package com.healthcare.chat.repository;

import com.healthcare.chat.model.entity.ChatGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author tungn
 */
@Repository
public interface ChatGroupRepository extends CrudRepository<ChatGroup, Long>, JpaRepository<ChatGroup, Long>, PagingAndSortingRepository<ChatGroup, Long> {

    ChatGroup findByName(@Param("name") String name);

    @Query(value = "select cg from ChatGroup as cg, GroupMember as gm where gm.user.id =:userId and cg.id = gm.group.id")
    Page<ChatGroup> getChatGroupListContainUser(@Param("userId") long userId, Pageable pageable);
    
    @Query(value = "select cg from ChatGroup as cg")
    Page<ChatGroup> getAllChatGroupList(Pageable pageable);
}
