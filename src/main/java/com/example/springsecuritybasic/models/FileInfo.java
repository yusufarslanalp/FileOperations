package com.example.springsecuritybasic.models;

import javax.persistence.*;

@Entity
@Table(name = "file")
public class FileInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private Long userId;
    @Column
    private String name;
    @Column
    private String path;
    @Column
    private String extension;
    @Column
    private Long size;

    public FileInfo() {
    }

    public FileInfo(Long userId, String name, String path, String extension, Long size) {
        this.userId = userId;
        this.name = name;
        this.path = path;
        this.extension = extension;
        this.size = size;
    }

}

