package com.lunchteam.lunchrestapi.api.dto.file;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Data
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class FileRequestDto {

    private Long memberId;
    private Long targetId;
    private String fileKey;
    private MultipartFile[] files;
}
