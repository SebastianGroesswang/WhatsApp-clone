package org.acme.coder;

import com.google.gson.Gson;
import org.acme.dto.MessageDto;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class MessageEncoder implements Encoder.Text<MessageDto> {

    private static Gson gson = new Gson();

    @Override
    public String encode(MessageDto messageDto) throws EncodeException {
        return gson.toJson(messageDto, MessageDto.class);
    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
}
