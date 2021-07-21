package com.lunchteam.lunchrestapi.api.repository;

import com.lunchteam.lunchrestapi.security.dto.RefreshTokenDto;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenDto, Long> {

    Optional<RefreshTokenDto> findByKeyToken(String keyToken);
}
