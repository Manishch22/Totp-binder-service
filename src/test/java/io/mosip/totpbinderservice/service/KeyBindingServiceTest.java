package io.mosip.totpbinderservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.util.UriComponentsBuilder;

import io.mosip.totpbinderservice.dto.JwkDTO;
import io.mosip.totpbinderservice.dto.KeyBindRequestDTO;
import io.mosip.totpbinderservice.dto.KeyBindResponseDTO;
import io.mosip.totpbinderservice.dto.RequestWrapper;
import io.mosip.totpbinderservice.dto.ResponseWrapper;
import io.mosip.totpbinderservice.service.Impl.KeyBindingServiceImpl;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@EnableWebMvc
public class KeyBindingServiceTest {
	
    @Value("${mosip.iam.binding_endpoint}")
    private String bindingEndpoint;
    
    @Autowired
    private KeyBindingServiceImpl keyBindingServiceInstance;
    
    @Before
    public void setUp() {
    	MockitoAnnotations.initMocks(this);
    }
    
    
	
	@SuppressWarnings("unchecked")
	@Test(expected = Exception.class)
	public void sendBindingKeyTest() {		
		RestTemplate restTemplate = Mockito.mock(RestTemplate.class);
		//KeyBindingServiceImpl keyBindingServiceImpl= Mockito.mock(KeyBindingServiceImpl.class);
		KeyBindResponseDTO responseDto = new KeyBindResponseDTO();
		ResponseWrapper<KeyBindResponseDTO> response = new ResponseWrapper<>();
		response.setResponse(responseDto);
		HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "header");

        RequestWrapper<KeyBindRequestDTO> request = new RequestWrapper<KeyBindRequestDTO>();
        request.setRequest(new KeyBindRequestDTO());
        request.setRequestTime(LocalDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")));

        UriComponentsBuilder.fromUriString("http://localhost:8098/");
        new HttpEntity<>(request, headers);
        
        ResponseEntity<ResponseWrapper<KeyBindResponseDTO>> response1 = Mockito.mock(ResponseEntity.class);
        Mockito.mock(ParameterizedTypeReference.class);
        
		Mockito.when(restTemplate.exchange(Mockito.anyString(), Mockito.any(HttpMethod.class), Mockito.any(HttpEntity.class), Mockito.any(ParameterizedTypeReference.class))).thenReturn(response1);
		KeyBindRequestDTO requestDto = new KeyBindRequestDTO();
		Map<String, Object> totpkey1 = new HashMap<String, Object>();
		totpkey1.put("key", "123456");
		requestDto.setTotpKey(totpkey1);
		
		requestDto.setTotpKey(null);
		keyBindingServiceInstance.sendBindingKey(requestDto, bindingEndpoint);	
	}
	
	@Test
	public void getKeyTest1() {
		JwkDTO response = keyBindingServiceInstance.getKey();	
		assertEquals(response.getKty(), "oct");		
	}
	}
