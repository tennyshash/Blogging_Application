package com.shashwat.blog_app_youtube;

import com.shashwat.blog_app_youtube.Repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BlogAppYoutubeApplicationTests {
	private UserRepository userRepository;


	@Autowired
	public BlogAppYoutubeApplicationTests(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Test
	void contextLoads() {
	}
	@Test
	public void repoTest(){
		String className= userRepository.getClass().getName();
		String packAge=userRepository.getClass().getPackageName();

		System.out.println(packAge);
		System.out.println(className);
	}

}
