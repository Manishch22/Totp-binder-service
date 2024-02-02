package io.mosip.totpbinderservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.mosip.totpbinderservice.dto.AccessTokenRequestDTO;
import io.mosip.totpbinderservice.dto.AccessTokenResponseDTO;
import io.mosip.totpbinderservice.dto.ResponseWrapper;
import io.mosip.totpbinderservice.service.OAuthService;

@RestController
@RequestMapping("/oauth")
public class OAuthController {

    @Autowired
    private OAuthService oAuthService;

    /**
     * Handles the request for obtaining an OAuth access token.
     *
     * @param tokenRequest
     * @return ResponseWrapper containing the response from the OAuth token retrieval operation.
     */
    @PostMapping("/token")
    public ResponseWrapper<AccessTokenResponseDTO> getToken(@RequestBody AccessTokenRequestDTO tokenRequest) {
    	ResponseWrapper<AccessTokenResponseDTO> response = new ResponseWrapper<AccessTokenResponseDTO>();
    	response.setResponse(oAuthService.getAccessToken(tokenRequest));
        return response;
    }
}