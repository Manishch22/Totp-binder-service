package io.mosip.totpbinderservice.service.Impl;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.apache.commons.codec.binary.Base32;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.OctetSequenceKey;

import io.mosip.totpbinderservice.dto.JwkDTO;
import io.mosip.totpbinderservice.dto.KeyBindRequestDTO;
import io.mosip.totpbinderservice.dto.KeyBindResponseDTO;
import io.mosip.totpbinderservice.dto.RequestWrapper;
import io.mosip.totpbinderservice.dto.ResponseWrapper;
import io.mosip.totpbinderservice.exception.BindingException;
import io.mosip.totpbinderservice.service.KeyBindingService;
import io.mosip.totpbinderservice.util.ErrorConstants;

@Service
public class KeyBindingServiceImpl implements KeyBindingService {

    @Value("${mosip.iam.binding_endpoint}")
    private String bindingEndpoint;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public KeyBindResponseDTO sendBindingKey(KeyBindRequestDTO bindRequestDTO, String header) throws BindingException {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", header);

        RequestWrapper<KeyBindRequestDTO> request = new RequestWrapper<KeyBindRequestDTO>();
        request.setRequest(bindRequestDTO);
        request.setRequestTime(LocalDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")));

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(bindingEndpoint);
        HttpEntity<RequestWrapper<KeyBindRequestDTO>> entity = new HttpEntity<>(request, headers);

        try {
        	ResponseEntity<ResponseWrapper<KeyBindResponseDTO>> responseEntity = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.POST,
                    entity, new ParameterizedTypeReference<>() {});

            if (responseEntity.getStatusCode().is2xxSuccessful() && responseEntity.getBody() != null) {
                var responseWrapper = responseEntity.getBody();
                
                if(responseWrapper.getResponse() != null) {
                    return responseWrapper.getResponse();
                }
                
                throw new BindingException(CollectionUtils.isEmpty(responseWrapper.getErrors()) ?
                		ErrorConstants.KEY_BINDING_FAILED : responseWrapper.getErrors().get(0).getErrorCode());
            }

            throw new BindingException("Error response received with status: " + responseEntity.getStatusCode());
        } catch (BindingException ex) {
        	throw ex;
        } catch (Exception e) {
            throw new BindingException(ErrorConstants.KEY_BINDING_FAILED + ": " + e.getMessage());
        }
    }

	@Override
	public JwkDTO getKey() throws BindingException {
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
