package io.mosip.totpbinderservice.dto;

import lombok.Data;

@Data
public class JwkDTO {

	private String kty;
	private String kid;
	private String key;
}
