package com.example.model;

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

    private Date date;

    private String category;

    private String authorName;

    private String url;

    private String thumbnailPicS;

    private String thumbnailPicS02;

    private String thumbnailPicS03;

    private String isContent;
}
