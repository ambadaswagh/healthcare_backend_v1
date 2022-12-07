package com.healthcare.chat.repository;

import com.healthcare.chat.model.entity.ChatSection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
public interface ChatSectionRepository extends CrudRepository<ChatSection, String>, JpaRepository<ChatSection, String>, PagingAndSortingRepository<ChatSection, String> {

    @Query(value = "select cs.* from chat_section as cs where cs.id in ?1 limit 1", nativeQuery = true)
    ChatSection findByIds(List<String> ids);

    @Query(value = "select cs from ChatSection as cs order by cs.updatedAt desc")
    Page<ChatSection> getChatSectionList(Pageable pageable);
}
