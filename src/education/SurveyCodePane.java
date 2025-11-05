package education;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.json.JSONObject;

/**
 * Generates the survey code as: last N hex chars of SHA-256(username + surveySuffix), uppercase.
 * Username is read from the provided settings JSON.
 */
public class SurveyCodePane extends ScrollPane {

    private final VBox root = new VBox(16);
    private final String surveySuffix;
    private final int displayLength;
    private final JSONObject settings;
    private EducationSubScene scene;

    public SurveyCodePane(double width, double height, EducationSubScene scene, String surveySuffix, String username, int displayLength, JSONObject settings) {
        this.surveySuffix = surveySuffix == null ? "" : surveySuffix;
        this.displayLength = Math.max(1, displayLength <= 0 ? 8 : displayLength);
        this.settings = settings;
        this.scene = scene;

        setFitToWidth(true);
        setHbarPolicy(ScrollBarPolicy.NEVER);
        setVbarPolicy(ScrollBarPolicy.AS_NEEDED);

        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(32));
        root.setFillWidth(true);
        root.setMaxWidth(width * 0.8);

        Text headline = new Text("Your survey code");
        headline.setFont(new Font(36));

        Label info = new Label(
            "Username: " + (username.isEmpty() ? "(not set)" : username) +
            "\nGive the code below to the online survey to unlock it."
        );
        info.setWrapText(true);
        info.setMaxWidth(width * 0.8);

        TextArea codeArea = new TextArea();
        codeArea.setEditable(false);
        codeArea.setWrapText(true);
        codeArea.setPrefRowCount(2);
        codeArea.setMaxWidth(width * 0.6);

        String display = "(username missing)";
        if (!username.isEmpty()) {
            String fullHash = sha256Hex(username + surveySuffix);
            display = fullHash.length() <= displayLength
                    ? fullHash
                    : fullHash.substring(fullHash.length() - displayLength);
            display = display.toUpperCase();
        }
        codeArea.setText(display);

        Button copyBtn = new Button("Copy");
        copyBtn.setOnAction(e -> {
            String txt = codeArea.getText();
            if (txt != null && !txt.isBlank()) {
                ClipboardContent content = new ClipboardContent();
                content.putString(txt);
                Clipboard.getSystemClipboard().setContent(content);
            }
        });

        HBox actions = new HBox(8, copyBtn);
        actions.setAlignment(Pos.CENTER);

		HBox button_box = new HBox();
		Button button_back = new Button("Back");
		button_back.setFont(new Font(25));
		button_back.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				scene.setPrev();
			}
		});		
		Button button_next = new Button("Next");
		button_next.setFont(new Font(25));
		button_next.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				scene.setNext();
			}
		});
		button_box.getChildren().addAll(button_back, button_next);
        
        root.getChildren().addAll(headline, info, actions, codeArea, button_box);

        BorderPane bp = new BorderPane();
        bp.setCenter(root);
        setContent(bp);
    }

    public static String sha256Hex(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] out = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder(out.length * 2);
            for (byte b : out) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 not available", e);
        }
    }
}
