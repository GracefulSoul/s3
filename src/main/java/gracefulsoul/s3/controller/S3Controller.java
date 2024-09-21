package gracefulsoul.s3.controller;

import java.util.List;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gracefulsoul.s3.service.S3Service;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.s3.model.S3Object;

/**
 * <p>From Spring Boot 3.2 and Spring Framework 6.1 and above,
 * when using {@link PathVariable} annotation, the following error occurs:
 * "Ensure that the compiler uses the '-parameters' flag.".
 * 
 * @see <a href="https://github.com/spring-projects/spring-framework/wiki/Upgrading-to-Spring-Framework-6.x#parameter-name-retention">Upgrading-to-Spring-Framework-6.x#parameter-name-retention</a>
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class S3Controller {

	private final S3Service s3Service;

	@GetMapping("/{bucket}")
	public List<S3Object> getObjectList(
			@PathVariable(name = "bucket") String bucket,
			@RequestParam(name = "size", required = false, defaultValue = "100") int size) {
		return this.s3Service.listObjects(bucket, size);
	}

	@GetMapping("/{bucket}/{key}")
	public ResponseEntity<ByteArrayResource> getObject(
			@PathVariable(name = "bucket") String bucket,
			@PathVariable(name = "key") String key) {
		return this.s3Service.getObject(bucket, key);
	}

	@PutMapping("/{bucket}/{key}")
	public boolean putObject(
			@PathVariable(name = "bucket") String bucket,
			@PathVariable(name = "key") String key,
			@RequestParam(name = "path", required = true) String path) {
		return this.s3Service.putObject(bucket, key, path);
	}

	@PatchMapping("/{bucket}/{key}")
	public boolean copyObject(
			@PathVariable(name = "bucket") String bucket,
			@PathVariable(name = "key") String key,
			@RequestParam(name = "bucket", required = true) String destinationBucket,
			@RequestParam(name = "key", required = true) String destinationKey) {
		return this.s3Service.copyObject(bucket, key, destinationBucket, destinationKey);
	}

	@DeleteMapping("/{bucket}/{key}")
	public boolean deleteObject(
			@PathVariable(name = "bucket") String bucket,
			@PathVariable(name = "key") String key) {
		return this.s3Service.deleteObject(bucket, key);
	}

}
