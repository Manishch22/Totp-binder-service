package io.mosip.totpbinderservice.controller;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.Map;

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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.mosip.totpbinderservice.dto.JwkDTO;
import io.mosip.totpbinderservice.dto.KeyBindRequestDTO;
import io.mosip.totpbinderservice.dto.KeyBindResponseDTO;
import io.mosip.totpbinderservice.dto.ResponseWrapper;
import io.mosip.totpbinderservice.service.KeyBindingService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@EnableWebMvc
public class KeyBindingControllerTest {

	@MockBean
	KeyBindingService keyBindingService;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Test 
	public void bindKeyTest() {
		ResponseWrapper<KeyBindResponseDTO> response = new ResponseWrapper<KeyBindResponseDTO>();		
		KeyBindRequestDTO requestDto = new KeyBindRequestDTO();
		Map<String, Object> totpkey1 = new HashMap<String, Object>();
		totpkey1.put("key", "123456");
		requestDto.setTotpKey(totpkey1);
		KeyBindResponseDTO responseDto = new KeyBindResponseDTO();
		response.setResponse(responseDto);
		Mockito.when(keyBindingService.sendBindingKey(requestDto, "header")).thenReturn(responseDto);
		try {			
			mockMvc.perform(post("/binding/totp-key-bind").contentType(MediaType.APPLICATION_JSON_VALUE)
			        .content(objectMapper.writeValueAsString(requestDto)).header("Authorization", "header")).andExpect(status().isOk());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void generateKeyTest() {
		ResponseWrapper<JwkDTO> response = new ResponseWrapper<JwkDTO>();
		JwkDTO responseDTO = new JwkDTO();
		response.setResponse(responseDTO);
		Mockito.when(keyBindingService.getKey()).thenReturn(responseDTO);
		try {
			mockMvc.perform(MockMvcRequestBuilders.get("/binding/key")).andExpect(MockMvcResultMatchers.status().isOk());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
