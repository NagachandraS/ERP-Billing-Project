package com.example.oilbilling.services;


import com.example.oilbilling.model.UserSessionStatusType;
import com.example.oilbilling.model.UserSessions;
import com.example.oilbilling.repository.UserSessionRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.time.Instant;

@Service
public class UserSessionService {

    @Autowired
    private UserSessionRepository userSessionRepository;

    public void createSessionLog(
            String userName,
            HttpSession session,
            HttpServletRequest request) {
        UserSessions userSessions = new UserSessions(
                userName,
                Instant.now(),
                null,
                session.getId(),
                request.getRemoteAddr(),
                UserSessionStatusType.ACTIVE
        );

        userSessionRepository.save(userSessions);
    }

    public void updateLogoutSession(
            String sessionId,
            UserSessionStatusType userSessionStatusType)
    {

        UserSessions userSessions =
                userSessionRepository.findBySessionIdAndStatus(
                        sessionId,
                        userSessionStatusType
                );

        if(userSessions != null)
        {
            userSessions.setLogoutTime(Instant.now());

            userSessions.setStatus(
                    UserSessionStatusType.LOGGED_OUT
            );

            userSessionRepository.save(userSessions);
        }
    }
}