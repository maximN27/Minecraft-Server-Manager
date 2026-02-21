package org.ServerManager;

import javax.swing.*;
import java.awt.*;

public class ServerManagerGUI {
    ServerManagerGUI() {
        ServerManager sm = new ServerManager();
        PlayitManager pm = new PlayitManager();

        JFrame mainWindow = new JFrame("Minecraft Server Manager");
        mainWindow.setSize(1000, 800);
        mainWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JLabel mainText = new JLabel("Minecraft Server Manager", JLabel.CENTER);

        JButton startServerButton = new JButton("Start Server");
        startServerButton.setSize(new Dimension(50, 50));
        JButton stopServerButton = new JButton("Stop Server");
        stopServerButton.setSize(new Dimension(50, 50));

        JButton startPlayitButton = new JButton("Start Playit");
        startPlayitButton.setSize(new Dimension(50, 50));
        JButton stopPlayitButton = new JButton("Stop Playit");
        stopPlayitButton.setSize(new Dimension(50, 50));

        JPanel serverbuttonPanel = new JPanel();
        serverbuttonPanel.add(startServerButton);
        serverbuttonPanel.add(stopServerButton);

        JPanel playitButtonPanel = new JPanel();
        playitButtonPanel.add(startPlayitButton);
        playitButtonPanel.add(stopPlayitButton);

        JPanel outputPanel = new JPanel();
        JTextArea outputText = new JTextArea(800, 400);
        JScrollPane outputScroll = new JScrollPane(outputText);
        outputPanel.add(outputText);


        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(0, 1, 5, 5));
        mainPanel.add(mainText);
        mainPanel.add(serverbuttonPanel);
        mainPanel.add(playitButtonPanel);
        mainPanel.add(outputPanel);

        startServerButton.addActionListener(e -> {
            try {
                sm.startServer();
            }
            catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Server Startup Failed!");
            }
        });

        stopServerButton.addActionListener(e -> {
            try {
                sm.stopServer();
            }
            catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Cannot Stop the Server!");
            }
        });

        startPlayitButton.addActionListener(e -> {
            try {
                pm.startPlayit();
            }
            catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Playit Startup Failed!");
            }
        });

        stopPlayitButton.addActionListener(e -> {
            try {
                pm.playitQuitter();
            }
            catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Cannot Stop the Playit!");
            }
        });

        mainWindow.setContentPane(mainPanel);
        mainWindow.setVisible(true);
    }
    static void main() {
        new ServerManagerGUI();
    }
}
