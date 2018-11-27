package net.gueka.skill.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.Card;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.StandardCard;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AlexaUtils {
	
	protected static final String SESSION_CONVERSATION_FLAG = "conversation";
	
	public static final String HELP_TEXT = "Here is something you can say: hello";
	public static final String REPROMPT_TEXT = "What else can I tell you?  Say \"Help\" for some suggestions.";
	
	private AlexaUtils() { }
	
	public static Card newCard(String cardTitle, String cardText) {
		StandardCard card = new StandardCard();
		card.setTitle(cardTitle == null ? "" : cardTitle);
		card.setText(cardText);

		return card;
	}
	
	public static PlainTextOutputSpeech newSpeech( String speechText, boolean appendRepromptText ) {
		PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
		speech.setText( appendRepromptText ? speechText + "\n\n" + AlexaUtils.REPROMPT_TEXT : speechText);

		return speech;
	}
	
	/**
	 * Use this util to generate a response
	 * @param card Display card you can find in your alexa app or an alexa display
	 * @param speech Actual readable text for alexa
	 * @param session Current speech session
	 * @param shouldEndSession true when is the end of a conversation
	 * 
	 * @return SpeechletResponse it can return a new question for the user.
	 */
	public static SpeechletResponse newSpeechletResponse(Card card, PlainTextOutputSpeech speech, Session session, boolean shouldEndSession)  {
		
		log.info("Generating response for speech: " + speech.getText() + ", with card: " + card.getTitle() + ", and session: " + session.getSessionId());
		
		if ( AlexaUtils.inConversationMode(session) && !shouldEndSession) {
			PlainTextOutputSpeech repromptSpeech = new PlainTextOutputSpeech();
			repromptSpeech.setText(AlexaUtils.REPROMPT_TEXT);
			
			Reprompt reprompt = new Reprompt();
			reprompt.setOutputSpeech(repromptSpeech);
			
			log.info("Generating newAskResponse");
			return SpeechletResponse.newAskResponse(speech, reprompt, card);
		}
		else {
			log.info("Generating newTellResponse");
			return SpeechletResponse.newTellResponse(speech, card);
		}
	}
	
	public static void setConversationMode(Session session, boolean conversationMode) {		
		if (conversationMode) {
			session.setAttribute(SESSION_CONVERSATION_FLAG, "true");
		}else {
			session.removeAttribute(SESSION_CONVERSATION_FLAG);
		}
	}

	public static boolean inConversationMode(Session session) {
		 return session.getAttribute(SESSION_CONVERSATION_FLAG) != null;
	}	
	
	// TODO move this to AlexaDateUtils
	public static String spokenDayOfWeek( Date date, TimeZone zone ) {
		SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
		sdf.setTimeZone(zone);
		
		return sdf.format(date);
	}
	
	public static String spokenDate( Date date, TimeZone zone ) {
		SimpleDateFormat sdf = new SimpleDateFormat("MMMM d");
		sdf.setTimeZone(zone);
		
		return sdf.format(date);
	}
	
	public static String spokenTime( Date date, TimeZone zone ) {
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
		sdf.setTimeZone(zone);
		
		return sdf.format(date);
	}
}
