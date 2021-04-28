package com.example.model;


import lombok.Data;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

@Data
public class Catalog implements Serializable {


    @Id
    private int id;

    private String catalog;
}
