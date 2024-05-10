package com.cyangate.minifileservice.service;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class FileServiceImpl implements FileService {

	@Override
	public String uploadFile(MultipartFile file) {

		String fileUploadStatus;

		try {
			String fileName = File.separator + file.getOriginalFilename();
			String filePath = "uploads/" + File.separator + fileName;

			File uploadsDir = new File("uploads/");
			if (!uploadsDir.exists() || !uploadsDir.isDirectory()) {
				uploadsDir.mkdirs();
			}

			FileOutputStream fout = new FileOutputStream(filePath);
			fout.write(file.getBytes());

			fout.close();

			fileUploadStatus = "Dosya başarıyla yüklendi: " + fileName;

		} catch (IOException e) {
			e.printStackTrace();
			return "Dosya yükleme sırasında hata oluştu!";
		}
		return fileUploadStatus;
	}

	@Override
	public Resource previewFile(String fileName) {
		Resource file = new FileSystemResource("uploads/" + fileName);
		if (!file.exists() || !file.isFile()) {
			return null;
		}

		try {
			String previewFileName = fileName + "_preview.jpg";
			String previewFilePath = "uploads/" + previewFileName;

			String command = "magick convert " + file.getFile().getPath() + " -define jpeg:size=200x200 -thumbnail 128x128> " + previewFilePath;

			Process process = Runtime.getRuntime().exec(command);
			process.waitFor();

			if (process.exitValue() == 0) {
				Resource previewResource = new FileSystemResource(previewFilePath);
				return previewResource;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<String> getAllFileNames() {
		try {
			File uploadsDir = new File("uploads");
			if (!uploadsDir.exists() || !uploadsDir.isDirectory()) {
				return new ArrayList<>();
			}

			List<String> fileNames = new ArrayList<>();
			for (File file : uploadsDir.listFiles()) {
				if (!file.isDirectory()) {
					fileNames.add(file.getName());
				}
			}

			return fileNames;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Resource downloadFile(String fileName, boolean isPreview) {

		Resource file = new FileSystemResource("uploads/" + fileName);
		if (!file.exists() || !file.isFile()) {
			return null;
		}

		if (isPreview) {
			String previewFileName = fileName + "_preview.jpg";

			Resource previewFile = new FileSystemResource("uploads/" + previewFileName);
			if (previewFile.exists() && previewFile.isFile()) {
				return previewFile;
			}

			previewFile = previewFile(fileName);

			return previewFile;

		} else {
			return file;
		}

	}

	@Override
	public Resource exportFiles(List<String> fileNames) throws IOException {

		final FileOutputStream fos = new FileOutputStream("uploads/exportedFiles.zip");
		ZipOutputStream zipOut = new ZipOutputStream(fos);

		for (String fileName : fileNames) {
			File fileToZip = new File("uploads/" + fileName);
			FileInputStream fis = new FileInputStream(fileToZip);
			ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
			zipOut.putNextEntry(zipEntry);

			byte[] bytes = new byte[1024];
			int length;
			while((length = fis.read(bytes)) >= 0) {
				zipOut.write(bytes, 0, length);
			}
			fis.close();
		}

		zipOut.close();
		fos.close();

		Resource file = new FileSystemResource("uploads/exportedFiles.zip");
		if (!file.exists() || !file.isFile()) {
			System.out.println("başaramadık");
			return null;
		}
		return file;

	}

	@Override
	public String deleteFile(String fileName) {
		File file = new File("uploads/" + fileName);
		if (!file.exists() || !file.isFile()) {
			return null;
		}

		if (file.delete()) {
			return "Dosya başarıyla silindi: " + fileName;
		} else {
			return null;
		}
	}
}
