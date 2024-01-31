package io.mosip.totpbinderservice.controller;

import com.nimbusds.jose.JOSEException;

import io.mosip.totpbinderservice.dto.*;

import io.mosip.totpbinderservice.service.OAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;

@RestController
@RequestMapping("/oauth")
public class OAuthController {

    @Autowired
    private OAuthService oAuthService;

    @PostMapping("/token")
    public AccessTokenResponseDTO getToken(@RequestBody AccessTokenRequestDTO tokenRequest) throws NoSuchAlgorithmException, InvalidKeySpecException, JOSEException, ParseException {

        return oAuthService.getAccessToken(tokenRequest);
    }
}
