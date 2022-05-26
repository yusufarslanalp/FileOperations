package com.example.springsecuritybasic.repository;

import com.example.springsecuritybasic.models.FileInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<FileInfo, Long> {




}
