package com.rest.automation.order.service;

import com.rest.automation.admin.dto.OrderDto;
import com.rest.automation.order.coders.MessageDecoder;
import com.rest.automation.order.coders.MessageEncoder;
import com.rest.automation.order.messages.Message;
import com.rest.automation.order.messages.OrderMessage;
import lombok.extern.slf4j.Slf4j;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@ServerEndpoint(value = "/websocket/order",
        encoders = MessageEncoder.class,
        decoders = MessageDecoder.class)
public class OrderEndpoint {

    Session session;

    private static List<OrderEndpoint> clients = new CopyOnWriteArrayList<OrderEndpoint>();

    private static List<Message> orderList = new CopyOnWriteArrayList<>();


    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        clients.add(this);
    }

    @OnClose
    public void onClose(Session session) {
        this.session = session;
        clients.remove(this);
    }

    @OnError
    public void onError(Session session, Throwable t) {
        try {
            session.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        t.printStackTrace();
    }

    @OnMessage
    public void onMessage(Message message) {
        if(message instanceof OrderMessage)
            processMessage(message);
        else
            log.error("Unknown message.");
    }

    private void processMessage(Message message) {
        orderList.add(message);
        OrderMessage orderMessage = (OrderMessage) message;
        Response response = RestClient.getInstance().createOrder(orderMessage.getOrderDto());
        if(response.getStatus()!=200) {
            log.warn("Could not created order");
        } else {
            OrderDto order = response.readEntity(OrderDto.class);
            try {
                ((OrderMessage) message).setOrderDto(order);
                this.session.getBasicRemote().sendObject(message);
                log.info("Order created with id:");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (EncodeException e) {
                e.printStackTrace();
            }
        }
    }
}
