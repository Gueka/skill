package net.gueka.skill.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.amazon.speech.speechlet.servlet.SpeechletServlet;

import net.gueka.skill.handler.HandlerSpeechlet;

@Configuration
@Profile("dev")
public class AlexaDevConfig {

	@Autowired
	private HandlerSpeechlet handlerSpeechlet;

	@Bean
	public ServletRegistrationBean<SpeechletServlet> registerSpeechletServlet() {
		// force system property to no validate signed request before we create the speechlet
		//System.setProperty("com.amazon.speech.speechlet.servlet.disableRequestSignatureCheck", "true");
		
		SpeechletServlet speechletServlet = new SpeechletServlet();
		
		speechletServlet.setSpeechlet(handlerSpeechlet);
		
		ServletRegistrationBean<SpeechletServlet> servletRegistrationBean = new ServletRegistrationBean<SpeechletServlet>(speechletServlet, "/alexa");
		return servletRegistrationBean;
	}
	
}
