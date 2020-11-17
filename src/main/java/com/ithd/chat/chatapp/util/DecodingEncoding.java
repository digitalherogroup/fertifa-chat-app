package com.ithd.chat.chatapp.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.Base64;

@Component
@RequiredArgsConstructor
public class DecodingEncoding {

    public String encodeValue(String value) {
        return Base64.getUrlEncoder().encodeToString(value.getBytes());
    }

    public String decodeValue(String value) {
        byte[] decodedBytes = Base64.getUrlDecoder().decode(value);
        return new String(decodedBytes);
    }
}
