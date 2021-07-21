package com.lunchteam.lunchrestapi.api.entity;

import com.lunchteam.lunchrestapi.security.dto.Authority;
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

@Getter
@NoArgsConstructor
@Table(name = "lunch_member")
@Entity
public class MemberEntity {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    public Long id;

    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    @Builder
    public MemberEntity(String email, String password, Authority authority) {
        this.email = email;
        this.password = password;
        this.authority = authority;
    }
}
