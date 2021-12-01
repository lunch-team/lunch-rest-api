package com.lunchteam.lunchrestapi.api.dto.file;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FileResult {

    private Long id;
    private Long targetId;
    private Long insertMemberId;

    private Long fileSize;
    private String fileExtension;
    private String fileType;
    private String originalFileName;
    private String storedFileName;
    private String storedFile_path;

    private LocalDateTime insertDateTime;
    private LocalDateTime updateDateTime;

    private String useYn;
}
