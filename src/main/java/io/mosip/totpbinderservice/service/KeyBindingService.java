package io.mosip.totpbinderservice.service;

import io.mosip.totpbinderservice.dto.JwkDTO;
import io.mosip.totpbinderservice.dto.KeyBindRequestDTO;
import io.mosip.totpbinderservice.dto.KeyBindResponseDTO;
import io.mosip.totpbinderservice.exception.BindingException;

public interface KeyBindingService {

    /**
     * Sends a key to the E-signet module for key binding with VID.
     *
     * @param bindRequestDTO
     * @param header
     * @return KeyBindResponseDTO
     * @throws BindingException
     */
    KeyBindResponseDTO sendBindingKey (KeyBindRequestDTO bindRequestDTO, String header) throws BindingException ;

    /**
     * Generates a JSON Web Key (JWK) for binding purposes.
     *
     * @return JwkDTO
     * @throws BindingException
     */
    JwkDTO getKey() throws BindingException;
}