package com.example.repository;

import com.example.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Integer> {
    
    public boolean existsByPostedBy(Integer postedBy);
    
    @Transactional  // needed for modifying 
    @Modifying      // needed bc performing DML operation
    // Query annotation by default is read only
    @Query("DELETE FROM Message m WHERE m.messageId = :message_id")
    public int deleteByIdAndGetCount(Integer message_id);

    public List<Message> findByPostedBy(Integer postedBy);

}
