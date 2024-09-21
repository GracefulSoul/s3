package gracefulsoul.s3.config;

import java.net.URI;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;
import software.amazon.awssdk.utils.StringUtils;

@Configuration
@RequiredArgsConstructor
public class S3Config {

	private final S3Properties s3Properties;

	@Bean
	public S3Client s3Client() {
		S3ClientBuilder s3ClientBuilder = S3Client.builder().credentialsProvider(this.getAwsCredentialsProvider());
		if (StringUtils.isNotBlank(this.s3Properties.getEndpointUrl())) {
			s3ClientBuilder.endpointOverride(URI.create(this.s3Properties.getEndpointUrl()));
		}
		if (StringUtils.isNotBlank(this.s3Properties.getRegion())) {
			s3ClientBuilder.region(Region.of(this.s3Properties.getRegion()));
		}
		return s3ClientBuilder.build();
	}

	private AwsCredentialsProvider getAwsCredentialsProvider() {
		if (StringUtils.isNotBlank(this.s3Properties.getAccessKeyId()) && StringUtils.isNotBlank(this.s3Properties.getSecretAccessKey())) {
			return StaticCredentialsProvider.create(
					AwsBasicCredentials.create(this.s3Properties.getAccessKeyId(), this.s3Properties.getSecretAccessKey()));
		} else {
			return DefaultCredentialsProvider.create();
		}
	}

}
