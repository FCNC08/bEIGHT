package education;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.json.JSONObject;

/**
 * Shown before modules if the unit is locked (has unlockEnding and not yet unlocked).
 * Uses the username from settings; only asks for the website code.
 * On success: marks settings.unlocked = true and jumps into modules.
 */
public class UnlockPane extends ScrollPane {

    private final VBox root = new VBox(16);
    private final String expectedEnding;
    private final EducationSubScene subscene;
    private final JSONObject settings;
    private final String username;

    public UnlockPane(double width, double height, String expectedEnding, JSONObject settings, String username, EducationSubScene subscene) {
        this.expectedEnding = expectedEnding == null ? "" : expectedEnding.trim();
        this.settings = settings;
        this.subscene = subscene;
        this.username = username;

        setFitToWidth(true);
        setHbarPolicy(ScrollBarPolicy.NEVER);
        setVbarPolicy(ScrollBarPolicy.AS_NEEDED);

        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(32));
        root.setFillWidth(true);
        root.setMaxWidth(width * 0.8);

        Text headline = new Text("Unlock this section");
        headline.setFont(new Font(36));

        Label info = new Label(
            "Username: " + (username.isEmpty() ? "(not set)" : username) +
            "\nPaste the code you received after completing the online survey."
        );
        info.setWrapText(true);
        info.setMaxWidth(width * 0.8);

        TextField codeField = new TextField();
        codeField.setPromptText("Paste website code here");
        codeField.setMaxWidth(width * 0.5);

        Label status = new Label();
        status.setWrapText(true);
        status.setMaxWidth(width * 0.8);

        Button unlockBtn = new Button("Unlock");
        unlockBtn.setFont(new Font(20));
        unlockBtn.setOnAction(e -> {
            String code = codeField.getText() == null ? "" : codeField.getText().trim();
            if (code.isEmpty()) {
                status.setText("Please paste the website code.");
                return;
            }
            
            // expected = last N chars of SHA-256(username + unlockEnding), uppercase
            String full = SurveyCodePane.sha256Hex(username + this.expectedEnding);
            String expected = full.length() <= 8 ? full : full.substring(full.length() - 8);
            expected = expected.toUpperCase();

            if (expected.equalsIgnoreCase(code)) {
                status.setText("✅ Unlocked!");
                // persist unlock state and the code we saw (optional but useful for audit)
                settings.put("unlocked", true);
                settings.put("unlockCodeUsed", code);
                // update icon right away (remove lock)
                subscene.refreshIconDecorations();
                // go to modules
                subscene.startModules();
            } else {
                status.setText("❌ Code does not match. Double-check the code from the website.");
            }
        });

        root.getChildren().addAll(headline, info, codeField, unlockBtn, status);

        BorderPane bp = new BorderPane();
        bp.setCenter(root);
        setContent(bp);
    }
}
