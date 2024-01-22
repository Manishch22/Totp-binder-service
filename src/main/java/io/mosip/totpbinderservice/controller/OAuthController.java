package io.mosip.totpbinderservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JOSEException;

import io.mosip.totpbinderservice.DTO.*;
import io.mosip.totpbinderservice.helper.JWTGenerator;
import io.mosip.totpbinderservice.helper.Constants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;

@RestController
@RequestMapping("/oauth")
public class OAuthController {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired(required = false)
    @Qualifier("selfTokenRestTemplate")
    private RestTemplate selfTokenRestTemplate;

    @Autowired
    private Environment environment;

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

    @PostMapping("/token")
    public AccessTokenResponseDTO getToken(@RequestBody AccessTokenRequestDTO tokenRequest) throws NoSuchAlgorithmException, InvalidKeySpecException, JOSEException, ParseException {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add(Constants.GRANT_TYPE, grantType);
        map.add(Constants.CLIENT_ID, clientID);

        String signedJWT = JWTGenerator.generateSignedJwt(clientID, jwtAlgorithm, jwtExpiryTime, privateKey, tokenEndpoint);

        map.add(Constants.CLIENT_ASSERTION, signedJWT);
        map.add(Constants.CLIENT_ASSERTION_TYPE, clientAssertionType);

        map.add(Constants.CODE, tokenRequest.getCode());
        map.add(Constants.REDIRECT_URI, redirectURI);

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(tokenEndpoint);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);
        ResponseEntity<String> responseEntity = null;

        try {
            responseEntity = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.POST,
                    entity, String.class);

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            // handle exceptions
        }
        AccessTokenResponse accessTokenResponse = null;
        try {
            accessTokenResponse = objectMapper.readValue(responseEntity.getBody(), AccessTokenResponse.class);
        } catch (IOException exception) {
            // handle exceptions
        }
        AccessTokenResponseDTO accessTokenResponseDTO = new AccessTokenResponseDTO();
        accessTokenResponseDTO.setAccessToken(accessTokenResponse.getAccess_token());
        accessTokenResponseDTO.setExpiresIn(accessTokenResponse.getExpires_in());
        accessTokenResponseDTO.setIdToken(accessTokenResponse.getId_token());

        System.out.println("Token Recieved");

        return accessTokenResponseDTO;
    }
}
