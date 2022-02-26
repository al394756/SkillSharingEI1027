package com.example.SkillSharingEI1027;


import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.util.logging.Logger;

@SpringBootApplication
public class SkillSharingApplication {

	private static final Logger log = Logger.getLogger(SkillSharingApplication.class.getName());

	public static void main(String[] args) {
		new SpringApplicationBuilder(SkillSharingApplication.class).run(args);

	}

}
