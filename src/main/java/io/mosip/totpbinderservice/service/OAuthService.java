package io.mosip.totpbinderservice.service;

import com.nimbusds.jose.JOSEException;
import io.mosip.totpbinderservice.dto.AccessTokenRequestDTO;
import io.mosip.totpbinderservice.dto.AccessTokenResponseDTO;
import io.mosip.totpbinderservice.exception.BindingException;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;

public interface OAuthService {
    AccessTokenResponseDTO getAccessToken (AccessTokenRequestDTO tokenRequest) throws BindingException;
}
