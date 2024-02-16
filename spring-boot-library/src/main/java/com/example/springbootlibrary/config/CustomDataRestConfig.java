package com.example.springbootlibrary.config;

import com.example.springbootlibrary.entity.Book;
import com.example.springbootlibrary.entity.Message;
import com.example.springbootlibrary.entity.Review;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Configuration
public class CustomDataRestConfig implements RepositoryRestConfigurer {

	private final String ALLOWED_ORIGINS = "https://localhost:3000";

	@Override
	public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {

		HttpMethod[] unsupportedActions = {HttpMethod.POST,
			HttpMethod.PATCH,
			HttpMethod.DELETE,
			HttpMethod.PUT};

		config.exposeIdsFor(Book.class);
		config.exposeIdsFor(Review.class);
		config.exposeIdsFor(Message.class);

		disableHttpMethods(Book.class, config, unsupportedActions);
		disableHttpMethods(Review.class, config, unsupportedActions);
		disableHttpMethods(Message.class, config, unsupportedActions);

		/* Configure CORS Mapping */
		cors.addMapping(config.getBasePath() + "/**")
			.allowedOrigins(ALLOWED_ORIGINS);
	}

	private void disableHttpMethods(Class clazz, RepositoryRestConfiguration config, HttpMethod[] unsupportedActions) {
		config.getExposureConfiguration()
			.forDomainType(clazz)
			.withItemExposure((metadata, httpMethods) -> httpMethods.disable(unsupportedActions))
			.withCollectionExposure(((metadata, httpMethods) -> httpMethods.disable(unsupportedActions)));
	}
}
