package com.example.tcprandompasswordgenerator.runner;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

@Component
public class RandomPasswordServerRunner implements ApplicationRunner {
    private final ServerSocket serverSocket;
    private final ExecutorService executorService;

    private void runServerCallback()  {
       for(;;) {
           try {
               System.out.println("Server client'ını bekliyor...");
               Socket clientSocket = serverSocket.accept();


               System.out.println("Client bağlandı : " + clientSocket.getInetAddress().getHostAddress()
                       + " " + clientSocket.getPort());
           }catch (IOException ex) {
               ex.printStackTrace();
           }

       }
    }
    public RandomPasswordServerRunner(ServerSocket serverSocket,
                                      @Qualifier("executorService.cached") ExecutorService executorService) {
        this.serverSocket = serverSocket;
        this.executorService = executorService;
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
    executorService.execute(this::runServerCallback);
    }
}
