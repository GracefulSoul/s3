package gracefulsoul.s3.service;

import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CopyObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Object;

@Service
@RequiredArgsConstructor
public class S3Service {

	private final S3Client s3Client;

	public List<S3Object> listObjects(String bucket, int size) {
		try {
			return this.s3Client.listObjects(ListObjectsRequest.builder().bucket(bucket).maxKeys(size).build())
					.contents();
		} catch (Exception e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	public ResponseEntity<ByteArrayResource> getObject(String bucket, String key) {
		try {
			ResponseInputStream<GetObjectResponse> responseInputStream = this.s3Client.getObject(GetObjectRequest.builder().bucket(bucket).key(key).build());
			GetObjectResponse getObjectResponse = responseInputStream.response();
			return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + key)
					.contentType(MediaType.valueOf(responseInputStream.response().contentType()))
					.contentLength(getObjectResponse.contentLength())
					.lastModified(getObjectResponse.lastModified())
					.body(new ByteArrayResource(responseInputStream.readAllBytes()));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().build();
		}
	}

	public boolean putObject(String bucket, String key, String path) {
		try {
			return this.s3Client.putObject(PutObjectRequest.builder().bucket(bucket).key(key).build(), Paths.get(path))
					.sdkHttpResponse()
					.isSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean copyObject(String sourceBucket, String sourceKey, String destinationBucket, String destinationKey) {
		try {
			return this.s3Client
					.copyObject(CopyObjectRequest.builder()
							.sourceBucket(sourceBucket).sourceKey(sourceKey)
							.destinationBucket(destinationBucket).destinationKey(destinationKey).build())
					.sdkHttpResponse()
					.isSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean deleteObject(String bucket, String key) {
		try {
			return this.s3Client.deleteObject(DeleteObjectRequest.builder().bucket(bucket).key(key).build())
					.sdkHttpResponse()
					.isSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
