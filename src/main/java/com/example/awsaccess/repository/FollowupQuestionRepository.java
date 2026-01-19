package com.example.awsaccess.repository;

import com.example.awsaccess.model.FollowupQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FollowupQuestionRepository extends JpaRepository<FollowupQuestion, Long> {

    List<FollowupQuestion> findByAccessRequestId(Long accessRequestId);
}

