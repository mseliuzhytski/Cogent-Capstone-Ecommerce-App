package com.cogent.ecommerce;

import com.cogent.ecommerce.loader.DatabaseLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EcommerceApplication implements CommandLineRunner
{
	@Value( "${spring.jpa.hibernate.ddl-auto}" )
	private String ddl_auto;

	@Value("${seedData}")
	private String seedData;
	public static final String TRUE = "true";
	public static final String FALSE = "false";

	public static final String CREATE_DROP = "create-drop";
	public static final String CREATE = "create";
	public static final String UPDATE = "update";


	public static void main(String[] args) {
		SpringApplication.run(EcommerceApplication.class, args);
	}

	@Autowired
	private DatabaseLoader loader;

	@Override
	public void run(String... args) throws Exception {
		if (seedData.equals(TRUE)) {
			loader.loadSeededData();
		}
	}

}
