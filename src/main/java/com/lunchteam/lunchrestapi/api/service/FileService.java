package com.lunchteam.lunchrestapi.api.service;

import com.lunchteam.lunchrestapi.api.dto.file.FileRequestDto;
import com.lunchteam.lunchrestapi.api.dto.file.FileResult;
import com.lunchteam.lunchrestapi.api.entity.FileEntity;
import com.lunchteam.lunchrestapi.api.exception.FileDownloadException;
import com.lunchteam.lunchrestapi.api.exception.FileUploadException;
import com.lunchteam.lunchrestapi.api.repository.FileRepository;
import com.lunchteam.lunchrestapi.api.repository.FileRepositorySupport;
import com.lunchteam.lunchrestapi.property.FileProperties;
import com.lunchteam.lunchrestapi.util.RandomUtil;
import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;
    private final FileRepositorySupport fileRepositorySupport;
    private final Path fileLocation;

    @Autowired
    public FileService(
        FileProperties properties,
        FileRepository fileRepository,
        FileRepositorySupport fileRepositorySupport) {
        this.fileLocation = Paths.get(properties.getDirectory())
            .toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileLocation);
        } catch (Exception e) {
            throw new FileUploadException("Could not create directory for file uploadeing.", e);
        }
        this.fileRepository = fileRepository;
        this.fileRepositorySupport = fileRepositorySupport;
    }

    public void downloadFile(
        HttpServletResponse response,
        String storedFileName,
        String originalFileName) throws IOException {

        String realFileName;

        File file = new File(fileLocation + File.separator + storedFileName);

        if (!file.exists()) {
            throw new FileNotFoundException(storedFileName);
        }

        if (!file.isFile()) {
            throw new FileNotFoundException(storedFileName);
        }

        int fSize = (int) file.length();

        if (fSize > 0) {
            BufferedInputStream bis = null;

            try {
                bis = new BufferedInputStream(new FileInputStream(file));

                String mimetype = "application/octet-stream";
                realFileName = URLEncoder.encode(originalFileName, "UTF-8");
                response.setCharacterEncoding("UTF-8");

                response.setContentType(mimetype);
                response.setHeader("Content-Disposition", "attachment; filename=" + realFileName);
                response.setContentLength(fSize);

                fileCopy(bis, response.getOutputStream());
            } finally {
                resourceClose(bis);
            }
        }
    }


    /**
     * Resource close
     *
     * @param resources resource
     */
    private void resourceClose(Closeable... resources) {
        for (Closeable resource : resources) {
            if (resource != null) {
                try {
                    resource.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Copy the contents of the given InputStream to the given OutputStream. Closes both streams
     * when done.
     *
     * @param in  the stream to copy from
     * @param out the stream to copy to
     * @throws IOException in case of I/O errors
     */
    private void fileCopy(InputStream in, OutputStream out) throws IOException {
        Assert.notNull(in, "No InputStream specified");
        Assert.notNull(out, "No OutputStream specified");
        try {
            // return the stream to copy to
            StreamUtils.copy(in, out);
        } finally {
            try {
                in.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            try {
                out.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * upload File
     *
     * @param paramFile file
     * @return entity
     */
    public FileEntity storeFile(MultipartFile paramFile, HashMap<String, Long> params) {
        String fileName = StringUtils
            .cleanPath(Objects.requireNonNull(paramFile.getOriginalFilename()));

        try {
            if (fileName.contains("..")) {
                throw new FileUploadException("Invalid File Name : " + fileName);
            }

            // File Name Encrypt by UUID
            // File Extension
            String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1);
            // UUID Encrypt and add Extension
            String storedFileName = RandomUtil.getRandomString() + "." + fileExt;

            Path targetLocation = this.fileLocation.resolve(storedFileName);

            Files.copy(paramFile.getInputStream(), targetLocation,
                StandardCopyOption.REPLACE_EXISTING);

            // Store File Data in Database
            FileEntity fileEntity = FileEntity.StoreFile()
                .targetId(params.get("targetId"))
                .memberId(params.get("memberId"))
                .groupId(params.get("groupId"))
                .fileNo(params.get("fileNo").intValue())
                .storedFileName(storedFileName)
                .storedFilePath(fileLocation.toString())
                .originalFileName(fileName)
                .fileSize(paramFile.getSize())
                .fileType(paramFile.getContentType())
                .fileExtension(fileExt)
                .build();

            fileRepository.save(fileEntity);

            return fileEntity;
        } catch (Exception e) {
            throw new FileUploadException(
                "File Name [" + fileName + "] Fail to upload, Please try again.", e);
        }
    }

    /**
     * Load File As resource
     *
     * @param storedFileName Stored File Name
     * @return Result Map.
     */
    public Map<String, Object> loadFileAsResource(String storedFileName) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            Path filePath = this.fileLocation.resolve(storedFileName).normalize();

            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                String streFileName = resource.getFilename();
                FileEntity fileEntity
                    = fileRepository.findByStoredFileNameAndUseYn(streFileName, "Y");

                resultMap.put("resource", resource);
                resultMap.put("fileName", fileEntity.getStoredFileName());
                return resultMap;
            } else {
                throw new FileDownloadException("Could not find file : " + storedFileName);
            }
        } catch (MalformedURLException e) {
            throw new FileDownloadException("Could not find file : " + storedFileName);
        }
    }

    /**
     * File Delete
     *
     * @param file FileKey
     * @return boolean
     */
    public boolean fileDelete(FileRequestDto file) {
        return fileRepositorySupport.deleteFile(file) > 0L;
    }

    /**
     * get File List
     *
     * @param fileRequestDto id
     * @return List
     */
    public List<FileResult> getFileListByTargetId(FileRequestDto fileRequestDto) {
        return fileRepositorySupport.getFileList(fileRequestDto);
    }
}
