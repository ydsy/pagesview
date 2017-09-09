package felix.pagesview;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Created by Z< on 09/09/2017.
 */
public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Group group = new Group();
        PagesView pagesView = new PagesView(group, 100, new GetPageImpl() {
            @Override
            public void page(int before, int after) {
                System.out.println("before = " + before + ", after = " + after);
            }
        });
        pagesView.initView();

        StackPane root = new StackPane();
        root.getChildren().add(group);

        primaryStage.setScene(new Scene(root, 800, 100, Color.BLACK));
        primaryStage.show();
    }
}