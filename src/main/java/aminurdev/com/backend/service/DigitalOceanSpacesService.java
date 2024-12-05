package aminurdev.com.backend.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import aminurdev.com.backend.webapp.config.DigitalOceanSpacesConfig;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.File;
import java.io.IOException;
import java.net.URI;

@Service
public class DigitalOceanSpacesService {

    private static final Logger logger = LoggerFactory.getLogger(DigitalOceanSpacesService.class);

    private final S3Client s3Client;
    private final DigitalOceanSpacesConfig config;

    public DigitalOceanSpacesService(DigitalOceanSpacesConfig config) {
        this.config = config;

        AwsBasicCredentials credentials = AwsBasicCredentials.create(
                config.getAccessKey(), config.getSecretKey());

        this.s3Client = S3Client.builder()
                .region(Region.of(config.getRegion()))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .endpointOverride(URI.create(config.getEndpoint()))
                .build();
    }

    public String uploadImage(MultipartFile file, String folderName, String objectKey) throws IOException {
        // Convert MultipartFile to File
        File tempFile = File.createTempFile("upload-", file.getOriginalFilename());
        file.transferTo(tempFile); // Transfer the contents to the temporary file

        try {
            String fullPath = folderName + "/" + objectKey;

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(config.getBucketName())
                    .key(fullPath)
                    .acl("public-read")
                    .build();

            PutObjectResponse response = s3Client.putObject(putObjectRequest, tempFile.toPath());

            return config.getEndpoint() + "/" + config.getBucketName() + "/" + fullPath;

        } finally {
            // Delete the temporary file after the upload
            tempFile.delete();
        }
    }

    public boolean deleteImage(String objectKey) {
        try {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(config.getBucketName())
                    .key(objectKey)  // full path, e.g., folderName/image.jpg
                    .build();

            DeleteObjectResponse deleteResponse = s3Client.deleteObject(deleteObjectRequest);

            logger.info("Image deleted successfully from DigitalOcean Spaces: {}", objectKey);
            return true;  // Indicate successful deletion

        } catch (Exception e) {
            logger.error("Failed to delete image from DigitalOcean Spaces: {}", e.getMessage());
            return false;  // Indicate failure
        }
    }

}
