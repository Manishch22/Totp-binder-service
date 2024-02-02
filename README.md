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

## API Documentation:
Please refer TOTP_Binder_Service_API_Documentation.docx present [here](docs/) for API documentation.

## Deployment:

Use the helm charts present [here](helm/) for deployment. Add the host name according to deploying environment domain in global configuration map.

# TOTP Key Identity Binding UI:

This user interface will bind TOTP key with resident/user UIN/VID. This code is available totp-binder-ui repo.

## UI/Environment Configurations :
```
ENV TOTP_BINDER_SERVICE_URL=$totp_binder_service_url
ENV CLIENT_ID=$client_id
// valid values are 6(default) and 8
ENV TOTP_DIGITS=$totp_digits
ENV TOTP_PERIOD=$totp_period
ENV TOTP_BINDER_UI_PUBLIC_URL=$totp_binder_ui_PublicUrl
// valid values are SHA1(default), SHA256, SHA512
ENV TOTP_ALGORITHM=$totp_algorithm
```

# Usage of TOTP 

## eSsignet customization:

For usage of TOTP, customized eSignet application. Existing eSignet application has been customized based on the guidelines provided [here](https://github.com/mosip/hackathon/blob/master/Decode-2023/totp_auth_for_esignet/TOTP_Implementation_for_MOSIP_eSignet.pdf).

### Configuration modifications:
The following configuration keys should be added/modified in **esignet-default.properties**.
```
  // mock identiy key binding url
  mosip.esignet.mock.authenticator.key-binding-url=${mosip.api.public.mock.url}/v1/mock-identity-system/key-binding
```

  Along with above configuration have to modify amr-acr-mapping.json file to allow TOTP based authentication as below.
```
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

### Deployment of eSignet service and ui:
Please build the latest mock-esignet-integration-impl and put the built jar(or the provided jar) in the artifactory.
For UI modified the public/locales/en.json for Login with TOTP placeholders, so update the same in the artifactory 

## Mock-identity customization:

Used mock identity to test this TOTP based authentication. MOCK Identity application has been customized to support TOTP based authentication as per [requirements](https://github.com/mosip/hackathon/blob/master/Decode-2023/totp_auth_for_esignet/TOTP_Implementation_for_MOSIP_eSignet.pdf)

### Configuration modifications:

The following configuration keys should be added/modified in **mock-identity-system-default.properties**.
```
mosip.mock.totp-bind.security.algorithm-name=AES/ECB/PKCS5Padding \
// this is reference id used to encrypt and decrypt the TOTP key
mosip.mock.totp-bind.security.secretkey.reference-id=TOTP_BIND \
// Time To Live value
mosip.mock.totp-validation-period=30 \
mosip.mock.totp-transmission-delay=0 \
//valid values are 6(default) and 8
mosip.mock.totp-digits=6 \
//valid values are HmacSHA1(default), HmacSHA256, HmacSHA512
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
  * Relying party configuration - This includes creation of relying party with required policies and creation of OIDC client, please use Relying_party_and_OIDC_client .postman_collection present [here](docs/).
  * For OIDC client creation required public key in JWK format. This can be generated [here](https://mkjwk.org/).
  * Modify acrs(add mosip:idp:acr:time-generated-code) for the relying party OIDC client(health service or resident portal) to enable the TOTP based authentication.

**Note:**
 In postman collection update the redirect uri(totp binder ui url) in create client api(last api in collection).

Post creation of above data, update the following configurations.
 * mosip.iam.module.clientID and mosip.iam.module.privatekey in totp-binder-service configurations.
 * client Id needs to be updated in UI environment variables.

Once above configurations are over, please follow TOTP_User_Guide.docx present [here](docs/) to test the application.





 
 


  
