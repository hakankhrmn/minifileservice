package com.cyangate.minifileservice.controller;

import com.cyangate.minifileservice.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
@CrossOrigin
public class FileController {

	private final FileService fileService;

	@PostMapping("/upload")
	@PreAuthorize("hasAnyAuthority('ADMIN_USER','REGULAR_USER')")
	public ResponseEntity<String> uploadFile(@RequestPart(required = true) MultipartFile file) {
		if (file.isEmpty()) {
			return ResponseEntity.badRequest().body("Dosya boş!");
		}

		if (file.getSize() < 50000) {
			return ResponseEntity.badRequest().body("Dosya en az 50KB olmalıdır!");
		}

		return ResponseEntity.ok(fileService.uploadFile(file));
	}

	@PostMapping("/preview/{filename}")
	@PreAuthorize("hasAnyAuthority('ADMIN_USER','REGULAR_USER')")
	public ResponseEntity<Resource> previewFile(@PathVariable String filename) {
		Resource file = fileService.previewFile(filename);
		if (file == null) {
			return ResponseEntity.badRequest().body(null);
		}
		return ResponseEntity.ok()
				.header("Content-Type", "image/jpeg")
				.body(file);
	}

	@PostMapping("/download/previewFile/{filename}")
	@PreAuthorize("hasAnyAuthority('ADMIN_USER','REGULAR_USER')")
	public ResponseEntity<Resource> downloadPreviewFile(@PathVariable String filename) {
		Resource file = fileService.downloadFile(filename, true);
		if (file == null) {
			return ResponseEntity.badRequest().body(null);
		}
		return ResponseEntity.ok()
				.header("Content-Type", "image/jpeg")
				.body(file);
	}

	@PostMapping("/download/originalFile/{filename}")
	@PreAuthorize("hasAnyAuthority('ADMIN_USER','REGULAR_USER')")
	public ResponseEntity<Resource> downloadOriginalFile(@PathVariable String filename) {
		Resource file = fileService.downloadFile(filename, false);
		if (file == null) {
			return ResponseEntity.badRequest().body(null);
		}
		return ResponseEntity.ok()
				.header("Content-Type", MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_GIF_VALUE)
				.body(file);
	}

	@PostMapping("/export")
	@PreAuthorize("hasAnyAuthority('ADMIN_USER','REGULAR_USER')")
	public ResponseEntity<Resource> exportFiles(@RequestParam("fileNames") List<String> fileNames) throws IOException {
		Resource zipFile = fileService.exportFiles(fileNames);
		if (zipFile == null) {
			return ResponseEntity.badRequest().body(null);
		}
		return ResponseEntity.ok()
				.header("Content-Type", "application/zip")
				.header("Content-Disposition", "attachment; filename=\"exportedFiles.zip\"")
				.body(zipFile);
	}



	@GetMapping("/names")
	@PreAuthorize("hasAnyAuthority('ADMIN_USER','REGULAR_USER')")
	public ResponseEntity<List<String>> getFileNames() {
		List<String> fileNames = fileService.getAllFileNames();
		if (CollectionUtils.isEmpty(fileNames)) {
			return ResponseEntity.badRequest().body(fileNames);
		}
		return ResponseEntity.ok(fileNames);
	}

	@DeleteMapping("/{filename}")
	@PreAuthorize("hasAnyAuthority('ADMIN_USER')")
	public ResponseEntity<String> deleteFile(@PathVariable String filename) {
		String response = fileService.deleteFile(filename);
		if (response == null) {
			return ResponseEntity.internalServerError().body("Dosya silme sırasında hata oluştu!");
		}
		return ResponseEntity.ok(response);
	}




}
