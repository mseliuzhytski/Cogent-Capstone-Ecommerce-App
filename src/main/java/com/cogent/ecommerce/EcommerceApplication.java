package com.cogent.ecommerce;

import com.cogent.ecommerce.loader.DatabaseLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class EcommerceApplication
{
	public static void main(String[] args) {
		SpringApplication.run(EcommerceApplication.class, args);
	}

}
