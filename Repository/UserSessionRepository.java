package com.example.oilbilling.repository;

import com.example.oilbilling.model.UserSessionStatusType;
import com.example.oilbilling.model.UserSessions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserSessionRepository extends JpaRepository<UserSessions, Long> {

    UserSessions findBySessionIdAndStatus ( String sessionId, UserSessionStatusType status);
}

