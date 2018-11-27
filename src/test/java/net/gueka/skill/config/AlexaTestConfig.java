package net.gueka.skill.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.TestPropertySource;

import com.amazon.speech.speechlet.servlet.SpeechletServlet;

import net.gueka.skill.handler.HandlerSpeechlet;

@Configuration
@Profile("test")
@TestPropertySource(locations="classpath:test.properties")
public class AlexaTestConfig {
	
	@Autowired
	private HandlerSpeechlet handlerSpeechlet;
	
	@Bean
	public ServletRegistrationBean registerSpeechletServlet() {
		
		// force system property to no validate signed request before we create the speechlet
		System.setProperty("com.amazon.speech.speechlet.servlet.disableRequestSignatureCheck", "true");
		
		SpeechletServlet speechletServlet = new SpeechletServlet();
		
		speechletServlet.setSpeechlet(handlerSpeechlet);
		
		ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(speechletServlet, "/alexa");
		return servletRegistrationBean;
	}

	
}
