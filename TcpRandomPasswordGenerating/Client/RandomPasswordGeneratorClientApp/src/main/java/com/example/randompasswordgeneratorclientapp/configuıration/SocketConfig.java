package com.example.randompasswordgeneratorclientapp.configuıration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.Socket;

@Component
public class SocketConfig {

    @Bean
    @Scope("prototype")
    public Socket createSocket(@Value("${randomServer.host}") String host,
                               @Value("${randomServer.port}") int port) throws IOException {

        return new Socket(host, port);
    }


}
