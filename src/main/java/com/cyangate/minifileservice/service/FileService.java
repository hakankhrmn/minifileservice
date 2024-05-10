package com.cyangate.minifileservice.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileService {
	String uploadFile(MultipartFile file);
	Resource previewFile(String fileName);
	List<String> getAllFileNames();
	Resource downloadFile(String fileName, boolean isPreview);
	Resource exportFiles(List<String> fileNames) throws IOException;
	String deleteFile(String fileName);

}
