package com.cognizant.spring_learn;

import com.cognizant.spring_learn.Country;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

	public static void main(String[] args) {

		ApplicationContext context =
				new ClassPathXmlApplicationContext("country.xml");

		Country country = context.getBean("country", Country.class);

		System.out.println(country);
	}
}