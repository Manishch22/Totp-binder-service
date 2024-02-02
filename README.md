# TOTP Key - User Identity Binding Service

## Info: 

This service is responsible for generating TOTP key and binding to the UIN/VID of the resident. This service has been implemented based on the requirements present [here](https://github.com/mosip/hackathon/blob/master/Decode-2023/totp_auth_for_esignet/TOTP_Implementation_for_MOSIP_eSignet.pdf). 

## Configurations:

 ```
  jwt.algorithm=RS256 \
  mosip.iam.module.grant_type=authorization_code \
  // OIDC relying partId 
  mosip.iam.module.clientID=999client \
  mosip.iam.module.clientassertiontype=urn:ietf:params:oauth:client-assertion-type:jwt-bearer \
  // eSignet end point to get the token
  mosip.iam.token_endpoint={{domainUrl}}/v1/esignet/oauth/token \
  // TOTP binding api 
  mosip.iam.binding_endpoint={{domainUrl}}/v1/esignet/binding/totp-key-binding \
  // TOTP Key and Identity binder UI url
  mosip.iam.module.redirecturi={{totpBinderUIUrl}}/qrcode \
  // private key of relying party
  mosip.iam.module.privatekey=  \
  mosip.iam.module.token.endpoint.private-key-jwt.expiry.seconds=180
```
**Note :**
 Configuration file name for this application should be 'totp-binder-service-default.properties'.

## UI Configurations :
```
```

# Usage of TOTP 

## Info:

For usage of TOTP, customized eSignet application. Existing eSignet application has been customized based on the guidelines provided [here](https://github.com/mosip/hackathon/blob/master/Decode-2023/totp_auth_for_esignet/TOTP_Implementation_for_MOSIP_eSignet.pdf).

## Configurations:
The following configuration keys should be added/modified in **esignet-default.properties**.
```
  // mock identiy key binding url
  mosip.esignet.mock.authenticator.key-binding-url=${mosip.api.public.mock.url}/v1/mock-identity-system/key-binding

  Along with above configuration have to modify amr-acr-mapping.json file to allow TOTP based authentication as below.

    "{
	"amr": {
		"PIN": [
			{
				"type": "PIN"
			}
		],
		"OTP": [
			{
				"type": "OTP"
			}
		],
		"TOTP": [
			{
				"type": "TOTP" -- newly added one
			}
		],
		"Wallet": [
			{
				"type": "WLA"
			}
		],
		"L1-bio-device": [
			{
				"type": "BIO",
				"count": 1
			}
		]
	},
	"acr_amr": {
		"mosip:idp:acr:static-code": [
			"PIN"
		],
		"mosip:idp:acr:generated-code": [
			"OTP"
		],
		"mosip:idp:acr:time-generated-code": [
			"TOTP" -- newly added one
		],
		"mosip:idp:acr:linked-wallet": [
			"Wallet"
		],
		"mosip:idp:acr:biometrics": [
			"L1-bio-device"
		]
	}}"
 ```

# mock-identity customization:

As of now used mock identity to test this TOTP based authentication. MOCK Identity application has been customized to support TOTP based authentication as per [requirements](https://github.com/mosip/hackathon/blob/master/Decode-2023/totp_auth_for_esignet/TOTP_Implementation_for_MOSIP_eSignet.pdf)

## Configurations:

The following configuration keys should be added/modified as below in **mock-identity-system-default.properties**.
```
mosip.mock.totp-bind.security.algorithm-name=AES/ECB/PKCS5Padding \
// this is reference id used to encrypt and decrypt the TOTP key
mosip.mock.totp-bind.security.secretkey.reference-id=TOTP_BIND \
// Time To Live value
mosip.mock.totp-validation-period=30 \
mosip.mock.totp-transmission-delay=0 \
mosip.mock.totp-digits=6 \
mosip.mock.totp-generation-algo=HmacSHA1
```
**Note :**
 Following properties should be in sync between TOTP binder UI and mock identity.

 ```
mosip.mock.totp-validation-period=30 \
mosip.mock.totp-transmission-delay=0 \
mosip.mock.totp-digits=6 \
mosip.mock.totp-generation-algo=HmacSHA1
```
**Notes:**
   * OTP manager not used to validate the TOTP as this required changes in MOSIP kernel core. Post discussion with the MOSIP team, implemented TOTP validate validations in mock identity itself.
   * Binding details have not stored in registry as bind wallet due to not having public key and certificate.
   * Tested the TOTP based authetication with health service portal by using google and microsoft authenticators.
# How To Test:

### Prerequisites
  * Relying party configuration - This includes creation of relying party with required policies and creation of OIDC client.
  * For OIDC client creation required public key in JWK format. This can be generated [here](https://mkjwk.org/).
  * Update acr(mosip:idp:acr:time-generated-code) for the relying party OIDC client(health service or resident portal) to enable the TOTP based authentication.

**Notes:**
 In postman collection update the redirect uri(totp binder ui url) in create client api(last api in collection).

Post creation of above data, update the following configurations.
 * mosip.iam.module.clientID and mosip.iam.module.privatekey in totp-binder-service configurations.
 * client Id needs to be updated in UI environment variables.

Once above configurations are over, please follow this [user manual]() to test the application.





 
 


  
