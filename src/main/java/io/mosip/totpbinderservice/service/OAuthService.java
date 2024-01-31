package io.mosip.totpbinderservice.service;

import io.mosip.totpbinderservice.dto.AccessTokenRequestDTO;
import io.mosip.totpbinderservice.dto.AccessTokenResponseDTO;
import io.mosip.totpbinderservice.exception.BindingException;

public interface OAuthService {
    AccessTokenResponseDTO getAccessToken (AccessTokenRequestDTO tokenRequest) throws BindingException;
}
