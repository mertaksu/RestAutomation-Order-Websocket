package com.rest.automation.order.service;


import com.rest.automation.admin.dto.OrderDto;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class RestClient {

    private static RestClient restClient;

    private RestClient(){

    }

    public static RestClient getInstance(){
        if(restClient==null) {
            restClient = new RestClient();
        }
        return restClient;
    }

    private static final String REST_URL = "http://localhost:8080";
    private static final String REST_PATH = "/order";

    private Client client = ClientBuilder.newClient();

    private static final String username="mertaksu";
    private static final String password="1234Aa";
    private static final String usernameAndPassword = username + ":" + password;
    private static final String authorizationHeaderValue = "Basic " + java.util.Base64.getEncoder().encodeToString( usernameAndPassword.getBytes() );

    public Response createOrder(OrderDto order) {
        return client.
                target(REST_URL).
                path(REST_PATH).
                request(MediaType.APPLICATION_JSON).
                header("Authorization",authorizationHeaderValue).
                post(Entity.entity(order,MediaType.APPLICATION_JSON));
    }
}
