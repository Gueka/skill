package net.gueka.skill;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestClientException;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.json.SpeechletResponseEnvelope;
import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.IntentRequest.DialogState;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class SkillApplicationTests {

	@LocalServerPort
    private int port;
	
	@Autowired
	private TestRestTemplate restTemplate;
	
    @Autowired
    private ObjectMapper mapper;
	
	@Test
	public void testHelloIntent() throws RestClientException, JsonProcessingException {
	    String uri = "http://localhost:" + port + "/alexa";
	    
	    SpeechletRequestEnvelope<SpeechletRequest> envelope = SpeechletRequestEnvelope.builder()
	    	.withVersion("1.8.1")
	    	.withSession(Session.builder()
	    			.withIsNew(true)
	    			.withSessionId(UUID.randomUUID().toString())
	    			.build())
	    	.withRequest(IntentRequest.builder()
		    		.withRequestId(UUID.randomUUID().toString())
		    		.withIntent(Intent.builder()
		    				.withName("Hello")
		    				.build())
		    		.withTimestamp(new Date())
		    		.withLocale(Locale.US)
		    		.withDialogState(DialogState.STARTED)
		    		.build())
	    	.build();
	    
	    ResponseEntity<SpeechletResponseEnvelope> response = this.restTemplate.exchange(
	    		uri,
	    		HttpMethod.POST,
	    		getPostRequestHeaders(envelope),
	    		SpeechletResponseEnvelope.class);

	    assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
	    assertThat(response.getBody().getResponse().getCard().getTitle(), equalTo("Hello"));
	}

	private <T> HttpEntity<String> getPostRequestHeaders(T request) throws JsonProcessingException {
        List<MediaType> acceptTypes = new ArrayList<MediaType>();
        acceptTypes.add(MediaType.APPLICATION_JSON_UTF8);

        HttpHeaders reqHeaders = new HttpHeaders();
        reqHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
        reqHeaders.setAccept(acceptTypes);

        return new HttpEntity<>(mapper.writeValueAsString(request), reqHeaders);
    }

}
