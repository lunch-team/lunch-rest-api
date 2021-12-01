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
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@NoArgsConstructor
@Table(name = "lunch_file")
@Entity
public class FileEntity {

    @Id
    @GeneratedValue
    private Long id;

    private Long targetId;

    private String fileType;

    private String storedFilePath;

    private String storedFileName;

    private String originalFileName;

    private String fileExtension;

    private Long fileSize;

    private Long insertMemberId;

    private String useYn;

    @CreationTimestamp
    private LocalDateTime insertDateTime;

    @UpdateTimestamp
    private LocalDateTime updateDateTime;

    @Builder(builderClassName = "StoreFile", builderMethodName = "StoreFile")
    public FileEntity(
        Long targetId,
        Long memberId,
        String storedFileName,
        String storedFilePath,
        String originalFileName,
        Long fileSize,
        String fileType,
        String fileExtension
    ) {
        this.insertMemberId = memberId;
        this.targetId = targetId;
        this.storedFileName = storedFileName;
        this.storedFilePath = storedFilePath;
        this.originalFileName = originalFileName;
        this.fileSize = fileSize;
        this.fileType = fileType;
        this.fileExtension = fileExtension;
        this.useYn = "Y";
    }
}
