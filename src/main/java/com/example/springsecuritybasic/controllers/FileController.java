package com.example.springsecuritybasic.controllers;

import com.example.springsecuritybasic.repository.UserRepository;
import com.example.springsecuritybasic.models.FileInfo;
import com.example.springsecuritybasic.models.MyUser;
import com.example.springsecuritybasic.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("file")
public class FileController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileRepository fileRepository;


    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public ResponseEntity<?> fileUpload( Authentication authentication, @RequestParam("file") MultipartFile file ){ //
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
            return new ResponseEntity<FileInfo>( fileInfo, HttpStatus.OK );

        } catch ( Exception e) {
            e.printStackTrace();
            return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR ).body( e.getMessage() );
        }
    }


    @RequestMapping(value = "/info/all", method = RequestMethod.GET)
    public ResponseEntity<?> getFileInfoAll( Authentication authentication ){ //
        MyUser user = userRepository.findByUsername( authentication.getName() );

        List<FileInfo> ls = fileRepository.findByUserId( user.getId() );

        return new ResponseEntity<List<FileInfo>>( ls, HttpStatus.OK );

    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<?> getFile( Authentication authentication, Long fileId ){ //
        MyUser user = userRepository.findByUsername( authentication.getName() );

        FileInfo fileInfo = fileRepository.findById( fileId ).get();
        if( fileInfo.getUserId() == user.getId() )
        {
            String fullPath = fileInfo.getPath() + "\\" + fileInfo.getName();
            File file = new File( fullPath );
            try {
                byte[] bytes = Files.readAllBytes( file.toPath());
                return new ResponseEntity<byte[]>( bytes, HttpStatus.OK  );
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return new ResponseEntity<Error>( HttpStatus.UNAUTHORIZED );
    }

    @RequestMapping(value = "/rename", method = RequestMethod.PUT)
    public ResponseEntity<?> getFile( Authentication authentication, Long fileId, String newFileName ){ //
        MyUser user = userRepository.findByUsername( authentication.getName() );

        FileInfo fileInfo = fileRepository.findById( fileId ).get();
        if( fileInfo.getUserId() == user.getId() )
        {
            String fullPath = fileInfo.getPath() + "\\" + fileInfo.getName();
            String newFullPath = fileInfo.getPath() + "\\" + newFileName;
            File file = new File( fullPath );
            File file2 = new File(newFullPath);

            if (file2.exists()){
                return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR ).
                        body( "Error: File exist!" );
            }
            if ( file.renameTo(file2) ) {
                fileInfo.setName( newFileName );
                fileRepository.save( fileInfo );
                return new ResponseEntity<FileInfo>( fileInfo, HttpStatus.OK );
            }
            else{
                return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR ).
                        body( "Error: File can not renamed!" );
            }

        }
        return new ResponseEntity<Error>( HttpStatus.UNAUTHORIZED );



    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteFile( Authentication authentication, Long fileId ){ //
        MyUser user = userRepository.findByUsername( authentication.getName() );

        FileInfo fileInfo = fileRepository.findById( fileId ).get();
        if( fileInfo.getUserId() == user.getId() )
        {
            String fullPath = fileInfo.getPath() + "\\" + fileInfo.getName();
            File file = new File( fullPath );
            file.delete();
            fileRepository.deleteById( fileInfo.getId() );
            return new ResponseEntity<FileInfo>( fileInfo, HttpStatus.OK );
        }
        return new ResponseEntity<Error>( HttpStatus.UNAUTHORIZED );
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
