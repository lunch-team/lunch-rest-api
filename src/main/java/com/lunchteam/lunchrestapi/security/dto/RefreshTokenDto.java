package com.lunchteam.lunchrestapi.security.dto;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Table(name = "refresh_token")
@Entity
public class RefreshTokenDto {
    /**
     * Member ID 값
     */
    @Id
    private String keyToken;
    /**
     * Refresh Token String
     */
    private String value;

    @Builder
    public RefreshTokenDto(String key, String value) {
        this.keyToken = key;
        this.value = value;
    }

    public RefreshTokenDto updateValue(String token) {
        this.value = token;
        return this;
    }
}
