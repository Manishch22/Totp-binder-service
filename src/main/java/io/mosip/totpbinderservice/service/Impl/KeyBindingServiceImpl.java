package io.mosip.totpbinderservice.service.Impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.mosip.totpbinderservice.dto.KeyBindRequestDTO;
import io.mosip.totpbinderservice.dto.KeyBindResponseDTO;
import io.mosip.totpbinderservice.dto.RequestWrapper;
import io.mosip.totpbinderservice.dto.ResponseWrapper;
import io.mosip.totpbinderservice.exception.BindingException;
import io.mosip.totpbinderservice.service.KeyBindingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Service
public class KeyBindingServiceImpl implements KeyBindingService {

    @Value("${mosip.iam.binding_endpoint}")
    private String bindingEndpoint;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public KeyBindResponseDTO sendBindingKey(KeyBindRequestDTO bindRequestDTO, String header) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", header);

        RequestWrapper<KeyBindRequestDTO> request = new RequestWrapper<KeyBindRequestDTO>();
        request.setRequest(bindRequestDTO);
        request.setRequestTime(LocalDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")));

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(bindingEndpoint);
        HttpEntity<RequestWrapper<KeyBindRequestDTO>> entity = new HttpEntity<>(request, headers);
        ResponseEntity<String> responseEntity = null;

        try {
            responseEntity = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.POST, entity, String.class);

            if (responseEntity.getStatusCode().is2xxSuccessful() && responseEntity.getBody() != null) {
                KeyBindResponseDTO bindingResponse = objectMapper.convertValue(responseEntity.getBody(), KeyBindResponseDTO.class);
                System.out.println("Token bind confirmed");
                return bindingResponse;
            }

        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw new BindingException(ex.toString());
        }

        return new KeyBindResponseDTO();
    }
}
