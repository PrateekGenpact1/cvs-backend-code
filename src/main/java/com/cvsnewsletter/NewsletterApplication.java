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
			String managerOhrId = "123456789"; // unique OHR ID for manager
			Member manager = repository.findByOhrId(managerOhrId)
					.orElseGet(() -> repository.save(Member.builder()
							.firstName("Manager")
							.lastName("Manager")
							.ohrId(managerOhrId)
							.password(encoder.encode("managerpass"))
							.isPasswordSet(true)
							.role(Role.MANAGER)
							.designationBand("4D Principal Consultant")
							.genpactMailId("manager@genpact.com")
							.emergencyContactName("Manager")
							.emergencyPhoneNumber("1234567890")
							.contactNumber("1234567801")
							.build()));

			String adminOhrId = "987654321"; // unique OHR ID for admin
			if (repository.findByOhrId(adminOhrId).isEmpty()) {
				repository.save(Member.builder()
						.firstName("Admin")
						.lastName("Admin")
						.ohrId(adminOhrId)
						.password(encoder.encode("password"))
						.isPasswordSet(true)
						.role(Role.ADMIN)
						.designationBand("4B Consultant")
						.reportingManager(manager.getFirstName() + " " + manager.getLastName())
						.reportingManagerOhrId(manager.getOhrId())
						.genpactMailId("admin@genpact.com")
						.emergencyContactName("Admin")
						.emergencyPhoneNumber("9876543210")
						.contactNumber("9876543201")
						.build());
			}
		};
	}

}
