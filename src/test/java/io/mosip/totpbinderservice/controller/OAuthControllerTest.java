package io.mosip.totpbinderservice.controller;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.mosip.totpbinderservice.dto.AccessTokenResponseDTO;
import io.mosip.totpbinderservice.dto.ResponseWrapper;
import io.mosip.totpbinderservice.service.OAuthService;
import io.mosip.totpbinderservice.dto.AccessTokenRequestDTO;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@EnableWebMvc
public class OAuthControllerTest {

	@MockBean
	OAuthService oAuthService;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	
	@Test
	public void getTokenTest() {	
		ResponseWrapper<AccessTokenResponseDTO> responseOfService = new ResponseWrapper<>();
		AccessTokenRequestDTO request = new AccessTokenRequestDTO();
		request.setCode("code");
		
		AccessTokenResponseDTO responseDTO = new AccessTokenResponseDTO();
		responseDTO.setAccessToken("accessToken");
		responseDTO.setExpiresIn("60");
		responseDTO.setIdToken("idtoken");
		responseOfService.setResponse(responseDTO);
		Mockito.when(oAuthService.getAccessToken(Mockito.any())).thenReturn(responseDTO);
		
		try {
			mockMvc.perform(post("/oauth/token").contentType(MediaType.APPLICATION_JSON_VALUE)
			        .content(objectMapper.writeValueAsString(request))).andExpect(status().isOk());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}
