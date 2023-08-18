package com.shashwat.blog_app_youtube;

import com.shashwat.blog_app_youtube.Config.AppConstants;
import com.shashwat.blog_app_youtube.Models.Role;
import com.shashwat.blog_app_youtube.Repository.RoleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@SpringBootApplication
public class BlogAppYoutubeApplication implements CommandLineRunner {

	private PasswordEncoder passwordEncoder;
	private RoleRepository roleRepository;

	@Autowired
	public BlogAppYoutubeApplication(PasswordEncoder passwordEncoder,RoleRepository roleRepository) {
		this.passwordEncoder = passwordEncoder;
		this.roleRepository=roleRepository;
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println(passwordEncoder.encode("xyz"));

		try {
			Role adminRole= new Role();
			adminRole.setId(AppConstants.ADMIN_USER);
			adminRole.setRoleName("ROLE_ADMIN");

			Role normalRole= new Role();
			normalRole.setId(AppConstants.NORMAL_USER);
			normalRole.setRoleName("ROLE_NORMAL");

			List<Role> roles=List.of(adminRole,normalRole);
			List<Role> result=roleRepository.saveAll(roles);

			result.forEach( r-> {
				System.out.println(r.getRoleName());
			});

		}catch (Exception e){
			e.printStackTrace();
		}
	}

	@Bean
	@Scope("singleton")
	public ModelMapper  modelMapper(){
		return new ModelMapper();
	}

	public static void main(String[] args) {
		SpringApplication.run(BlogAppYoutubeApplication.class, args);
	}

}
