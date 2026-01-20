package com.example.awsaccess.repository;

import com.example.awsaccess.model.Approval;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApprovalRepository extends JpaRepository<Approval, Long> {

    List<Approval> findByAccessRequestId(Long accessRequestId);
}
