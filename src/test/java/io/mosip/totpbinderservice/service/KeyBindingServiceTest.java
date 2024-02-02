package io.mosip.totpbinderservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import io.mosip.totpbinderservice.dto.JwkDTO;
import io.mosip.totpbinderservice.dto.KeyBindRequestDTO;
import io.mosip.totpbinderservice.dto.KeyBindResponseDTO;
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
    
    @Mock
    private RestTemplate restTemplate;
    
    @Before
    public void setUp() {
    	MockitoAnnotations.initMocks(this);
    	ReflectionTestUtils.setField(keyBindingServiceInstance, "restTemplate", restTemplate);
    }
	
	@Test
	public void sendBindingKeyTest() {		
        ResponseWrapper<KeyBindResponseDTO> responseFromService = new ResponseWrapper<KeyBindResponseDTO>();
        KeyBindResponseDTO resDto = new KeyBindResponseDTO();
        resDto.setStatus("success");
        responseFromService.setResponse(resDto);       
         
        
        ResponseEntity<ResponseWrapper<KeyBindResponseDTO>> responseEntity = new ResponseEntity<ResponseWrapper<KeyBindResponseDTO>>(
        		responseFromService, HttpStatus.OK); 
		Mockito.when(restTemplate.exchange(Mockito.anyString(), Mockito.any(HttpMethod.class), Mockito.<RequestEntity<Void>>any(), eq(new ParameterizedTypeReference<ResponseWrapper<KeyBindResponseDTO>>() {})))
		.thenReturn(responseEntity);
		
		KeyBindRequestDTO requestDto = new KeyBindRequestDTO();
		Map<String, Object> totpkey1 = new HashMap<String, Object>();
		totpkey1.put("key", "123456");
		requestDto.setTotpKey(totpkey1);
		
		requestDto.setTotpKey(null);
		keyBindingServiceInstance.sendBindingKey(requestDto, bindingEndpoint);	
	}
	
	@Test(expected = Exception.class)
	public void sendBindingKeyTest1() {		
        ResponseWrapper<KeyBindResponseDTO> responseFromService = new ResponseWrapper<KeyBindResponseDTO>();
        KeyBindResponseDTO resDto = new KeyBindResponseDTO();
        resDto.setStatus("success");
        responseFromService.setResponse(null);       
         
        
        ResponseEntity<ResponseWrapper<KeyBindResponseDTO>> responseEntity = new ResponseEntity<ResponseWrapper<KeyBindResponseDTO>>(
        		responseFromService, HttpStatus.OK); 
		Mockito.when(restTemplate.exchange(Mockito.anyString(), Mockito.any(HttpMethod.class), Mockito.<RequestEntity<Void>>any(), eq(new ParameterizedTypeReference<ResponseWrapper<KeyBindResponseDTO>>() {})))
		.thenReturn(responseEntity);		
		
		KeyBindRequestDTO requestDto = new KeyBindRequestDTO();
		Map<String, Object> totpkey1 = new HashMap<String, Object>();
		totpkey1.put("key", "123456");
		requestDto.setTotpKey(totpkey1);
		
		requestDto.setTotpKey(null);
		keyBindingServiceInstance.sendBindingKey(requestDto, bindingEndpoint);	
	}
	
	@Test(expected = Exception.class)
	public void sendBindingKeyTest2() {		
        ResponseWrapper<KeyBindResponseDTO> responseFromService = new ResponseWrapper<KeyBindResponseDTO>();
        KeyBindResponseDTO resDto = new KeyBindResponseDTO();
        resDto.setStatus("success");
        responseFromService.setResponse(null);       
         
        
        ResponseEntity<ResponseWrapper<KeyBindResponseDTO>> responseEntity = new ResponseEntity<ResponseWrapper<KeyBindResponseDTO>>(
        		responseFromService, HttpStatus.BAD_REQUEST); 
		Mockito.when(restTemplate.exchange(Mockito.anyString(), Mockito.any(HttpMethod.class), Mockito.<RequestEntity<Void>>any(), eq(new ParameterizedTypeReference<ResponseWrapper<KeyBindResponseDTO>>() {})))
		.thenReturn(responseEntity);		
		
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
