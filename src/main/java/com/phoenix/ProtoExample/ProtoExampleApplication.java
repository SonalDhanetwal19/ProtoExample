package com.phoenix.ProtoExample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.protobuf.ProtobufHttpMessageConverter;

import java.util.Random;

@SpringBootApplication
public class ProtoExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProtoExampleApplication.class, args);

		int id = new Random().nextInt();
		String name = "Test";
		String email = "test@example.org";
		com.phoenix.ProtoExample.UserProto.User firstuser = com.phoenix.ProtoExample.UserProto.User.newBuilder().setId(id)
									.setName(name)
									.setEmail(email)
									.build();
		System.out.println(firstuser);
	}
	@Bean
	ProtobufHttpMessageConverter protobufHttpMessageConverter() {
		return new ProtobufHttpMessageConverter();
	}
}