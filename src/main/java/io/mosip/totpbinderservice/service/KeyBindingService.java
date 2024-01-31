package io.mosip.totpbinderservice.service;

import io.mosip.totpbinderservice.dto.JwkDTO;
import io.mosip.totpbinderservice.dto.KeyBindRequestDTO;
import io.mosip.totpbinderservice.dto.KeyBindResponseDTO;

public interface KeyBindingService {
    KeyBindResponseDTO sendBindingKey (KeyBindRequestDTO bindRequestDTO, String header) ;
    JwkDTO getKey();
}
