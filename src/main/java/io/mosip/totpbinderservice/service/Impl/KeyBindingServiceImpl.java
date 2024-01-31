package io.mosip.totpbinderservice.service.Impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.OctetSequenceKey;

import io.mosip.totpbinderservice.dto.JwkDTO;
import io.mosip.totpbinderservice.dto.KeyBindRequestDTO;
import io.mosip.totpbinderservice.dto.KeyBindResponseDTO;
import io.mosip.totpbinderservice.dto.RequestWrapper;
import io.mosip.totpbinderservice.exception.BindingException;
import io.mosip.totpbinderservice.service.KeyBindingService;

import org.apache.commons.codec.binary.Base32;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

@Service
public class KeyBindingServiceImpl implements KeyBindingService {

    @Value("${mosip.iam.binding_endpoint}")
    private String bindingEndpoint;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public KeyBindResponseDTO sendBindingKey(KeyBindRequestDTO bindRequestDTO, String header) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", header);

        RequestWrapper<KeyBindRequestDTO> request = new RequestWrapper<KeyBindRequestDTO>();
        request.setRequest(bindRequestDTO);
        request.setRequestTime(LocalDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")));

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(bindingEndpoint);
        HttpEntity<RequestWrapper<KeyBindRequestDTO>> entity = new HttpEntity<>(request, headers);
        ResponseEntity<String> responseEntity = null;

        try {
            responseEntity = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.POST, entity, String.class);

            if (responseEntity.getStatusCode().is2xxSuccessful() && responseEntity.getBody() != null) {
                KeyBindResponseDTO bindingResponse = objectMapper.convertValue(responseEntity.getBody(), KeyBindResponseDTO.class);
                return bindingResponse;
            }

        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw new BindingException(ex.toString());
        }

        return new KeyBindResponseDTO();
    }

	@Override
	public JwkDTO getKey() {
		try {
			KeyGenerator gen = KeyGenerator.getInstance("AES");
			gen.init(128);
	    	SecretKey aesKey = gen.generateKey();

	    	JWK jwk = new OctetSequenceKey.Builder(aesKey)
	    	    .keyID(UUID.randomUUID().toString())
	    	    .algorithm(EncryptionMethod.A128GCM)
	    	    .build();
	    	JwkDTO jwkDTO = new JwkDTO();
	    	jwkDTO.setKty((String)jwk.getRequiredParams().get("kty"));
	    	jwkDTO.setKid(jwk.getKeyID());
	    	
	    	String key = (String)jwk.getRequiredParams().get("k");
	    	Base32 base32 = new Base32();
	        String encodedString = base32.encodeToString(key.getBytes());
	    	jwkDTO.setKey(encodedString);
	    	
	    	return jwkDTO;
		} catch (NoSuchAlgorithmException e) {
			throw new BindingException("key generation failed");
		}
	}
}
