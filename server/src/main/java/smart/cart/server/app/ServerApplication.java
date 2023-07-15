package smart.cart.server.app;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.InputStream;

@SpringBootApplication
public class ServerApplication {
    public static void main(String[] args) throws Exception {
        // Retrieve the Firebase admin credentials.
        InputStream inputStream = ServerApplication.class.getResourceAsStream("/serviceAccount.json");

        // Initialize the Firebase application.
        assert inputStream != null;
        GoogleCredentials googleCredentials = GoogleCredentials.fromStream(inputStream);
        FirebaseOptions options = FirebaseOptions.builder()
                                                 .setCredentials(googleCredentials)
                                                 .setDatabaseUrl("https://eur3.firebaseio.com")
                                                 .build();
        FirebaseApp.initializeApp(options);

        SpringApplication.run(ServerApplication.class, args);
    }
}
