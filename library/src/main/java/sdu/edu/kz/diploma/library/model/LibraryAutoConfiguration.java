package sdu.edu.kz.diploma.library.model;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import sdu.edu.kz.diploma.library.model.entity.EntityScanner;
import sdu.edu.kz.diploma.library.model.repository.SharedRepositoryScanner;
import sdu.edu.kz.diploma.library.test.repository.TestRepositoryScanner;

@AutoConfiguration
@EnableJpaAuditing
@EntityScan(basePackageClasses = EntityScanner.class)
@EnableJpaRepositories(basePackageClasses = {SharedRepositoryScanner.class, TestRepositoryScanner.class})
public class LibraryAutoConfiguration {
}