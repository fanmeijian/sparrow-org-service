package cn.sparrowmini.org.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@EntityScan("cn.sparrowmini.org.model")
@SpringBootApplication
public class SparrowOrgApplication {

	public static void main(String[] args) {
		SpringApplication.run(SparrowOrgApplication.class, args);

	}

}
