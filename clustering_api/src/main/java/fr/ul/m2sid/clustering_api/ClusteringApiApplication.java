package fr.ul.m2sid.clustering_api;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableRabbit
@SpringBootApplication
public class ClusteringApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClusteringApiApplication.class, args);
	}
}
