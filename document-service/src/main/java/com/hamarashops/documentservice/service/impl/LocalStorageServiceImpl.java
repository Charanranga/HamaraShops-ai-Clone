package com.hamarashops.documentservice.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.hamarashops.documentservice.service.StorageService;

@Service
@Profile("!cloud")
public class LocalStorageServiceImpl implements StorageService {

	private static final Logger logger = LoggerFactory.getLogger(LocalStorageServiceImpl.class);

	@Value("${file.upload-dir:C:/CandidateResumes}")
	private String uploadDir;

	@Override
	public String store(MultipartFile file) throws IOException {
		logger.info("Storing file locally: {}", file.getOriginalFilename());
		File dir = new File(uploadDir);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		String uniqueName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
		Path targetPath = Paths.get(uploadDir).resolve(uniqueName);
		Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
		logger.info("File stored successfully at: {}", targetPath.toAbsolutePath());
		return uniqueName;
	}
}
