package io.mosip.totpbinderservice.dto;

import java.util.Map;

import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
public class KeyBindRequestDTO {
	
	@NotEmpty(message = "invalid totp key")
    private Map<String, Object> totpKey;
}