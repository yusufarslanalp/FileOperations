package com.example.springsecuritybasic.models;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;

@JsonSerialize
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }
}

