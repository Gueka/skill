package net.gueka.skill.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SessionEndedRequest;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.speechlet.SpeechletV2;
import com.amazon.speech.ui.Card;
import com.amazon.speech.ui.PlainTextOutputSpeech;

import net.gueka.skill.utils.AlexaUtils;

@Service
public class HandlerSpeechlet implements SpeechletV2 {
	
	protected Logger logger = LoggerFactory.getLogger(HandlerSpeechlet.class);

	@Autowired
	private BeanFactory beanFactory;
	
	@Override
	public void onSessionStarted(SpeechletRequestEnvelope<SessionStartedRequest> requestEnvelope) {
		// TODO Auto-generated method stub
		logger.info("onSessionStarted");
	}

	@Override
	public SpeechletResponse onLaunch(SpeechletRequestEnvelope<LaunchRequest> requestEnvelope) {
		
		// TODO Need to validate if we need a Launch speechlet response or remove it if its unnecessary
		logger.info("onLaunch requestId={}, sessionId={}", requestEnvelope.getRequest().getRequestId(),
				requestEnvelope.getSession().getSessionId());
		
		String errorText = "I don't know what that means. " + AlexaUtils.HELP_TEXT;
		
		Card card = AlexaUtils.newCard("Dazed and Confused", errorText);
		PlainTextOutputSpeech speech = AlexaUtils.newSpeech(errorText, false);
		
		return AlexaUtils.newSpeechletResponse(card, speech, requestEnvelope.getSession(), true);	
	}

	/* Each request will execute onIntent and we will figure it, out based on the name of the intent,
	 * what handler we need and get it from the spring context
	 */
	@Override
	public SpeechletResponse onIntent(SpeechletRequestEnvelope<IntentRequest> requestEnvelope) {
		
		logger.info("onIntent");
		
		IntentRequest request = requestEnvelope.getRequest();
		Session session = requestEnvelope.getSession();
		
		Intent intent = request.getIntent();
		if ( intent != null ) {
			
			// Derive the handler's bean name
			String intentName = intent.getName();// Example Hello
			String handlerBeanName = intentName + "Handler";
			
			// If this is an Amazon Intent, change the handler name to better
			// match up to a Spring bean name.  For example, the intent AMAZON.HelpIntent should
			// be changed to AmazonHelpIntent, in order to make it more Classable named (?)
			handlerBeanName = StringUtils.replace(handlerBeanName, "AMAZON.", "Amazon");
			handlerBeanName = handlerBeanName.substring(0, 1).toLowerCase() + handlerBeanName.substring(1);
			
			if ( logger.isInfoEnabled() ) {
				logger.info("About to invoke handler '" + handlerBeanName + "' for intent '" + intentName + "'.");
			}
			
			// Handle the intent by delegating to the designated handler by getting it from the context.
			try {
				Object handlerBean = beanFactory.getBean(handlerBeanName);
			
				if ( handlerBean != null ) {
					
					if ( handlerBean instanceof IntentHandler ) {
						IntentHandler intentHandler = (IntentHandler) handlerBean;
						return intentHandler.handleIntent(intent, request, session);
					}
				}
			}
			catch (Exception e) {
				logger.error("Error handling intent " + intentName, e);
			}
		}
		
		
		// Handle unknown intents.  Ask the user for more info.
		// Start a conversation (if not started already) and say that we did not understand the intent
		AlexaUtils.setConversationMode(session, true);
		
		String errorText = "I don't know what that means. " + AlexaUtils.HELP_TEXT;
		
		Card card = AlexaUtils.newCard("Dazed and Confused", errorText);
		PlainTextOutputSpeech speech = AlexaUtils.newSpeech(errorText, false);
		
		return AlexaUtils.newSpeechletResponse(card, speech, session, false);	
	}

	@Override
	public void onSessionEnded(SpeechletRequestEnvelope<SessionEndedRequest> requestEnvelope) {
		// TODO Auto-generated method stub

	}

}
