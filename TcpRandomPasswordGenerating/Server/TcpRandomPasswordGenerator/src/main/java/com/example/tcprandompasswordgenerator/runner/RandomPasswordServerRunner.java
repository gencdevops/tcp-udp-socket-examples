package com.example.tcprandompasswordgenerator.runner;

import com.example.tcprandompasswordgenerator.exceptionutil.ExceptionUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.concurrent.ExecutorService;

@Component
public class RandomPasswordServerRunner implements ApplicationRunner {
    private final ServerSocket serverSocket;
    private final ExecutorService executorService;

    private void handleClient(Socket clientSocket) {
        executorService.execute(() -> generatePasswords(clientSocket));
    }

    private void sendPassWord(long count, int nChars, BufferedWriter bw) throws IOException {


        for (int i = 0; i < count; ++i) {
            Random random = new Random();
            StringBuilder sb = new StringBuilder();
            for (int k = 0; k < nChars; ++k) {
                char ch = (char) (random.nextInt(80 - 65) + 65);
                sb.append(ch);
            }
            String text = sb.toString() + "\r\n";
            System.out.println(text);
            bw.write(text);
            bw.flush();
        }
    }

    private void generatePasswords(Socket clientSocket) {
        System.out.printf("Host: %s, Port: %d, Local Port: %d%n", clientSocket.getInetAddress().getHostAddress(),
                clientSocket.getPort(), clientSocket.getLocalPort());

        try (clientSocket) {
            DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

            long count = dis.readLong();
            int nChars = dis.readInt();
            sendPassWord(count, nChars, bw);
        } catch (Throwable ex) {
            //System.err.println("generatePasswords : " + ex.getMessage());
        }
    }

    private void acceptClient() throws IOException {
        System.out.println("Server client'ını bekliyor...");
        handleClient(serverSocket.accept());
    }

    private void runForAccept() {
        for (; ; ) {
            ExceptionUtil.subscribeRunnable(this::acceptClient, ex -> System.err.println(ex.getMessage()));
        }
    }

    private void runServerCallback() {
        runForAccept();
    }

    public RandomPasswordServerRunner(ServerSocket serverSocket,
                                      @Qualifier("executorService.cached") ExecutorService executorService) {
        this.serverSocket = serverSocket;
        this.executorService = executorService;
    }


    @Override
    public void run(ApplicationArguments args) {
        executorService.execute(this::runServerCallback);
    }
}
