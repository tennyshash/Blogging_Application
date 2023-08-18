package com.shashwat.blog_app_youtube.Services;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

public interface FileService {

    String uploadImage(String path, MultipartFile file) throws IOException ;

    InputStream getResource(String path, String fileName) throws FileNotFoundException ;
}
