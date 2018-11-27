package net.gueka.skill.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.Card;
import com.amazon.speech.ui.PlainTextOutputSpeech;

import net.gueka.skill.service.DataService;
import net.gueka.skill.utils.AlexaUtils;

@Component
public class HelloHandler implements IntentHandler {
	
	@Autowired
	private DataService service;

	@Override
	public SpeechletResponse handleIntent(Intent intent, IntentRequest request, Session session) {
		// Hello world example
		Card card = AlexaUtils.newCard("Hello", service.getRandomData().getMessage());
		PlainTextOutputSpeech speech = AlexaUtils.newSpeech("This is a default hello world message", AlexaUtils.inConversationMode(session));

		return AlexaUtils.newSpeechletResponse( card, speech, session, false);
			
	}

}
