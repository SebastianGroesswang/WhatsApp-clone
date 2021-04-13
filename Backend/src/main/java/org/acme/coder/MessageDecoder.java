package org.acme.coder;


import com.google.gson.Gson;
import org.acme.dto.MessageDto;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

public class MessageDecoder implements Decoder.Text<MessageDto> {

    private static Gson gson = new Gson();

    @Override
    public MessageDto decode(String s) throws DecodeException {
        return gson.fromJson(s, MessageDto.class);
    }

    @Override
    public boolean willDecode(String s) {
        return s != null;
    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
}
