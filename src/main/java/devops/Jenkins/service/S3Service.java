package devops.Jenkins.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

@Service
public class S3Service {

    private String bucketName = "photos-devops";

    Regions region = Regions.US_EAST_1;

    public void uploadPhotoToS3(InputStream inputStream, String fileName)
            throws IOException, AmazonServiceException, SdkClientException {
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withRegion(region).build();
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("image.jpeg");
        metadata.setContentLength(inputStream.available());

        PutObjectRequest request = new PutObjectRequest(bucketName, fileName, inputStream, metadata);
        s3Client.putObject(request);
    }

    public void deletePhotoFromS3(String fileName)
            throws AmazonServiceException, SdkClientException {
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withRegion(region).build();

        s3Client.deleteObject(new DeleteObjectRequest(bucketName, fileName));
    }

    public String extractFilenameFromUrl(String url) {
        int lastIndexOf = url.lastIndexOf("/");
        return url.substring(lastIndexOf + 1);
    }


    public String getPhotoUrlFromS3(String fileName)
            throws AmazonServiceException, SdkClientException {
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withRegion(region).build();

        GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest(bucketName, fileName);
        urlRequest.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)); // Örneğin, URL'nin 1 saat geçerli olmasını sağlar

        return s3Client.generatePresignedUrl(urlRequest).toString();
    }

}
