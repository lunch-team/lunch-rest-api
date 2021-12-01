package com.lunchteam.lunchrestapi.api.repository;

import com.lunchteam.lunchrestapi.api.dto.file.FileRequestDto;
import com.lunchteam.lunchrestapi.api.dto.file.FileResult;
import com.lunchteam.lunchrestapi.api.entity.FileEntity;
import com.lunchteam.lunchrestapi.api.entity.QFileEntity;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class FileRepositorySupport extends QuerydslRepositorySupport {

    private final JPAQueryFactory queryFactory;

    QFileEntity qFileEntity = QFileEntity.fileEntity;

    public FileRepositorySupport(JPAQueryFactory queryFactory) {
        super(FileEntity.class);
        this.queryFactory = queryFactory;
    }

    @Transactional
    public List<FileResult> getFileList(FileRequestDto fileDto) {
        JPAQuery<FileResult> query = queryFactory
            .select(
                Projections.fields(
                    FileResult.class,
                    qFileEntity.id,
                    qFileEntity.targetId,
                    qFileEntity.storedFileName,
                    qFileEntity.originalFileName,
                    qFileEntity.fileExtension,
                    qFileEntity.fileSize,
                    qFileEntity.fileType,
                    qFileEntity.insertDateTime,
                    qFileEntity.updateDateTime
                )
            ).from(qFileEntity)
            .where(qFileEntity.useYn.eq("Y").and(qFileEntity.targetId.eq(fileDto.getTargetId())));
        // 항상 최신순
        query.orderBy(qFileEntity.insertDateTime.desc());

        return query.fetch();
    }

    @Transactional
    public long deleteFile(FileRequestDto fileRequestDto) {
        return queryFactory.update(qFileEntity)
            .set(qFileEntity.updateDateTime, LocalDateTime.now())
            .set(qFileEntity.useYn, "N")
            .where(qFileEntity.storedFileName.eq(fileRequestDto.getFileKey()))
            .execute();
    }
}
