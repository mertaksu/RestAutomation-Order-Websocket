package com.rest.automation.order.coders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.rest.automation.order.messages.Message;
import com.rest.automation.order.messages.OrderMessage;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

public class MessageDecoder implements Decoder.Text<Message> {

    private static final int ORDER_MESSAGE = 0;

    @Override
    public Message decode(String message) throws DecodeException {
        Message msg = null;

        if(willDecode(message)) {
            try {
                Gson gson = new Gson();
                Message message1 = gson.fromJson(message,Message.class);
                ObjectMapper objectMapper = new ObjectMapper();

                int type = message1.getType();

                switch (type) {
                    case ORDER_MESSAGE:
                    msg = objectMapper.readValue(message, OrderMessage.class);
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return msg;
    }

    @Override
    public boolean willDecode(String s) {
        return true;
    }

    @Override
    public void init(EndpointConfig config) {

    }

    @Override
    public void destroy() {

    }
}
