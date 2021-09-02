package com.orbis.ssgascraper;

import com.orbis.ssgascraper.model.Role;
import com.orbis.ssgascraper.model.User;
import com.orbis.ssgascraper.service.UserService;
import com.orbis.ssgascraper.webscraper.ScraperMain;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@SpringBootApplication
public class SsgaScraperApplication {

	public static void main(String[] args) {

		ConfigurableApplicationContext context =  SpringApplication.run(SsgaScraperApplication.class, args);

		context.getBean(ScraperMain.class).start();
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	CommandLineRunner run(UserService userService) {
		return args -> {
			String roleUser = "ROLE_USER";
			if (userService.getRole(roleUser) == null) {
				userService.saveRole(new Role(null, roleUser));
			}

			String roleAdmin = "ROLE_ADMIN";
			if (userService.getRole(roleAdmin) == null) {
				userService.saveRole(new Role(null, roleAdmin));
			}

			String johnName = "john";
			if (userService.getUser(johnName) == null) {
				userService.saveUser(new User(null, "John Travolta", johnName, "123",
						new ArrayList<>()));
				userService.addRoleToUser("john", "ROLE_USER");
				userService.addRoleToUser("john", "ROLE_MANAGER");
			}

			String willName = "will";
			if (userService.getUser(willName) == null) {
				userService.saveUser(new User(null, "Will Smith", "will", "123",
						new ArrayList<>()));
				userService.addRoleToUser("will", "ROLE_MANAGER");
			}
		};
	}

}