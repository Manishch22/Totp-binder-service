package io.mosip.totpbinderservice.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.mosip.totpbinderservice.dto.AccessTokenRequestDTO;
import io.mosip.totpbinderservice.dto.AccessTokenResponseDTO;
import io.mosip.totpbinderservice.dto.AccessTokenResponse;
import io.mosip.totpbinderservice.exception.BindingException;
import io.mosip.totpbinderservice.service.OAuthService;
import io.mosip.totpbinderservice.util.Constants;
import io.mosip.totpbinderservice.util.JWTGenerator;

@Service
public class OAuthServiceImpl implements OAuthService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private JWTGenerator jwtGenerator;

    @Value("${mosip.iam.module.grant_type}")
    private String grantType;

    @Value("${mosip.iam.module.clientid}")
    private String clientID;

    @Value("${mosip.iam.module.clientassertiontype}")
    private String clientAssertionType;

    @Value("${mosip.iam.token_endpoint}")
    private String tokenEndpoint;

    @Value("${mosip.iam.module.redirecturi}")
    private String redirectURI;

    @Value("${mosip.iam.module.privatekey}")
    private String privateKey;

    @Value("${jwt.algorithm}")
    private String jwtAlgorithm;

    @Value("${mosip.iam.module.token.endpoint.private-key-jwt.expiry.seconds}")
    private String jwtExpiryTime;

    @Override
    public AccessTokenResponseDTO getAccessToken(AccessTokenRequestDTO tokenRequest) throws BindingException {

        // Generate a signed JWT for authentication
        String signedJWT = jwtGenerator.generateSignedJwt(clientID, jwtAlgorithm, jwtExpiryTime, privateKey, tokenEndpoint);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add(Constants.GRANT_TYPE, grantType);
        map.add(Constants.CLIENT_ID, clientID);
        map.add(Constants.CLIENT_ASSERTION, signedJWT);
        map.add(Constants.CLIENT_ASSERTION_TYPE, clientAssertionType);
        map.add(Constants.CODE, tokenRequest.getCode());
        map.add(Constants.REDIRECT_URI, redirectURI);

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(tokenEndpoint);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);

        try {
        	ResponseEntity<String> responseEntity = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.POST, entity, String.class);
            
            if (responseEntity.getStatusCode().is2xxSuccessful() && responseEntity.getBody() != null) {
            	AccessTokenResponse accessTokenResponse = objectMapper.readValue(responseEntity.getBody(), AccessTokenResponse.class);
                
            	AccessTokenResponseDTO accessTokenResponseDTO = new AccessTokenResponseDTO();
                accessTokenResponseDTO.setAccessToken(accessTokenResponse.getAccess_token());
                accessTokenResponseDTO.setExpiresIn(accessTokenResponse.getExpires_in());
                accessTokenResponseDTO.setIdToken(accessTokenResponse.getId_token());
                return accessTokenResponseDTO;
            }
            
            throw new BindingException("Error response received with status: " + responseEntity.getStatusCode());
        } catch (BindingException ex) {
        	throw ex;
        } catch (Exception e) {
        	throw new BindingException("unable to get access token: " + e.getMessage());
        }
    }
}
