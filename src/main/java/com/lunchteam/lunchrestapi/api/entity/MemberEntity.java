package com.lunchteam.lunchrestapi.api.entity;

import com.lunchteam.lunchrestapi.security.dto.Authority;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@NoArgsConstructor
@Table(name = "lunch_member")
@Entity
public class MemberEntity {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    public Long id;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String loginId;

    private String name;

    private String password;

    private String delYn;

    private int useCount;

    private String groupId;

    @CreationTimestamp
    private LocalDateTime insertDateTime;

    @UpdateTimestamp
    private LocalDateTime updateDateTime;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    @Builder(builderClassName = "UserLoginInfo", builderMethodName = "UserLoginInfo")
    public MemberEntity(
        Long id,
        String loginId,
        String name
    ) {
        this.id = id;
        this.loginId = loginId;
        this.name = name;
    }

    /**
     * 회원 가입 빌더
     *
     * @param email     email
     * @param name      사용자 이름
     * @param loginId   로그인 아이디
     * @param password  비밀번호
     * @param delYn     'N'
     * @param useCount  0
     * @param authority {@link Authority}.ROLE_USER
     */
    @Builder(builderClassName = "UserSignUp", builderMethodName = "UserSignUp")
    public MemberEntity(
        String email,
        String loginId,
        String name,
        String password,
        String delYn,
        int useCount,
        Authority authority
    ) {
        this.email = email;
        this.loginId = loginId;
        this.name = name;
        this.password = password;
        this.delYn = delYn;
        this.useCount = useCount;
        this.authority = authority;
    }
}
