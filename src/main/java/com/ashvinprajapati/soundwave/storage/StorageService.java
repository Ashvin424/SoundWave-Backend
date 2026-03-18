package com.ashvinprajapati.soundwave.storage;

import org.springframework.web.multipart.MultipartFile;

// This interface defines the contract for a storage service that can upload files and return their URLs.
public interface StorageService {
    String uploadFile(MultipartFile file, String folder);
}
