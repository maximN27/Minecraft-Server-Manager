package org.ServerManager;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import org.apache.http.protocol.HTTP;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

public class DriveManager {
    private static final String APPLICATION_NAME = "ServerManager";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String TOKEN_DIRECTORY_PATH = "tokens";
    private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE);
    private static final String CREDENTIALS_FILE_PATH = "/client_secret_603343915038-4pqsopi4onema0b5q4gcuu2ebj7j5ovj.apps.googleusercontent.com.json";

    /**
     * Creates an authorized Credential object.
     *
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private static Credential getCredential(HttpTransport HTTP_TRANSPORT) throws IOException {
        InputStream in = DriveManager.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build Flow for authorization
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKEN_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();

        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");

        return credential;
    }

    private static Drive getDriveService() throws GeneralSecurityException, IOException {
        // Set NetHttpTransport
        NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

        // Build Drive Service
        Drive service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredential(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();

        return service;
    }

    /**
     * Uploads a file to already created folder in drive
     *
     * @param FILENAME Name of the file that is uploaded
     * @param FILE_PATH Path of the file that is uploaded
     * @param mimeType MIME Type of the file that is uploaded
     * @throws IOException if file is not found in FILE_PATH
     * @return File ID of the uploaded file
     */
    public String uploadFile(String FILENAME, String FILE_PATH, String mimeType) throws IOException {
        Drive service;
        try {
            service = getDriveService();
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }

        //Creating File object to upload
        File fileMetaData = new File();
        fileMetaData.setName(FILENAME);
        fileMetaData.setMimeType(mimeType);
        fileMetaData.setParents(Collections.singletonList("1p7in2Ja4kNTzNGgN0npauB-3XaMFxekG"));

        java.io.File filePath = new java.io.File(FILE_PATH);
        FileContent mediaContent = new FileContent(mimeType, filePath);

        File file = service.files().create(fileMetaData, mediaContent)
                .setFields("id")
                .execute();

        return file.getId();
    }

}
