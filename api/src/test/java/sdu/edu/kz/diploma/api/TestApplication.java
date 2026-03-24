package sdu.edu.kz.diploma.api;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import sdu.edu.kz.diploma.api.auth.JwtAuthenticationFilter;
import sdu.edu.kz.diploma.api.auth.SecurityConfig;

@SpringBootApplication
@ComponentScan(
        basePackages = {
                "sdu.edu.kz.diploma.api",
                "sdu.edu.kz.diploma.library"
        },
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = {SecurityConfig.class, JwtAuthenticationFilter.class}
        )
)
public class TestApplication {
}