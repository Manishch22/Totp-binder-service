package io.mosip.totpbinderservice.controller;

import io.mosip.totpbinderservice.dto.JwkDTO;
import io.mosip.totpbinderservice.dto.KeyBindRequestDTO;
import io.mosip.totpbinderservice.dto.KeyBindResponseDTO;
import io.mosip.totpbinderservice.dto.ResponseWrapper;
import io.mosip.totpbinderservice.service.KeyBindingService;
import io.swagger.v3.oas.annotations.Operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/binding")
public class KeyBindingController {

    @Autowired
    private KeyBindingService keyBindingService;

    /**
     * Handles the TOTP key binding operation.
     *
     * @param bindRequestDTO
     * @param bearerToken
     * @return ResponseWrapper containing the response from the TOTP key binding operation.
     */
    @PostMapping("/totp-key-bind")
    @Operation(summary = "totp key binding", description = "totp key binding")
    public ResponseWrapper<KeyBindResponseDTO> bindKey (@RequestBody KeyBindRequestDTO bindRequestDTO, @RequestHeader("Authorization") String bearerToken) {
    	ResponseWrapper<KeyBindResponseDTO> response = new ResponseWrapper<KeyBindResponseDTO>();
        response.setResponse(keyBindingService.sendBindingKey(bindRequestDTO, bearerToken));
        return response;
    }

    /**
     * Handles the TOTP secret key generation operation.
     *
     * @return ResponseWrapper containing the generated JWK (JSON Web Key).
     */
    @GetMapping("/key")
    @Operation(summary = "totp secret key generation", description = "totp secret key generation")
    public ResponseWrapper<JwkDTO> generateKey() {
    	ResponseWrapper<JwkDTO> response = new ResponseWrapper<JwkDTO>();
    	response.setResponse(keyBindingService.getKey());
		return response;
    }
}