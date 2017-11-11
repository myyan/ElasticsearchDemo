package com.example.demo;

import com.example.demo.service.BatchService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:spring/spring-*.xml")
public class ElasticsearchApplicationTests {


	@Autowired
	private BatchService batchService;

	@Test
	public void contextLoads() throws IOException {
		batchService.batch();
	}




}
