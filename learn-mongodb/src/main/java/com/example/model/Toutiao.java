package com.example.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;


@Data
@Document("toutiao")
public class Toutiao implements Serializable {

    @Id
    private String uniquekey;

    @Indexed
    private String title;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date date;

    private String category;

    private String authorName;

    private String url;

    private String thumbnailPicS;

    private String thumbnailPicS02;

    private String thumbnailPicS03;

    private String isContent;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    private Integer status;
}
