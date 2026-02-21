package org.ServerManager;

import java.io.*;
import java.util.concurrent.TimeUnit;

public class PlayitManager {
    private final String playitDirectory = "serverbin\\playit\\";
    private final String playitExecutablePath = "serverbin\\playit\\playit-windows-x86_64-signed.exe";

    private Process playit;
    private BufferedReader playitReader;
    private BufferedWriter playitWriter;

    public void startPlayit() throws IOException {
        if (this.playit != null && this.playit.isAlive()) {
            System.out.println("[Playit Manager] An instance is already running!");
            return;
        }

        ProcessBuilder pb = new ProcessBuilder(playitExecutablePath, "-s");
        pb.directory(new File(playitDirectory));
        pb.redirectErrorStream(true);
        this.playit = pb.start();

        // Channel the playit output to command line
        this.playitReader = new BufferedReader(new InputStreamReader(playit.getInputStream()));
        this.playitWriter = new BufferedWriter(new OutputStreamWriter(playit.getOutputStream()));

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (this.playit != null && this.playit.isAlive()) {
                System.out.println("Java is shutting down. Killing Minecraft Server...");
                this.playit.destroy();
            }
        }));

        Thread playitReader = new Thread(() -> {
            try {
                String line;
                while ((line = this.playitReader.readLine()) != null) {
                    System.out.println("[Playit Manager] " + line);
                }
            }
            catch (Exception e) {
                System.out.println("[Playit Manager] Error Playit Reader");
            }
        });
        playitReader.start();
    }

    public void playitQuitter() {
        if (this.playit == null || !this.playit.isAlive()) {
            System.out.println("[Playit Manager] No instance is currently running!");
            return;
        }

        try {
            this.playit.destroy();
            this.playit.waitFor(2, TimeUnit.SECONDS);
            if (this.playit != null && this.playit.isAlive())
                this.playit.destroyForcibly();
            this.playit = null;
            System.out.println("[Playit Manager] Closed Playit Application");
        }
        catch (Exception e) {
            System.out.println("[Playit Manager] Error writing Playit Writer");
        }
    }
}

