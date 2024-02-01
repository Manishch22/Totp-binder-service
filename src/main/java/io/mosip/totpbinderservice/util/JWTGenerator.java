package io.mosip.totpbinderservice.util;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import io.mosip.totpbinderservice.exception.BindingException;

import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.text.ParseException;
import java.util.Base64;
import java.util.Date;

@Component
public class JWTGenerator {

	/**
	 * Generates a signed JWT using the provided parameters.
	 *
	 * @param clientId
	 * @param jwtAlgorithm
	 * @param jwtExpirationTime
	 * @param clientPrivateKey
	 * @param TotpBinderServiceServiceUrl
	 * @return Serialized string representation of the generated signed JWT.
	 * @throws BindingException
	 */
    public String generateSignedJwt(String clientId, String jwtAlgorithm, String jwtExpirationTime, String clientPrivateKey, String TotpBinderServiceServiceUrl) {
		try {
			RSAPrivateKey privateKey = parsePrivateKey(clientPrivateKey);
			JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.parse(jwtAlgorithm))
	                .type(JOSEObjectType.JWT)
	                .build();
			JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
	                .issuer(clientId)
	                .subject(clientId)
	                .audience(TotpBinderServiceServiceUrl)
	                .issueTime(new Date())
	                .expirationTime(new Date(System.currentTimeMillis() + parseExpirationTime(jwtExpirationTime)))
	                .build();
			SignedJWT signedJWT = new SignedJWT(header, claimsSet);
			signedJWT.sign(new RSASSASigner(privateKey));
			return signedJWT.serialize();
		} catch (NoSuchAlgorithmException | InvalidKeySpecException | ParseException | JOSEException e) {
			throw new BindingException(e.getMessage());
		}
    }

	/**
	 * Parses the client's private key from the provided base64-encoded string.
	 *
	 * @param clientPrivateKey
	 * @return RSAPrivateKey instance representing the parsed private key.
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @throws ParseException
	 * @throws JOSEException
	 */
    private RSAPrivateKey parsePrivateKey(String clientPrivateKey) throws NoSuchAlgorithmException, InvalidKeySpecException, ParseException, JOSEException {
        byte[] keyBytes = Base64.getDecoder().decode(clientPrivateKey);
        String jwkString = new String(keyBytes);
        JWK jwk = JWK.parse(jwkString);
        RSAKey rsaKey = (RSAKey) jwk;

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(rsaKey.toRSAPrivateKey().getEncoded());

        return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
    }

	/**
	 * Parses the expiration time from string to milliseconds.
	 *
	 * @param expirationTime
	 * @return The expiration time in milliseconds.
	 */
    private static long parseExpirationTime(String expirationTime) {
        long duration = Long.parseLong(expirationTime);
        return duration * 1000; // seconds to milliseconds
    }
}
