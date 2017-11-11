package com.example.demo.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * Created by heiqie on 2017/9/22.
 */
@Configuration
@ImportResource("classpath:**/*.xml")
@ComponentScan
public class Config {

}
