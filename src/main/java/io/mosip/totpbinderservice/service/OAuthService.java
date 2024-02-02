package io.mosip.totpbinderservice.service;

import io.mosip.totpbinderservice.dto.AccessTokenRequestDTO;
import io.mosip.totpbinderservice.dto.AccessTokenResponseDTO;
import io.mosip.totpbinderservice.exception.BindingException;

public interface OAuthService {

    /**
     * Retrieves the access token from the E-signet module based on the provided AccessTokenRequestDTO.
     *
     * @param tokenRequest
     * @return AccessTokenResponseDTO
     * @throws BindingException
     */
    AccessTokenResponseDTO getAccessToken (AccessTokenRequestDTO tokenRequest) throws BindingException;
}
