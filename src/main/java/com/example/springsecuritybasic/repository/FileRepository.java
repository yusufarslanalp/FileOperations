package com.example.springsecuritybasic.repository;

import com.example.springsecuritybasic.models.FileInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<FileInfo, Long> {

    List<FileInfo> findByUserId( Long userId );

    //FileInfo findById( Long id );


}
