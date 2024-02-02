# TOTP Key - User Identity Binding Service

## Info: 

This service is responsible for generating TOTP key and binding to the UIN/VID of the resident. This service has been implemented based on the requirements present [here](https://github.com/mosip/hackathon/blob/master/Decode-2023/totp_auth_for_esignet/TOTP_Implementation_for_MOSIP_eSignet.pdf). 

## Configurations:

  jwt.algorithm=RS256 \
	mosip.iam.module.grant_type=authorization_code \
	mosip.iam.module.clientID=999client \
  mosip.iam.module.clientassertiontype=urn:ietf:params:oauth:client-assertion-type:jwt-bearer \
	mosip.iam.token_endpoint=https://esignet.onpremdev.idencode.link/v1/esignet/oauth/token \
	mosip.iam.binding_endpoint=https://api-internal.onpremdev.idencode.link/v1/esignet/binding/totp-key-binding \
	mosip.iam.module.redirecturi=https://totp-binder-service.onpremdev.idencode.link/qrcode \
  mosip.iam.module.privatekey=  \
  mosip.iam.module.token.endpoint.private-key-jwt.expiry.seconds=180 \

# Usage of TOTP 

## Info:

For usage of TOTP, customized eSignet application. Existing eSignet application has been customized based on the guidelines provided [here](https://github.com/mosip/hackathon/blob/master/Decode-2023/totp_auth_for_esignet/TOTP_Implementation_for_MOSIP_eSignet.pdf).

## Configurations:

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
	}
}"

  
