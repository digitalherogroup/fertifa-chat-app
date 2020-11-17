package com.ithd.chat.chatapp.image;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Properties;

@RestController
@PropertySource("classpath:application.properties")
public class UploadImageManager {

    @Value("${uploadlinkValueEndPoint}")
    private String urlValue;

    @Produces("application/json")
    @ResponseBody
    public Map<String, Object> uploadImage(MultipartFile file) throws IOException {

        Resource resource = file.getResource();

        LinkedMultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
        parts.add("file", resource);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
        httpHeaders.set("Allow", "PUT,POST");

        HttpEntity<LinkedMultiValueMap<String, Object>> httpEntity = new HttpEntity<>(parts, httpHeaders);
        RestTemplate restTemplate = new RestTemplate();

        String response = restTemplate.exchange("http://chat.fertifabenefits.com/upload/uploadFile", HttpMethod.POST, httpEntity,String.class).getBody();
        return null;
    }

    public Map<String, Object> convertToMaps(ImageUploadDto t) {
        String responseJson = new Gson().toJson(t);
        Type type = new TypeToken<Map<String, Object>>() {
        }.getType();
        return new Gson().fromJson(responseJson, type);
    }
}
