package com.shashwat.blog_app_youtube.Services.implementation;

import com.shashwat.blog_app_youtube.Services.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileServiceImple implements FileService {
    @Override
    public String uploadImage(String path, MultipartFile file) throws IOException {

        //FILE NAME
        String name = file.getOriginalFilename();
        //abc.png

        // generating random name for our file and concatenating its extension .png
        String randomID= UUID.randomUUID().toString(); // generates a random id
        String newFileName=randomID.concat(name.substring(name.lastIndexOf(".")));

//        //Checking file format
//        //TODO: incorrect code
//        if(!file.getContentType().contentEquals(MediaType.IMAGE_JPEG_VALUE)){
//            throw new UnsupportedMediaTypeStatusException("FILE FORMAT INVALID");
//        }

        //FULL PATH      ->path/abc.png
        String filePath=path + File.separator+newFileName;


        //Create Folder If Not Created
        File f = new File(path);
        if(!f.exists()){
            f.mkdir();
        }

        //TODO: READ
        //FileCopy (source,target, option) target take path object & we get path obj by .get() method by passing path into it
        Files.copy(file.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);

        return  newFileName;
    }

    @Override
    public InputStream getResource(String path, String fileName) throws FileNotFoundException {
                            // means adding /
        String fullPath=path + File.separator + fileName;
        InputStream is= new FileInputStream(fullPath);
                    //OR
        //db logic to return inputstream

        return is;
    }
}
