package com.healthcare.chat.repository;

import com.healthcare.chat.model.entity.GroupMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 * @author tungn
 */
@Repository
public interface GroupMemberRepository extends CrudRepository<GroupMember, Long>, JpaRepository<GroupMember, Long>, PagingAndSortingRepository<GroupMember, Long> {

    @Query(value = "select gm from GroupMember as gm where gm.group.id =:groupId")
    List<GroupMember> getGroupMemberListByGroupId(@Param("groupId") long groupId);

    @Query("select gm from GroupMember as gm where gm.user.id in ?1")
    List<GroupMember> getGroupMemberListByUserIds(List<Long> userIds);
}
