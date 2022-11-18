package com.jonny.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Demo {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String label;

    private String value;
}
