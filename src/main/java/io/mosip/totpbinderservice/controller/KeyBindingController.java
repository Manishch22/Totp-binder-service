package io.mosip.totpbinderservice.controller;

import io.mosip.totpbinderservice.dto.KeyBindRequestDTO;
import io.mosip.totpbinderservice.dto.KeyBindResponseDTO;

import io.mosip.totpbinderservice.exception.BindingException;
import io.mosip.totpbinderservice.service.KeyBindingService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping("/totp-key-bind")
    @Operation(summary = "totp key binding", description = "totp key binding")
    public KeyBindResponseDTO bindKey (@RequestBody KeyBindRequestDTO bindRequestDTO, @RequestHeader("Authorization") String bearerToken) {

        if (bindRequestDTO.getTotpKey() == null || bindRequestDTO.getTotpKey().isEmpty()) {
            throw new BindingException("The 'totpKey' map is required in the request body.");
        }

        if (!bindRequestDTO.getTotpKey().containsKey("key")) {
            throw new BindingException("The 'key' field is required in the 'totpKey' map.");
        }

        KeyBindResponseDTO response = keyBindingService.sendBindingKey(bindRequestDTO, bearerToken);

        return response;
    }

}
