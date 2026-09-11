package com.hamarashops.documentservice.service.impl;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.BucketInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.hamarashops.documentservice.service.StorageService;

@Service
@Profile("cloud")
public class GcsStorageServiceImpl implements StorageService {

	private static final Logger logger = LoggerFactory.getLogger(GcsStorageServiceImpl.class);

	@Value("${gcp.storage.bucket-name}")
	private String bucketName;

	@Override
	public String store(MultipartFile file) throws IOException {
		logger.info("Storing file in Google Cloud Storage: {}", file.getOriginalFilename());
		Storage storage = StorageOptions.getDefaultInstance().getService();
		
		// Create the bucket only if it does not already exist
		if (storage.get(bucketName) == null) {
			logger.info("Bucket {} not found. Creating it dynamically...", bucketName);
			storage.create(BucketInfo.of(bucketName));
			logger.info("Bucket {} created successfully.", bucketName);
		}
		
		String uniqueName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
		BlobId blobId = BlobId.of(bucketName, uniqueName);
		BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
				.setContentType(file.getContentType())
				.build();
				
		storage.create(blobInfo, file.getBytes());
		
		String publicUrl = String.format("https://storage.googleapis.com/%s/%s", bucketName, uniqueName);
		logger.info("File uploaded successfully to GCS. Public URL: {}", publicUrl);
		return publicUrl;
	}
}
