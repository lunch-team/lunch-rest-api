package com.lunchteam.lunchrestapi.api.dto.menu;

import com.lunchteam.lunchrestapi.api.dto.file.FileResult;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class MenuReviewResult {

    private Long id;
    private Long menuId;
    private Long insertMemberId;
    private int star;
    private String contents;
    private List<FileResult> files;
    private LocalDateTime insertDateTime;

    public void setFiles(List<FileResult> files) {
        this.files = files;
    }
}
