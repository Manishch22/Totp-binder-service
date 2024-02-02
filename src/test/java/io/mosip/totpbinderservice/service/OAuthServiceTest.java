package io.mosip.totpbinderservice.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
	public void getAccessTokenTest1() {
		AccessTokenRequestDTO request = new AccessTokenRequestDTO();		
		oAuthServiceImpl.getAccessToken(request);
	}	

}
