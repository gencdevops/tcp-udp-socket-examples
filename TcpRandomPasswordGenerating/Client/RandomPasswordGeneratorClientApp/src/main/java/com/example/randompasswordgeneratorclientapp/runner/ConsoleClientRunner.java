package com.example.randompasswordgeneratorclientapp.runner;

import com.example.randompasswordgeneratorclientapp.exceptionutil.ExceptionUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;

@Component
public class ConsoleClientRunner implements ApplicationRunner {
@Value("${randomServer.host}")
private String host;

@Value("${randomServer.port}")
private int port;


    private boolean clientCallback() {
        Scanner kb = new Scanner(System.in);
        try(Socket socket = new Socket(host, port)) {
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            System.out.println("Count?");
            long count = Long.parseLong(kb.nextLine());
            if(count <= 0)
                return false;

            System.out.println("Number of Characters?");
            int nChars = Integer.parseInt(kb.nextLine());

            dos.writeLong(count);
            dos.writeInt(nChars);
            dos.flush();

            for(long i = 0L; i < count; ++i) {
                System.out.println("password :" + br.readLine());
            }

        }catch (Throwable ex) {
            System.err.println(ex.getMessage());
        }
        return true;

    }

    private void runClient() {
       for(;;) {
          if(!clientCallback())
              break;
       }
    }

    @Override
    public void run(ApplicationArguments args) {
        new Thread(this::runClient).start();
    }

}
