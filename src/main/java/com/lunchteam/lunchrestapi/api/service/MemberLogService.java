package com.lunchteam.lunchrestapi.api.service;

import com.lunchteam.lunchrestapi.api.entity.MemberEntity;
import com.lunchteam.lunchrestapi.api.entity.MemberLogEntity;
import com.lunchteam.lunchrestapi.api.repository.MemberLogRepository;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberLogService {

    private final MemberLogRepository memberLogRepository;

    @Transactional
    public void logging(MemberEntity member, HttpServletRequest request) {
        try {
            MemberLogEntity param = member.toLogging(request);
            memberLogRepository.save(param);
        } catch (Exception e) {
            log.warn(
                "Can't Logging: "
                    + request.getHeader("X-Real-IP")
                    + ", URI: " + request.getRequestURI()
                    + ", Error: " + e.getMessage()
            );
        }

        log.debug("");
    }
}

