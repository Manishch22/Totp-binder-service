package io.mosip.totpbinderservice.DTO;

import java.util.Map;

import javax.validation.constraints.NotEmpty;

public class KeyBindRequestDTO {
	@NotEmpty(message = "invalid totp key")
    private Map<String, Object> totpKey;

    public Map<String, Object> getTotpKey() {
        return totpKey;
    }

    public void setTotpKey(Map<String, Object> key) {
        this.totpKey = key;
    }
}