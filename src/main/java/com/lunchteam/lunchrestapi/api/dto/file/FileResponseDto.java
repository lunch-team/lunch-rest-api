package com.lunchteam.lunchrestapi.api.dto.file;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileResponseDto {

    // originalFileName
    private String fileName;

    // storedFileName
    private String fileKey;

    private String fileExtension;

    private String fileSize;

    // mime-type
    private String fileType;

    private Long targetId;

    private LocalDateTime insertDateTime;

    private LocalDateTime updateDateTime;

    // for download file.
    private Resource resource;

    private static FileResponseDto allOf(FileResult file) {
        return FileResponseDto.builder()
            .fileName(file.getOriginalFileName())
            .fileKey(file.getStoredFileName())
            .fileExtension(file.getFileExtension())
            .fileType(file.getFileType())
            .targetId(file.getTargetId())
            .insertDateTime(file.getInsertDateTime())
            .updateDateTime(file.getUpdateDateTime())
            .build();
    }

    public static List<FileResponseDto> listOf(List<FileResult> files) {
        if (!files.isEmpty()) {
            List<FileResponseDto> result = new ArrayList<>();
            for (FileResult entity : files) {
                result.add(FileResponseDto.allOf(entity));
            }
            return result;
        } else {
            return null;
        }
    }
}
