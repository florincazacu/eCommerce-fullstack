package com.example.springbootlibrary.config;

import com.okta.spring.boot.oauth.Okta;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.accept.ContentNegotiationStrategy;
import org.springframework.web.accept.HeaderContentNegotiationStrategy;

@Configuration
public class SecurityConfiguration {

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		// disable cross site request forgery
		http.csrf().disable();

		// protect endpoints at <api>/type/secure
		http.authorizeRequests(auth ->
				auth
					.antMatchers("/api/books/secure/**")
					.authenticated())
			.oauth2ResourceServer(oauth2 -> oauth2
				.jwt(Customizer.withDefaults())
			);

		// add CORS filters
		http.cors();

		// add content negotiation strategy
		http.setSharedObject(ContentNegotiationStrategy.class, new HeaderContentNegotiationStrategy());

		// Force a non-empty response body for 401 to make response friendly
		Okta.configureResourceServer401ResponseBody(http);

		return http.build();
	}
}
