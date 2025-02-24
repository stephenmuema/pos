package gdrive;

import Controllers.UtilityClass;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.AbstractInputStreamContent;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import javafx.scene.control.Alert;
import securityandtime.AesCrypto;
import securityandtime.config;

import java.io.*;
import java.security.GeneralSecurityException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static securityandtime.config.*;

public class DriveSuperClass extends UtilityClass {
    Drive service;
    private NetHttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private Credential credential = null;

    public DriveSuperClass() throws IOException {
        super();
        // 2: Build a new authorized API client service.
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 3: Read client_secret.json file & create Credential object.
        try {
            credential = getCredentials(HTTP_TRANSPORT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 5: Create Google Drive Service.
        setService(new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential) //
                .setApplicationName(APPLICATION_NAME).build());
    }


    static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {

        java.io.File clientSecretFilePath = new java.io.File(config.CREDENTIALS_FOLDER, config.CLIENT_SECRET_FILE_NAME);

        if (!clientSecretFilePath.exists()) {
            throw new FileNotFoundException("Please copy " + config.CLIENT_SECRET_FILE_NAME //
                    + " to folder: " + config.CREDENTIALS_FOLDER.getAbsolutePath());
        }

        // Load client secrets.
        InputStream in = new FileInputStream(clientSecretFilePath);

        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(config.JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, config.JSON_FACTORY,
                clientSecrets, SCOPES).setDataStoreFactory(new FileDataStoreFactory(CREDENTIALS_FOLDER))
                .setAccessType("offline").build();

        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
    }

    public static final File createGoogleFolder(String folderIdParent, String folderName) throws IOException {

        File fileMetadata = new File();

        fileMetadata.setName(folderName);
        fileMetadata.setMimeType("application/vnd.google-apps.folder");

        if (folderIdParent != null) {
            List<String> parents = Arrays.asList(folderIdParent);

            fileMetadata.setParents(parents);
        }
        NetHttpTransport HTTP_TRANSPORT = new NetHttpTransport();
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }


        Credential credential = getCredentials(HTTP_TRANSPORT);

        // 5: Create Google Drive Service.
        Drive service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential) //
                .setApplicationName(APPLICATION_NAME).build();

        // Create a Folder.
        // Returns File object with id & name fields will be assigned values
        File file = service.files().create(fileMetadata).setFields("id, name").execute();

        return file;
    }


    public static File _createGoogleFile(String googleFolderIdParent, String contentType, //
                                         String customFileName, AbstractInputStreamContent uploadStreamContent) throws IOException, GeneralSecurityException {

        File fileMetadata = new File();
        fileMetadata.setName(customFileName);

        List<String> parents = Arrays.asList(googleFolderIdParent);
        fileMetadata.setParents(parents);
        //
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

        // 3: Read client_secret.json file & create Credential object.
        Credential credential = getCredentials(HTTP_TRANSPORT);

        // 5: Create Google Drive Service.
        Drive service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential) //
                .setApplicationName(APPLICATION_NAME).build();
        File file = service.files().create(fileMetadata, uploadStreamContent)
                .setFields("id, webContentLink, webViewLink, parents").execute();

        return file;
    }

    // Create Google File from byte[]
    public static File createGoogleFile(String googleFolderIdParent, String contentType, //
                                        String customFileName, byte[] uploadData) throws IOException, GeneralSecurityException {
        //
        AbstractInputStreamContent uploadStreamContent = new ByteArrayContent(contentType, uploadData);
        //
        return _createGoogleFile(googleFolderIdParent, contentType, customFileName, uploadStreamContent);
    }

    // Create Google File from java.io.File
    public static File createGoogleFile(String googleFolderIdParent, String contentType, //
                                        String customFileName, java.io.File uploadFile) throws IOException, GeneralSecurityException {

        //
        AbstractInputStreamContent uploadStreamContent = new FileContent(contentType, uploadFile);
        //
        return _createGoogleFile(googleFolderIdParent, contentType, customFileName, uploadStreamContent);
    }

    // Create Google File from InputStream
    public static File createGoogleFile(String googleFolderIdParent, String contentType, //
                                        String customFileName, InputStream inputStream) throws IOException, GeneralSecurityException {

        //
        AbstractInputStreamContent uploadStreamContent = new InputStreamContent(contentType, inputStream);
        //
        return _createGoogleFile(googleFolderIdParent, contentType, customFileName, uploadStreamContent);
    }

    // com.google.api.services.drive.model.File
    public static List<File> getGoogleSubFolders(String googleFolderIdParent) throws IOException {
        DriveSuperClass driveSuperClass = new DriveSuperClass();
        Drive driveService = driveSuperClass.getService();

        String pageToken = null;
        List<File> list = new ArrayList<File>();

        String query = null;
        if (googleFolderIdParent == null) {
            query = " mimeType = 'application/vnd.google-apps.folder' " //
                    + " and 'root' in parents";
        } else {
            query = " mimeType = 'application/vnd.google-apps.folder' " //
                    + " and '" + googleFolderIdParent + "' in parents";
        }

        do {
            FileList result = driveService.files().list().setQ(query).setSpaces("drive") //
                    // Fields will be assigned values: id, name, createdTime
                    .setFields("nextPageToken, files(id, name, createdTime)")//
                    .setPageToken(pageToken).execute();
            for (File file : result.getFiles()) {
                list.add(file);
            }
            pageToken = result.getNextPageToken();
        } while (pageToken != null);
        //
        return list;
    }

    // com.google.api.services.drive.model.File
    public static final List<File> getGoogleRootFolders() throws IOException {
        return getGoogleSubFolders(null);
    }

    public static boolean driveBackup(String driveFileName, String filePathName) throws IOException {
        boolean success = true;
        String company = "";

        String companyFolderId = "";//folder id for the company
        Connection connection = new UtilityClass().getConnection();
        try {
            PreparedStatement preparedStatement;
            preparedStatement = connection.prepareStatement("SELECT * FROM drivesettings ORDER BY id DESC LIMIT 1");

            ResultSet rs = preparedStatement.executeQuery();

            if (rs.isBeforeFirst()) {
                //admin has set company name
                while (rs.next()) {
                    if (rs.getString("companyname").length() > 0 && rs.getString("companyname") != null) {
                        company = AesCrypto.decrypt(encryptionkey, rs.getString("companyname"));


                    }
                    if (rs.getString("backupfolderid").length() > 0 && rs.getString("backupfolderid") != null) {
                        companyFolderId = AesCrypto.decrypt(encryptionkey, rs.getString("backupfolderid"));


                    }

                    if (!company.isEmpty() && companyFolderId.isEmpty()) {
                        //create drive folder for company
                        File f = createGoogleFolder(null, company);
                        preparedStatement = connection.prepareStatement("INSERT INTO drivesettings(driveBackupFolderId,backupFolderName)VALUES(?,?)");

                        preparedStatement.setString(1, AesCrypto.encrypt(encryptionkey, initVector, f.getId()));
                        preparedStatement.setString(2, AesCrypto.encrypt(encryptionkey, initVector, f.getName()));
                        preparedStatement.executeUpdate();
                        success = false;

                        System.out.println("backup to google drive failed because you dont have a company");

                    }

                    if (!companyFolderId.isEmpty() && !driveFileName.isEmpty() && !company.isEmpty()) {
                        List<File> googlefolders = getGoogleSubFolders(backUpFolderId);
                        for (File folder : googlefolders) {
                            System.out.println("Folder ID: " + folder.getId() + " --- Name: " + folder.getName());
                            if (folder.getId().equals(companyFolderId)/*id from db*/) {
                                //make backup in this folder
                                try {
                                    createGoogleFile(companyFolderId, "multipart/x-zip", driveFileName, new java.io.File(filePathName));
                                } catch (GeneralSecurityException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    } else {
                        System.out.println("backup to google drive failed because a parameter was empty");
                    }

                }
            } else {
                new UtilityClass().showAlert(Alert.AlertType.INFORMATION, panel.get("panel").getScene().getWindow(), "INFORMATION", "SET UP THE COMPANY NAME IN SETTINGS IN ORDER TO SEND BACKUP TO OUR DRIVE");


            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (!success) {
            driveBackup(driveFileName, filePathName);
        }
        return success;
    }

    public NetHttpTransport getHTTP_TRANSPORT() {
        return HTTP_TRANSPORT;
    }

    public DriveSuperClass setHTTP_TRANSPORT(NetHttpTransport HTTP_TRANSPORT) {
        this.HTTP_TRANSPORT = HTTP_TRANSPORT;
        return this;
    }

    public Credential getCredential() {
        return credential;
    }

    //subfolders

    public DriveSuperClass setCredential(Credential credential) {
        this.credential = credential;
        return this;
    }

    public Drive getService() {
        return service;
    }

    public DriveSuperClass setService(Drive service) {
        this.service = service;
        return this;
    }

}
