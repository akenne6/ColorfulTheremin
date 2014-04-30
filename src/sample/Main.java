package sample;

import com.leapmotion.leap.Controller;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.stage.Stage;

import javax.sound.midi.Instrument;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Synthesizer;
import javafx.*;
import javafx.util.Duration;

import static java.lang.Math.random;

public class Main extends Application {
    public static Instrument[] instruments;

    private SimpleLeapListener listener = new SimpleLeapListener();
    private Controller leapController = new Controller();

    private AnchorPane root = new AnchorPane();
    private Circle circle = new Circle(50, Color.web("white", 0.35));
    public Instrument[] orchestra;
    public MidiChannel[] channels;

    @Override
    public void start(Stage primaryStage) {
        circle.setStrokeType(StrokeType.OUTSIDE);
        circle.setStroke(Color.web("white", 0.15));
        circle.setStrokeWidth(4);

        try {
            Synthesizer synthesizer = MidiSystem.getSynthesizer();
            synthesizer.open();

            orchestra = synthesizer.getAvailableInstruments();
            channels = synthesizer.getChannels();
            instruments = synthesizer.getDefaultSoundbank().getInstruments();
            channels[0].programChange(instruments[94].getPatch().getProgram());
        }
        catch (Exception e) {};


        leapController.addListener(listener);
        circle.setLayoutX(circle.getRadius());
        circle.setLayoutY(circle.getRadius());
        root.getChildren().add(circle);
        final Scene scene = new Scene(root, 1354, 762);
        /*
        //Greens
        Rectangle colors = new Rectangle(scene.getWidth(), scene.getHeight(),
                new LinearGradient(0f, 0f, 1f, 1f, true, CycleMethod.NO_CYCLE, new Stop[]{
                        new Stop(0, Color.web("#00E645")), // bright green, top left
                        new Stop(0.33, Color.web("#00A130")), // regular green
                        new Stop(0.66, Color.web("#006600")), // dark green
                        new Stop(1, Color.web("#003300")),})); // forest green, lower right
        */

        Rectangle colors = new Rectangle(scene.getWidth(), scene.getHeight(),
                new LinearGradient(0f, 1f, 1f, 0f, true, CycleMethod.NO_CYCLE, new Stop[]{
                        new Stop(0, Color.web("#f8bd55")),
                        new Stop(0.14, Color.web("#c0fe56")),
                        new Stop(0.28, Color.web("#5dfbc1")),
                        new Stop(0.43, Color.web("#64c2f8")),
                        new Stop(0.57, Color.web("#be4af7")),
                        new Stop(0.71, Color.web("#ed5fc2")),
                        new Stop(0.85, Color.web("#ef504c")),
                        new Stop(1, Color.web("#f2660f")),}));

        colors.widthProperty().bind(scene.widthProperty());
        colors.heightProperty().bind(scene.heightProperty());
        Group circles = new Group();
        for (int i = 0; i < 30; i++) {
            Circle circle = new Circle(150, Color.web("white", 0.05));
            circle.setStrokeType(StrokeType.OUTSIDE);
            circle.setStroke(Color.web("white", 0.16));
            circle.setStrokeWidth(4);
            circles.getChildren().add(circle);
        }

        Group blendModeGroup =
                new Group(new Group(new Rectangle(scene.getWidth(), scene.getHeight(),
                        Color.BLACK), circle, circles), colors);
        colors.setBlendMode(BlendMode.OVERLAY);
        root.getChildren().add(blendModeGroup);
        circle.setEffect(new BoxBlur(10, 10, 3));
        circles.setEffect(new BoxBlur(10, 10, 3));

        Timeline timeline = new Timeline();


        for (Node circle : circles.getChildren()) {
            timeline.getKeyFrames().addAll(
                    new KeyFrame(Duration.ZERO, // set start position at 0
                            new KeyValue(circle.translateXProperty(), random() * 1300),
                            new KeyValue(circle.translateYProperty(), random() * 762)),
                    new KeyFrame(new Duration(40000), // set end position at 40s
                            new KeyValue(circle.translateXProperty(), random() * 1300),
                            new KeyValue(circle.translateYProperty(), random() * 762)));
        }
        listener.pointProperty().addListener(new ChangeListener<Point2D>(){
            @Override
            public void changed(ObservableValue ov, Point2D t, final Point2D t1) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Point2D d = root.sceneToLocal(t1.getX() - scene.getX() - scene.getWindow().getX(),
                                t1.getY() - scene.getY() - scene.getWindow().getY());
                        double dx = d.getX(), dy = d.getY();
                        if (dx >= 0d && dx <= root.getWidth() - 2d * circle.getRadius() &&
                                dy >= 0d && dy <= root.getHeight() - 2d * circle.getRadius()) {
                            circle.setTranslateX(dx);
                            circle.setTranslateY(dy);
                        }
                        int x, y;
                        if (dx < 0)
                            x = 0;
                        else if (dx > 1277)
                            x = 80;
                        else
                        {
                            x = (int)dx/16;
                        }
                        if (dy < 0)
                            y = 127;
                        else if (dy > 720)
                            y = 0;
                        else
                        {
                            y = 127 - (int)dy/6;
                        }

                        channels[0].noteOn(y, x);
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println("X: " + x + " Y: " + y);

                    }
                });
            }
        });

        primaryStage.setScene(scene);
        timeline.play();
        primaryStage.show();
    }
    @Override
    public void stop(){
        leapController.removeListener(listener);

    }

    public static void main(String args[]){
        launch(args);
    }
}
