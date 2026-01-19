package com.example.awsaccess.repository;

import com.example.awsaccess.model.AccessRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccessRequestRepository extends JpaRepository<AccessRequest, Long> {
}

