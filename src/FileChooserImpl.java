import java.io.File;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public final class FileChooserImpl extends Application {
  private Stage stage;
  private Pane root;
  private Clipboard clipboard = Clipboard.getSystemClipboard();
  private Parser parser;

  @Override
  public void start(final Stage stage) {
    stage.setTitle("MIDI Parser");

    final FileChooser fileChooser = new FileChooser();
    fileChooser.getExtensionFilters().add(new ExtensionFilter("midi files", "*.mid", "*.midi"));

    final Button openButton = new Button("Open a File");
    openButton.setOnAction(
            new EventHandler<ActionEvent>() {
              @Override
              public void handle(final ActionEvent e) {
                File file = fileChooser.showOpenDialog(stage);
                if (file != null) {
                  try {
                    openFile(file);
                  } catch (Exception e1) {
                    e1.printStackTrace();
                  }
                }
              }
            });

    Pane p = new VBox(openButton);
    p.setPadding(new Insets(5, 5, 5, 5));

    final Pane rootGroup = new VBox();
    rootGroup.getChildren().addAll(p);
    rootGroup.setPadding(new Insets(12, 12, 12, 12));

    this.root = rootGroup;
    this.stage = stage;

    stage.setScene(new Scene(rootGroup));
    stage.show();
  }

  public static void main(String[] args) {
    Application.launch(args);
  }

  private void openFile(File file) throws Exception {
    this.parser = new Parser(file);

    VBox v = new VBox();
    Button b = new Button("Copy to clipboard");
    Pane p = new VBox(b);
    p.setPadding(new Insets(5, 5, 5, 5));

    TextArea t = new TextArea();
    t.setText(this.parser.toString());
    t.setEditable(false);

    ScrollPane sp = new ScrollPane();
    sp.setContent(t);

    HBox h = new HBox();

    

    b.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(final ActionEvent e) {
        ClipboardContent c = new ClipboardContent();
        c.putString(t.getText());
        clipboard.setContent(c);
      }
    });

    v.setPadding(new Insets(12, 12, 12, 12));
    v.getChildren().addAll(this.root.getChildren());
    v.getChildren().addAll(p, t);
    v.setAlignment(Pos.CENTER);

    this.stage.setScene(new Scene(v));
    this.stage.show();
  }
}