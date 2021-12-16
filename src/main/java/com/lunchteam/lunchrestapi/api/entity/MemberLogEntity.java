package com.lunchteam.lunchrestapi.api.entity;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Getter
@NoArgsConstructor
@Table(name = "lunch_access_log")
@Entity
public class MemberLogEntity {

    @Id
    @GeneratedValue
    private Long id;

    private Long memberId;

    private String loginId;

    private String name;

    private String ip;

    private String userAgent;

    private String requestUri;

    @CreationTimestamp
    private LocalDateTime insertDateTime;

    @Builder(builderClassName = "AddMemberLog", builderMethodName = "AddMemberLog")
    public MemberLogEntity(
        String ip,
        Long memberId,
        String loginId,
        String name,
        String userAgent,
        String requestUri
    ) {
        this.ip = ip;
        this.memberId = memberId;
        this.loginId = loginId;
        this.name = name;
        this.userAgent = userAgent;
        this.requestUri = requestUri;
    }
}
