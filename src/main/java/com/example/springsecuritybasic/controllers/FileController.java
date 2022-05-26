package com.example.springsecuritybasic.controllers;

import com.example.springsecuritybasic.UserRepository;
import com.example.springsecuritybasic.models.AuthenticationRequest;
import com.example.springsecuritybasic.models.AuthenticationResponse;
import com.example.springsecuritybasic.models.FileInfo;
import com.example.springsecuritybasic.models.MyUser;
import com.example.springsecuritybasic.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@RestController
public class FileController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileRepository fileRepository;


    @RequestMapping(value = "/upload/file", method = RequestMethod.POST)
    public ResponseEntity<?> uploadFile( Authentication authentication, @RequestParam("file") MultipartFile file ){ //

        System.out.println( authentication.getName() );
        MyUser user = userRepository.findByUsername( authentication.getName() );

        try {
            String appPath = new File(".").getCanonicalPath();
            String filePath = appPath + "\\" + "user-files" + "\\" +  user.getId();
            String fileExt = getFileExtension( file.getOriginalFilename() );
            if( !isAcceptedContent( fileExt ) ){
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE ).body( "Not allowed type" );
            }
            Files.copy(file.getInputStream(), Paths.get( filePath + "\\" + file.getOriginalFilename() ) );
            FileInfo fileInfo = new FileInfo( user.getId(), file.getOriginalFilename(),
                    filePath, fileExt ,file.getSize() );
            fileRepository.save( fileInfo );

        } catch ( Exception e) {
            e.printStackTrace();
            return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR ).body( e.getMessage() );
        }
        return ResponseEntity.ok( null );/**/
    }

    private String getFileExtension( String name ) {
        int lastIndexOf = name.lastIndexOf(".")+1;
        if (lastIndexOf == -1) {
            return ""; // empty extension
        }
        return name.substring(lastIndexOf);
    }

    //png, jpeg, jpg, docx, pdf, xlsx
    private boolean isAcceptedContent( String fileExt ){
        if( fileExt.contains("png") ) return true;
        if( fileExt.contains("jpeg")  ) return true;
        if( fileExt.contains("jpg")  ) return true;
        if( fileExt.contains("docx")  ) return true;
        if( fileExt.contains("pdf")  ) return true;
        if( fileExt.contains("xlsx")  ) return true;
        return false;

    }

}
