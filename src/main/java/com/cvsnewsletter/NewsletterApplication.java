package com.cvsnewsletter;

import com.cvsnewsletter.entities.Member;
import com.cvsnewsletter.entities.enums.Role;
import com.cvsnewsletter.repositories.MemberRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableScheduling
public class NewsletterApplication {

	public static void main(String[] args) {
		SpringApplication.run(NewsletterApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(
			MemberRepository repository, PasswordEncoder encoder
	) {
		return args -> {
			if(repository.findByOhrId("987654321").isEmpty()) {
				repository.save(Member.builder()
						.firstName("Admin")
						.lastName("Admin")
						.ohrId("987654321")
						.password(encoder.encode("password"))
						.isPasswordSet(true)
						.role(Role.ADMIN)
						.emergencyContactName("Admin")
						.emergencyPhoneNumber("9876543210")
						.contactNumber("9876543201")
						.build());
			}
		};
	}

}
