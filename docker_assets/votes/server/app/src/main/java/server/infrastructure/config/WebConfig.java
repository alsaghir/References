package server.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowCredentials(true)
                .allowedMethods(HttpMethod.OPTIONS.name(), HttpMethod.PUT.name(), HttpMethod.DELETE.name(),
                        HttpMethod.GET.name(), HttpMethod.POST.name(), HttpMethod.PATCH.name())
                .allowedHeaders("access-control-allow-origin", "authorization", "content-type", "Cache-Control")
                .allowedOriginPatterns("http://localhost:[*]");
    }



}
