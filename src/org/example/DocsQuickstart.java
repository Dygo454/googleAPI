package org.example;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.docs.v1.Docs;
import com.google.api.services.docs.v1.DocsScopes;
import com.google.api.services.docs.v1.model.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.*;
import java.util.List;

/* class to demonstarte use of Docs get documents API */
public class DocsQuickstart {
    /** Application name. */
    private static final String APPLICATION_NAME = "Google Docs API Java Quickstart";
    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    /** Directory to store authorization tokens for this application. */
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final String DOCUMENT_ID = "195j9eDD3ccgjQRttHhJPymLJUCOUjs-jmwTrekvdjFE";

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES = Collections.singletonList(DocsScopes.DOCUMENTS);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    /**
     * Creates an authorized Credential object.
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        InputStream in = DocsQuickstart.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
        //returns an authorized Credential object.
        return credential;
    }

    public static void main(String... args) throws IOException, GeneralSecurityException {
        // Build a new authorized API client service.
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Docs service = new Docs.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
        Document doc = new Document()
                .setTitle("secret-hackermans-file");
        doc = service.documents().create(doc)
                .execute();
        String header = "Lorem Ipsum\n\n";
        String text1 = "\tLorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Nunc aliquet bibendum enim facilisis gravida neque convallis a. Scelerisque purus semper eget duis at tellus. Eu mi bibendum neque egestas congue quisque egestas. Quam pellentesque nec nam aliquam sem et tortor consequat. Aliquam eleifend mi in nulla posuere. Amet mauris commodo quis imperdiet massa tincidunt nunc. Sit amet dictum sit amet justo donec. Sit amet porttitor eget dolor morbi. Pellentesque elit eget gravida cum sociis. Velit egestas dui id ornare. Praesent tristique magna sit amet purus. Augue lacus viverra vitae congue eu consequat ac felis. Sit amet volutpat consequat mauris nunc congue. Eget sit amet tellus cras adipiscing enim eu. Mi quis hendrerit dolor magna.\n\n";
        String text2 = "\tOrnare arcu odio ut sem. Pretium lectus quam id leo in. Non consectetur a erat nam. Sit amet commodo nulla facilisi. Urna nunc id cursus metus aliquam. Venenatis urna cursus eget nunc scelerisque. Scelerisque in dictum non consectetur. Penatibus et magnis dis parturient montes nascetur ridiculus mus. Non blandit massa enim nec dui nunc. Nascetur ridiculus mus mauris vitae ultricies leo integer malesuada. Enim nunc faucibus a pellentesque sit amet porttitor eget dolor. Accumsan tortor posuere ac ut consequat semper. At erat pellentesque adipiscing commodo elit at imperdiet. A diam maecenas sed enim ut sem viverra aliquet eget. Amet justo donec enim diam vulputate. Dignissim suspendisse in est ante. Tellus mauris a diam maecenas sed.\n\n";
        String text3 = "\tViverra nibh cras pulvinar mattis nunc sed blandit libero volutpat. A iaculis at erat pellentesque adipiscing. Scelerisque in dictum non consectetur a. Parturient montes nascetur ridiculus mus. Sit amet nisl purus in mollis nunc sed id semper. Semper auctor neque vitae tempus quam pellentesque nec. Adipiscing elit pellentesque habitant morbi tristique senectus et netus et. Ultricies mi eget mauris pharetra et ultrices neque ornare. Purus sit amet luctus venenatis lectus magna fringilla urna porttitor. Nulla pellentesque dignissim enim sit amet venenatis urna cursus. Mauris a diam maecenas sed enim ut sem viverra. Ullamcorper a lacus vestibulum sed arcu. Vestibulum morbi blandit cursus risus at ultrices.";
        List<Request> requests = new ArrayList<>();
        requests.add(new Request().setInsertText(new InsertTextRequest()
                .setText(text1)
                .setLocation(new Location().setIndex(1))));

        requests.add(new Request().setInsertText(new InsertTextRequest()
                .setText(text2)
                .setLocation(new Location().setIndex(text1.length()+1))));

        requests.add(new Request().setInsertText(new InsertTextRequest()
                .setText(text3)
                .setLocation(new Location().setIndex(text2.length()+text1.length()+1))));

        requests.add(new Request().setInsertText(new InsertTextRequest()
                .setText(header)
                .setLocation(new Location().setIndex(1))));

        requests.add(new Request().setUpdateParagraphStyle(new UpdateParagraphStyleRequest()
                .setRange(new Range()
                        .setStartIndex(1)
                        .setEndIndex(header.length()+1))
                .setParagraphStyle(new ParagraphStyle()
                        .setNamedStyleType("HEADING_1")
                        .setSpaceAbove(new Dimension()
                                .setMagnitude(10.0)
                                .setUnit("PT"))
                        .setSpaceBelow(new Dimension()
                                .setMagnitude(10.0)
                                .setUnit("PT"))
                        .setAlignment("CENTER"))
                .setFields("namedStyleType,spaceAbove,spaceBelow,alignment")));

        BatchUpdateDocumentRequest body = new BatchUpdateDocumentRequest().setRequests(requests);
        BatchUpdateDocumentResponse response = service.documents()
                .batchUpdate(doc.getDocumentId(), body).execute();
        System.out.println("Created document with title: " + doc.getTitle());
    }
}