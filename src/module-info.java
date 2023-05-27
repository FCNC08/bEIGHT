module EightBitTest {
	requires javafx.controls;
	requires javafx.graphics;
	requires java.desktop;
	requires javafx.swing;
	requires pdfbox.app;
	
	opens application to javafx.graphics, javafx.fxml;
}
