package io.mosip.totpbinderservice.dto;

import lombok.Data;

@Data
public class AccessTokenResponseDTO {
    private String accessToken;
    private String expiresIn;
    private String idToken;
}
