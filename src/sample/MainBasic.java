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

import javax.sound.midi.*;

import javafx.*;
import javafx.util.Duration;

import static java.lang.Math.random;

/* Created by Alex, Modified by Sarah */

public class MainBasic extends Application {
    public static Instrument[] instruments;

    private SimpleLeapListener listener = new SimpleLeapListener();
    private Controller leapController = new Controller();

    private AnchorPane root = new AnchorPane();
    private Circle circle = new Circle(50, Color.web("white", 0.35));
    public Instrument[] orchestra;
    public MidiChannel[] channels;



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
            channels[0].programChange(instruments[64].getPatch().getProgram());
            channels[1].programChange(instruments[70].getPatch().getProgram());


        }
        catch (Exception e) {};



        leapController.addListener(listener);
        circle.setLayoutX(circle.getRadius());
        circle.setLayoutY(circle.getRadius());
        final Scene scene = new Scene(root, 1277, 720);

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

        Group lines = new Group();
        for (int i = 660; i > 0; i-=180) {
            Line line = new Line(0, i, scene.getWidth(), i);
            line.setStrokeWidth(3);
            line.setStroke(Color.WHITE);
            lines.getChildren().add(line);
        }
        lines.setEffect(new BoxBlur(10,10,3));

        Group blendModeGroup =
                new Group(new Group(new Rectangle(scene.getWidth(), scene.getHeight(),
                        Color.BLACK), lines, circle), colors);
        colors.setBlendMode(BlendMode.OVERLAY);
        root.getChildren().add(blendModeGroup);
        circle.setEffect(new BoxBlur(10, 10, 3));
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
                            y = 91;
                        else if (dy > 720)
                            y = 43;
                        else
                        {
                            y = 91 - (int)dy/15;
                        }
                        //Mono creates a more "Theremin" like sound
                        channels[1].setMono(true);
                        channels[0].setMono(true);

                        //Y is 720 px high (15 * 48 --> 48 notes between 55 and 103 Midi notes), so every 15 px is 1 midi note
                        //Plays two notes on top of one another
                        channels[1].noteOn(y, x);
                        channels[0].noteOn(y, x);

                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println("x: " + x + " y: " + y);


                    }
                });
            }
        });

        primaryStage.setScene(scene);
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
