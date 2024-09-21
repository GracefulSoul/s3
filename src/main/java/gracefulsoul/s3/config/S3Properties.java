package gracefulsoul.s3.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "s3")
public class S3Properties {

	private String endpointUrl;
	private String accessKeyId;
	private String secretAccessKey;
	private String region;

}
