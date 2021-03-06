package com.lunchteam.lunchrestapi.api.controller;

import com.google.common.net.HttpHeaders;
import com.lunchteam.lunchrestapi.api.dto.file.FileRequestDto;
import com.lunchteam.lunchrestapi.api.dto.file.FileResponseDto;
import com.lunchteam.lunchrestapi.api.entity.FileEntity;
import com.lunchteam.lunchrestapi.api.response.BasicResponse;
import com.lunchteam.lunchrestapi.api.service.FileService;
import com.lunchteam.lunchrestapi.handler.ResultHandler;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    /**
     * 파일 업로드
     *
     * @param fileRequestDto files
     * @return fileEntity
     */
    @PostMapping("/upload")
    public ResponseEntity<? extends BasicResponse> uploadFiles(
        FileRequestDto fileRequestDto
    ) {
        try {
            log.debug("Upload Params: " + fileRequestDto.toString());
            HashMap<String, Long> params = new HashMap<>();
            params.put("targetId", fileRequestDto.getTargetId());
            params.put("memberId", fileRequestDto.getMemberId());
            params.put("groupId", System.currentTimeMillis());

            List<FileEntity> list = new ArrayList<>();
            for (int i = 0; i < fileRequestDto.getFiles().length; i++) {
                params.put("fileNo", (long) i);
                list.add(fileService.storeFile(fileRequestDto.getFiles()[i], params));
            }

            if (list.isEmpty()) {
                log.warn("File Upload result is empty.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            } else {
                for (FileEntity file : list) {
                    log.debug("File Upload is Complete: " + file.getOriginalFileName());
                }
                return ResultHandler.setResult(list, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 다운로드 파일
     *
     * @param fileRequestDto fileKey
     * @param request        request
     * @return Resource
     */
    @PostMapping("/downloadFile")
    public ResponseEntity<Resource> downloadFile(
        @RequestBody FileRequestDto fileRequestDto, HttpServletRequest request
    ) {
        try {
            Map<String, Object> resultQry = fileService.loadFileAsResource(
                fileRequestDto.getFileKey());
            Resource resource = (Resource) resultQry.get("resource");
            String fileName
                = URLEncoder.encode((String) resultQry.get("fileName"), "utf-8");
            String contentType
                = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
            if (contentType == null) {
                contentType = "application/octet-stream";
            }
            return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(
                    HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"" + fileName + "\""
                )
                .body(resource);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 파일 리스트 조회
     *
     * @param fileRequestDto targetId
     * @return List
     */
    @PostMapping("/getFileList")
    public ResponseEntity<? extends BasicResponse> getFileList(
        @RequestBody FileRequestDto fileRequestDto
    ) {
        try {
            List<FileResponseDto> result =
                FileResponseDto.listOf(fileService.getFileListByTargetId(fileRequestDto));
            return ResultHandler.setResult(result, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 파일 삭제
     *
     * @param fileRequestDto id
     * @return 200
     */
    @PostMapping("/delete")
    public ResponseEntity<? extends BasicResponse> deleteFile(
        @RequestBody FileRequestDto fileRequestDto
    ) {
        try {
            if (fileService.fileDelete(fileRequestDto)) {
                log.debug("Delete File: " + fileRequestDto.getFileKey());
                return ResponseEntity.status(HttpStatus.OK).build();
            } else {
                log.warn("Not Found File Id: " + fileRequestDto.getFileKey());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
