package org.ServerManager;

import java.io.*;

public class ServerManager {
    private final String serverDirectory = "serverbin\\bedrock-server-1.21.132.3\\";
    private final String serverPath = "serverbin\\bedrock-server-1.21.132.3\\bedrock_server.exe";

    private Process bedrockServer;
    private BufferedWriter serverWriter;
    private BufferedReader serverReader;

    public void writeServer(String command) {
        try {
            if(command.equals("exit server"))
                stopServer();
            else {
                this.serverWriter.write(command);
                this.serverWriter.newLine();
                this.serverWriter.flush();
            }
        }
        catch (Exception e) {
            System.out.println("[ServerManager]: Error writing command to server");
        }
    }

    public void startServer() throws IOException{
        if (this.bedrockServer != null && this.bedrockServer.isAlive()) {
            System.out.println("[Playit Manager] An instance is already running!");
            return;
        }

        ProcessBuilder pb = new ProcessBuilder(serverPath);
        pb.directory(new File(serverDirectory));
        pb.redirectErrorStream(true);
        this.bedrockServer = pb.start();

        this.serverWriter = new BufferedWriter(new OutputStreamWriter(this.bedrockServer.getOutputStream()));
        this.serverReader = new BufferedReader(new InputStreamReader(this.bedrockServer.getInputStream()));

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (this.bedrockServer != null && this.bedrockServer.isAlive()) {
                System.out.println("Java is shutting down. Killing Minecraft Server...");
                this.bedrockServer.destroy();
            }
        }));

        Thread serverOutput = new Thread(() -> {
            try {
                String line;
                while((line = serverReader.readLine()) != null)
                    System.out.println("[Server]: " + line);
            }
            catch(Exception e) {
                System.out.println("[ServerManager]: Error serverReader");
            }
        });
        serverOutput.start();
    }

    public void stopServer() {
        if (this.bedrockServer == null || !this.bedrockServer.isAlive()) {
            System.out.println("[Server Manager]: No server instance is running!");
            return;
        }

        try {
            this.serverWriter.write("stop");
            this.serverWriter.newLine();
            this.serverWriter.flush();
        }
        catch (Exception e) {
            System.out.println("[ServerManager]: Error writing to server");
        }
    }
}

