package com.example.blog.services.impl;

import com.example.blog.services.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {
    @Override
    public String uploadImage(String path, MultipartFile file) throws IOException {
        String name=file.getOriginalFilename();       //file original name

        //generate random name for file-> to overcome multiple upload of same file
        String randomId= UUID.randomUUID().toString();
        String randomFilename=randomId.concat(name.substring(name.lastIndexOf(".")));

        String filePath=path+ File.separator+randomFilename;   //full path

        File f=new File(path);

        //create folder if not created
        if(!f.exists()){
            f.mkdir();
        }

        //file copy
        Files.copy(file.getInputStream(), Paths.get(filePath));
        return randomFilename;
    }

    @Override
    public InputStream getResource(String path, String fileName) throws FileNotFoundException {
        String fullPath=path+File.separator+fileName;
        InputStream is=new FileInputStream(fullPath);
        return is;
    }
}
