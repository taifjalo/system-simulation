package controller.animation;

import javafx.animation.*;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages all animation timelines and visual effects for the workflow simulation.
 * <p>
 * Separates animation logic from controller for better maintainability.
 */
public class AnimationManager {
    /** The pane where animations are rendered. */
    private final Pane animationPane;
    /** List of all animation timelines. */
    private final List<Timeline> allTimelines;
    /** List of all rotation transitions. */
    private final List<RotateTransition> allRotations;
    // Color constants
    private static final Color GREEN_MAIN = Color.web("#4f7d47");
    private static final Color GREEN_FILL = Color.web("#3f6b3f");
    private static final Color GREEN_BRIGHT = Color.web("#28a745");
    private static final Color YELLOW_MAIN = Color.web("#ffc927");
    private static final Color YELLOW_DARK = Color.web("#ffbe0d");
    private static final Color RED_MAIN = Color.web("#e10000");
    private static final Color RED_DARK = Color.web("#da0000");
    private static final Color GOLD = Color.web("#ffd700");

    /**
     * Constructs an AnimationManager for the given animation pane.
     * @param animationPane the pane to render animations on
     */
    public AnimationManager(Pane animationPane) {
        this.animationPane = animationPane;
        this.allTimelines = new ArrayList<>();
        this.allRotations = new ArrayList<>();
    }

    /**
     * Initialize all animations for the workflow simulation.
     */
    public void setupAllAnimations() {
        clearAnimations();
        // Workflow station animations
        createStationAnimation(572, 450, GREEN_BRIGHT, GREEN_MAIN);
        createStationAnimation(735, 448, RED_MAIN, RED_DARK);
        // Flow animations
        createGreenLineFlowAnimations();
        createRedLineFlowAnimations();
        createGoldPathFlowAnimations();
        // Legacy loaders
        createBoxLoader(53, 12);
        createPulseLoader(180, 52);
    }

    /**
     * Clear all existing animations from the pane and reset state.
     */
    public void clearAnimations() {
        stopAll();
        animationPane.getChildren().clear();
        allTimelines.clear();
        allRotations.clear();
    }

    /**
     * Play all animations.
     */
    public void playAll() {
        allTimelines.forEach(Timeline::play);
        allRotations.forEach(RotateTransition::play);
    }

    /**
     * Pause all animations.
     */
    public void pauseAll() {
        allTimelines.forEach(Timeline::pause);
        allRotations.forEach(RotateTransition::pause);
    }

    /**
     * Stop all animations.
     */
    public void stopAll() {
        allTimelines.forEach(Timeline::stop);
        allRotations.forEach(RotateTransition::stop);
    }

    /**
     * Adjust speed of all animations.
     * @param speed the speed multiplier
     */
    public void setSpeed(double speed) {
        allTimelines.forEach(timeline -> timeline.setRate(speed));
        allRotations.forEach(rotation -> rotation.setRate(speed));
    }

    /**
     * Creates pulsing ring animation around workflow stations.
     * @param x x-coordinate of the station
     * @param y y-coordinate of the station
     * @param mainColor main color for the ring
     * @param accentColor accent color for particles
     */
    private void createStationAnimation(double x, double y, Color mainColor, Color accentColor) {
        final double baseRadius = 36.0;
        Circle outerRing = new Circle(baseRadius + 8);
        outerRing.setFill(Color.TRANSPARENT);
        outerRing.setStroke(mainColor);
        outerRing.setStrokeWidth(2);
        outerRing.setLayoutX(x);
        outerRing.setLayoutY(y);
        outerRing.setOpacity(0);
        outerRing.setEffect(createSoftGlow(mainColor, 15));
        Circle particle1 = new Circle(3, mainColor);
        Circle particle2 = new Circle(2.5, accentColor);
        Circle particle3 = new Circle(2, mainColor);
        particle1.setLayoutX(x);
        particle1.setLayoutY(y);
        particle2.setLayoutX(x);
        particle2.setLayoutY(y);
        particle3.setLayoutX(x);
        particle3.setLayoutY(y);
        particle1.setEffect(createSoftGlow(mainColor, 8));
        particle2.setEffect(createSoftGlow(accentColor, 6));
        particle3.setEffect(createSoftGlow(mainColor, 5));
        particle1.setOpacity(0.9);
        particle2.setOpacity(0.85);
        particle3.setOpacity(0.8);
        animationPane.getChildren().addAll(outerRing, particle1, particle2, particle3);
        Timeline pulseTimeline = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(outerRing.scaleXProperty(), 0.9, Interpolator.EASE_BOTH),
                        new KeyValue(outerRing.scaleYProperty(), 0.9, Interpolator.EASE_BOTH),
                        new KeyValue(outerRing.opacityProperty(), 0, Interpolator.EASE_BOTH),
                        new KeyValue(outerRing.strokeWidthProperty(), 3, Interpolator.EASE_BOTH)
                ),
                new KeyFrame(Duration.seconds(1.5),
                        new KeyValue(outerRing.scaleXProperty(), 1.3, Interpolator.EASE_BOTH),
                        new KeyValue(outerRing.scaleYProperty(), 1.3, Interpolator.EASE_BOTH),
                        new KeyValue(outerRing.opacityProperty(), 0.6, Interpolator.EASE_BOTH),
                        new KeyValue(outerRing.strokeWidthProperty(), 1, Interpolator.EASE_BOTH)
                ),
                new KeyFrame(Duration.seconds(3.0),
                        new KeyValue(outerRing.scaleXProperty(), 1.5, Interpolator.EASE_BOTH),
                        new KeyValue(outerRing.scaleYProperty(), 1.5, Interpolator.EASE_BOTH),
                        new KeyValue(outerRing.opacityProperty(), 0, Interpolator.EASE_BOTH)
                )
        );
        pulseTimeline.setCycleCount(Timeline.INDEFINITE);
        allTimelines.add(pulseTimeline);
        Timeline orbit1 = createCircularMotionTimeline(particle1, baseRadius - 5, 3.0, 0);
        orbit1.setCycleCount(Timeline.INDEFINITE);
        allTimelines.add(orbit1);
        Timeline orbit2 = createCircularMotionTimeline(particle2, baseRadius - 5, 3.5, Math.PI * 2 / 3);
        orbit2.setCycleCount(Timeline.INDEFINITE);
        allTimelines.add(orbit2);
        Timeline orbit3 = createCircularMotionTimeline(particle3, baseRadius - 5, 4.0, Math.PI * 4 / 3);
        orbit3.setCycleCount(Timeline.INDEFINITE);
        allTimelines.add(orbit3);
    }

    /**
     * Box loader with smooth circular motion.
     * @param x x-coordinate
     * @param y y-coordinate
     */
    private void createBoxLoader(double x, double y) {
        Circle containerCircle = new Circle(40);
        containerCircle.setFill(Color.TRANSPARENT);
        containerCircle.setStroke(Color.rgb(255, 255, 255, 0.3));
        containerCircle.setStrokeWidth(1);
        containerCircle.setLayoutX(x + 40);
        containerCircle.setLayoutY(y + 40);
        Circle dot1 = new Circle(5, YELLOW_DARK);
        Circle dot2 = new Circle(5, YELLOW_DARK);
        dot1.setLayoutX(x + 40);
        dot1.setLayoutY(y + 40);
        dot2.setLayoutX(x + 40);
        dot2.setLayoutY(y + 40);
        dot1.setEffect(createSoftGlow(GREEN_MAIN, 10));
        dot2.setEffect(createSoftGlow(GREEN_MAIN, 10));
        animationPane.getChildren().addAll(containerCircle, dot1, dot2);
        Timeline loaderBoxAnimation = createCircularMotionTimeline(dot1, 40, 2.0, 0);
        loaderBoxAnimation.setCycleCount(Timeline.INDEFINITE);
        allTimelines.add(loaderBoxAnimation);
        Timeline dot2Timeline = createCircularMotionTimeline(dot2, 40, 2.0, Math.PI);
        dot2Timeline.setCycleCount(Timeline.INDEFINITE);
        allTimelines.add(dot2Timeline);
    }

    /**
     * Pulse loader animation.
     * @param x x-coordinate
     * @param y y-coordinate
     */
    private void createPulseLoader(double x, double y) {
        Circle pulse1 = new Circle(30, Color.rgb(79, 125, 71, 0.5));
        Circle pulse2 = new Circle(30, Color.rgb(255, 201, 39, 0.5));
        pulse1.setLayoutX(x);
        pulse1.setLayoutY(y);
        pulse2.setLayoutX(x);
        pulse2.setLayoutY(y);
        animationPane.getChildren().addAll(pulse1, pulse2);
        Timeline loaderPulseAnimation = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(pulse1.scaleXProperty(), 0.3, Interpolator.EASE_BOTH),
                        new KeyValue(pulse1.scaleYProperty(), 0.3, Interpolator.EASE_BOTH),
                        new KeyValue(pulse1.opacityProperty(), 0, Interpolator.EASE_BOTH)
                ),
                new KeyFrame(Duration.seconds(0.66),
                        new KeyValue(pulse1.scaleXProperty(), 1.2, Interpolator.EASE_BOTH),
                        new KeyValue(pulse1.scaleYProperty(), 1.2, Interpolator.EASE_BOTH),
                        new KeyValue(pulse1.opacityProperty(), 0.8, Interpolator.EASE_BOTH)
                ),
                new KeyFrame(Duration.seconds(1.0),
                        new KeyValue(pulse1.scaleXProperty(), 1.8, Interpolator.EASE_BOTH),
                        new KeyValue(pulse1.scaleYProperty(), 1.8, Interpolator.EASE_BOTH),
                        new KeyValue(pulse1.opacityProperty(), 0, Interpolator.EASE_BOTH)
                )
        );
        loaderPulseAnimation.setCycleCount(Timeline.INDEFINITE);
        allTimelines.add(loaderPulseAnimation);
        Timeline pulse2Timeline = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(pulse2.scaleXProperty(), 0.3, Interpolator.EASE_BOTH),
                        new KeyValue(pulse2.scaleYProperty(), 0.3, Interpolator.EASE_BOTH),
                        new KeyValue(pulse2.opacityProperty(), 0, Interpolator.EASE_BOTH)
                ),
                new KeyFrame(Duration.seconds(0.66),
                        new KeyValue(pulse2.scaleXProperty(), 1.2, Interpolator.EASE_BOTH),
                        new KeyValue(pulse2.scaleYProperty(), 1.2, Interpolator.EASE_BOTH),
                        new KeyValue(pulse2.opacityProperty(), 0.8, Interpolator.EASE_BOTH)
                ),
                new KeyFrame(Duration.seconds(1.0),
                        new KeyValue(pulse2.scaleXProperty(), 1.8, Interpolator.EASE_BOTH),
                        new KeyValue(pulse2.scaleYProperty(), 1.8, Interpolator.EASE_BOTH),
                        new KeyValue(pulse2.opacityProperty(), 0, Interpolator.EASE_BOTH)
                )
        );
        pulse2Timeline.setDelay(Duration.seconds(0.5));
        pulse2Timeline.setCycleCount(Timeline.INDEFINITE);
        allTimelines.add(pulse2Timeline);
    }

    /**
     * Creates flowing particles along green connection lines.
     */
    private void createGreenLineFlowAnimations() {
        createLineFlowParticles(175, 303, 271, 303, GREEN_MAIN, 0.3);
        createLineFlowParticles(350, 305, 448, 305, GREEN_MAIN, 0.6);
        createLineFlowParticles(523, 305, 622, 305, GREEN_MAIN, 0.9);
    }

    /**
     * Creates flowing particles along red connection lines.
     */
    private void createRedLineFlowAnimations() {
        createLineFlowParticles(100, 304, 60, 304, RED_MAIN, 0);
        createLineFlowParticles(58, 300, 58, 190, RED_MAIN, 0);
        createLineFlowParticles(60, 190, 100, 190, RED_MAIN, 0);
        createLineFlowParticles(662, 267, 662, 200, RED_MAIN, 0.1);
        createLineFlowParticles(662, 187, 220, 187, RED_MAIN, 0.2);
        createLineFlowParticles(215, 185, 215, 275, RED_MAIN, 0.4);
        createLineFlowParticles(470, 270, 470, 220, RED_MAIN, 0);
        createLineFlowParticles(470, 217, 160, 220, RED_MAIN, 0.4);
        createLineFlowParticles(470, 340, 470, 377, RED_MAIN, 0);
        createLineFlowParticles(470, 380, 235, 383, RED_MAIN, 0);
        createLineFlowParticles(393, 377, 393, 335, RED_MAIN, 0);
        createLineFlowParticles(233, 377, 233, 335, RED_MAIN, 0);
        createLineFlowParticles(650, 337, 560, 477, GREEN_MAIN, 0.2);
        createLineFlowParticles(495, 337, 570, 477, GREEN_MAIN, 0.2);
        createLineFlowParticles(660, 337, 750, 477, RED_MAIN, 0.2);
    }

    /**
     * Creates flowing particles along gold curved paths.
     */
    private void createGoldPathFlowAnimations() {
        createArcFlowParticles(96, 85, 140, 120, 138, 265, GOLD, 0);
        createArcFlowParticles(182, 85, 140, 120, 138, 265, GOLD, 0.5);
    }

    /**
     * Creates flowing particles along a straight line.
     * @param startX start x
     * @param startY start y
     * @param endX end x
     * @param endY end y
     * @param color color of the particles
     * @param delay animation delay
     */
    private void createLineFlowParticles(double startX, double startY, double endX, double endY, Color color, double delay) {
        for (int i = 0; i < 3; i++) {
            Circle particle = new Circle(2.5, color);
            particle.setLayoutX(startX);
            particle.setLayoutY(startY);
            particle.setEffect(createSoftGlow(color, 8));
            particle.setOpacity(0.8);
            animationPane.getChildren().add(particle);
            Timeline flow = new Timeline(
                    new KeyFrame(Duration.ZERO,
                            new KeyValue(particle.layoutXProperty(), startX, Interpolator.LINEAR),
                            new KeyValue(particle.layoutYProperty(), startY, Interpolator.LINEAR),
                            new KeyValue(particle.opacityProperty(), 0.9, Interpolator.EASE_IN)
                    ),
                    new KeyFrame(Duration.seconds(2.0),
                            new KeyValue(particle.layoutXProperty(), endX, Interpolator.LINEAR),
                            new KeyValue(particle.layoutYProperty(), endY, Interpolator.LINEAR),
                            new KeyValue(particle.opacityProperty(), 0.9, Interpolator.EASE_OUT)
                    ),
                    new KeyFrame(Duration.seconds(2.1),
                            new KeyValue(particle.opacityProperty(), 0, Interpolator.EASE_OUT)
                    )
            );
            flow.setDelay(Duration.seconds(delay + i * 0.6));
            flow.setCycleCount(Timeline.INDEFINITE);
            allTimelines.add(flow);
        }
    }

    /**
     * Creates flowing particles along an arc/curve.
     * @param startX start x
     * @param startY start y
     * @param midX mid x (curve control)
     * @param midY mid y (curve control)
     * @param endX end x
     * @param endY end y
     * @param color color of the particles
     * @param delay animation delay
     */
    private void createArcFlowParticles(double startX, double startY, double midX, double midY, double endX, double endY, Color color, double delay) {
        for (int i = 0; i < 4; i++) {
            Circle particle = new Circle(2.5, color);
            particle.setLayoutX(startX);
            particle.setLayoutY(startY);
            particle.setEffect(createSoftGlow(color, 10));
            particle.setOpacity(0.85);
            animationPane.getChildren().add(particle);
            Timeline flow = new Timeline(
                    new KeyFrame(Duration.ZERO,
                            new KeyValue(particle.layoutXProperty(), startX, Interpolator.EASE_IN),
                            new KeyValue(particle.layoutYProperty(), startY, Interpolator.EASE_IN),
                            new KeyValue(particle.scaleXProperty(), 0.8, Interpolator.EASE_IN),
                            new KeyValue(particle.scaleYProperty(), 0.8, Interpolator.EASE_IN)
                    ),
                    new KeyFrame(Duration.seconds(1.0),
                            new KeyValue(particle.layoutXProperty(), midX, Interpolator.EASE_BOTH),
                            new KeyValue(particle.layoutYProperty(), midY, Interpolator.EASE_BOTH),
                            new KeyValue(particle.scaleXProperty(), 1.2, Interpolator.EASE_BOTH),
                            new KeyValue(particle.scaleYProperty(), 1.2, Interpolator.EASE_BOTH)
                    ),
                    new KeyFrame(Duration.seconds(2.5),
                            new KeyValue(particle.layoutXProperty(), endX, Interpolator.EASE_OUT),
                            new KeyValue(particle.layoutYProperty(), endY, Interpolator.EASE_OUT),
                            new KeyValue(particle.scaleXProperty(), 1.0, Interpolator.EASE_OUT),
                            new KeyValue(particle.scaleYProperty(), 1.0, Interpolator.EASE_OUT),
                            new KeyValue(particle.opacityProperty(), 0.85, Interpolator.EASE_OUT)
                    ),
                    new KeyFrame(Duration.seconds(2.7),
                            new KeyValue(particle.opacityProperty(), 0, Interpolator.EASE_OUT)
                    )
            );
            flow.setDelay(Duration.seconds(delay + i * 0.5));
            flow.setCycleCount(Timeline.INDEFINITE);
            allTimelines.add(flow);
        }
    }

    /**
     * Creates smooth circular motion timeline for a node.
     * @param node the circle node to animate
     * @param radius the radius of the circle
     * @param duration the duration of one loop (seconds)
     * @param startAngle the starting angle (radians)
     * @return the timeline for the circular motion
     */
    private Timeline createCircularMotionTimeline(Circle node, double radius, double duration, double startAngle) {
        final int keyFrameCount = 60;
        KeyFrame[] keyFrames = new KeyFrame[keyFrameCount + 1];
        for (int i = 0; i <= keyFrameCount; i++) {
            double progress = (double) i / keyFrameCount;
            double angle = startAngle + (2 * Math.PI * progress);
            double x = radius * Math.cos(angle);
            double y = radius * Math.sin(angle);
            keyFrames[i] = new KeyFrame(
                    Duration.seconds(duration * progress),
                    new KeyValue(node.translateXProperty(), x, Interpolator.LINEAR),
                    new KeyValue(node.translateYProperty(), y, Interpolator.LINEAR)
            );
        }
        return new Timeline(keyFrames);
    }

    /**
     * Creates soft glow effect for a node.
     * @param color the color of the glow
     * @param radius the radius of the glow
     * @return the effect to apply
     */
    private Effect createSoftGlow(Color color, double radius) {
        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(color);
        dropShadow.setRadius(radius);
        dropShadow.setSpread(0.3);
        Bloom bloom = new Bloom(0.15);
        bloom.setInput(dropShadow);
        return bloom;
    }
}