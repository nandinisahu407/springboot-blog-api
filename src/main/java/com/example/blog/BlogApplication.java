package com.example.blog;

import com.example.blog.repository.RoleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import com.example.blog.entity.Role;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BlogApplication {
	@Autowired
	private RoleRepository roleRepository;

	public static void main(String[] args) {
		SpringApplication.run(BlogApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper(){
		return new ModelMapper();
	}

	// CommandLineRunner will run after the application starts-> creates 'admin' and 'normal' role automatically if not exist in db
	@Bean
	public CommandLineRunner run() {
		return args -> {
			// Check if the 'admin' and 'normal' roles exist in the database
			if (!roleRepository.existsById(1)) {
				Role adminRole = new Role();
				adminRole.setId(1);
				adminRole.setName("ADMIN");
				roleRepository.save(adminRole);
			}
			if (!roleRepository.existsById(2)) {
				Role normalRole = new Role();
				normalRole.setId(2);
				normalRole.setName("NORMAL");
				roleRepository.save(normalRole);
			}
		};
	}


}
