package sdu.edu.kz.diploma.library.model.syllabus;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import sdu.edu.kz.diploma.library.model.syllabus.entity.EntityScanner;
import sdu.edu.kz.diploma.library.model.syllabus.repository.SharedRepositoryScanner;

@AutoConfiguration
@EnableJpaAuditing
@EntityScan(basePackageClasses = EntityScanner.class)
@EnableJpaRepositories(basePackageClasses = SharedRepositoryScanner.class)
public class LibraryAutoConfiguration {
}