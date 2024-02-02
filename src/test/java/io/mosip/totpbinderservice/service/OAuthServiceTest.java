package io.mosip.totpbinderservice.service;

import static org.mockito.ArgumentMatchers.eq;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import io.mosip.totpbinderservice.dto.AccessTokenRequestDTO;
import io.mosip.totpbinderservice.service.Impl.OAuthServiceImpl;
import io.mosip.totpbinderservice.util.JWTGenerator;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@EnableWebMvc
public class OAuthServiceTest {

	@Autowired
	private OAuthServiceImpl oAuthServiceImpl;

	@Mock
	private JWTGenerator jwtGenerator;

	@Mock
	private RestTemplate restTemplate;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		ReflectionTestUtils.setField(oAuthServiceImpl, "jwtGenerator", jwtGenerator);
		ReflectionTestUtils.setField(oAuthServiceImpl, "restTemplate", restTemplate);
	}

	@Test(expected = Exception.class)
	public void getAccessTokenTest() {
		AccessTokenRequestDTO request = new AccessTokenRequestDTO();
		oAuthServiceImpl.getAccessToken(request);
	}

	@Test
	public void getAccessTokenTest1() {
		AccessTokenRequestDTO request = new AccessTokenRequestDTO();
		request.setCode("123678");
		ResponseEntity<String> responseEntity = new ResponseEntity<String>(
				"{\r\n" + "	\"access_token\": \"ertyuyfd\",\r\n" + "	\"expires_in\": \"90\",\r\n"
						+ "	\"refresh_expires_in\": \"90\",\r\n" + "	\"refresh_token\": \"true\",\r\n"
						+ "	\"token_type\": \"type\",\r\n" + "	\"session_state\": \"active\",\r\n"
						+ "	\"scope\": \"global\",\r\n" + "	\"id_token\": \"idtoken\"\r\n" + "}",
				HttpStatus.OK);
		Mockito.when(restTemplate.exchange(Mockito.anyString(), Mockito.any(HttpMethod.class),
				Mockito.<RequestEntity<Void>>any(), eq(String.class))).thenReturn(responseEntity);
		oAuthServiceImpl.getAccessToken(request);
	}

	@Test(expected = Exception.class)
	public void getAccessTokenTest2() {
		AccessTokenRequestDTO request = new AccessTokenRequestDTO();
		request.setCode("123678");
		ResponseEntity<String> responseEntity = new ResponseEntity<String>("", HttpStatus.BAD_REQUEST);
		Mockito.when(restTemplate.exchange(Mockito.anyString(), Mockito.any(HttpMethod.class),
				Mockito.<RequestEntity<Void>>any(), eq(String.class))).thenReturn(responseEntity);
		oAuthServiceImpl.getAccessToken(request);
	}
}
