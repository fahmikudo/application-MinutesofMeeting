package id.web.fahmikudo.meeting.mom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@SpringBootApplication
@EnableSwagger2
public class MoMApplication {

	public static void main(String[] args) {
		SpringApplication.run(MoMApplication.class, args);
	}

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any())
				.build()
				.apiInfo(apiInfo());
	}

	private ApiInfo apiInfo() {
		ApiInfo apiInfo = new ApiInfo(
				"Aplikasi Minutes of Meeting",
				"API minutes of meeting",
				"V1.0.0",
				"Terms of service",
				new Contact("Fahmi Kudo", "fahmikudo.web.id", "fahmi.hidayatullah12@gmail.com"),
				"Apache License 2.0", "https://www.apache.org/licenses/LICENSE-2.0", Collections.emptyList());
		return apiInfo;
	}

}
