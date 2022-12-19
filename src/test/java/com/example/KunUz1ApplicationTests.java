package com.example;

import com.example.entity.ArticleEntity;
import com.example.repository.ArticleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class KunUz1ApplicationTests {
	@Autowired
	private ArticleRepository articleRepository;

	@Test
	void contextLoads() {
		Integer update = articleRepository.updateByArticleId("1fd1d9ce-b5aa-4c20-aa53-92f68d917793");
		System.out.println(update);
	}

}
