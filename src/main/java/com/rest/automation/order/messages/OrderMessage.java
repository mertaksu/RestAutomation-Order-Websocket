package com.rest.automation.order.messages;

import com.rest.automation.admin.dto.OrderDto;

public class OrderMessage extends Message {

    private OrderDto orderDto;

    public OrderDto getOrderDto() {
        return orderDto;
    }

    public void setOrderDto(OrderDto orderDto) {
        this.orderDto = orderDto;
    }

}
