package net.gueka.skill.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.amazon.speech.speechlet.servlet.SpeechletServlet;

import lombok.extern.slf4j.Slf4j;
import net.gueka.skill.handler.HandlerSpeechlet;

@Slf4j
@Configuration
@Profile("qa")
public class AlexaConfig {
	
	@Autowired
	private HandlerSpeechlet handlerSpeechlet;

	@Value("${applicationIds}")
	private String applicationIds;
	
	@Bean
	public ServletRegistrationBean<SpeechletServlet> registerSpeechletServlet() {
		log.info("Setting property for applicationIds: " + applicationIds);
		System.setProperty("com.amazon.speech.speechlet.servlet.supportedApplicationIds", applicationIds);
		
		SpeechletServlet speechletServlet = new SpeechletServlet();
		speechletServlet.setSpeechlet(handlerSpeechlet);
		
		ServletRegistrationBean<SpeechletServlet> servletRegistrationBean = new ServletRegistrationBean<SpeechletServlet>(speechletServlet, "/alexa");
		return servletRegistrationBean;
	}
	
}
