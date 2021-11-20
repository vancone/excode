package com.vancone.excode.generator.entity;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author Tenton Lien
 * @since 2021/11/20
 */
@Data
public class ResponsePage<T> {
    private List<T> list;
    private int pageNo;
    private int pageSize;
    private long totalCount;
    private int totalPage;

    public ResponsePage() {}

    public ResponsePage(Page page) {
        list = page.getContent();
        pageNo = page.getNumber() + 1;
        pageSize = page.getSize();
        totalCount = page.getTotalElements();
        totalPage = page.getTotalPages();
    }
}
