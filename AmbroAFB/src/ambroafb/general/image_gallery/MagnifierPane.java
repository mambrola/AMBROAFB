/**
 * (C) 2013 HealthConnect NV. All rights reserved.
 */
package ambroafb.general.image_gallery;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Bounds;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.ParallelCamera;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.SnapshotResult;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Scale;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * Pane which shows the details of its children in a magnified.
 */
public class MagnifierPane extends StackPane {

    // Configurable properties
    private DoubleProperty radius;
    private DoubleProperty frameWidth;
    private DoubleProperty scaleFactor;
    private DoubleProperty scopeLineWidth;
    private BooleanProperty scopeLinesVisible;
    private double minScale = 1, maxScale = 10;

    // Default values
    private final double DEFAULT_RADIUS = 150.0D;
    private final double DEFAULT_FRAME_WIDTH = 5.5D;
    private final double DEFAULT_SCALE_FACTOR = 3.0D;
    private final double DEFAULT_SCOPELINE_WIDTH = 1.5D;
    private final boolean DEFAULT_SCOPELINE_VISIBLE = false;

    private Scene scene;
    private WritableImage writeImg;
    final Popup viewer;
    private BooleanProperty showProperty = new SimpleBooleanProperty(true);

    public MagnifierPane() {
        super();
        Image imageCursor = new Image("/images/magnifier.png");
        ImageCursor logoCursor = new ImageCursor(imageCursor, imageCursor.getWidth() / 2, imageCursor.getHeight() /2);
        cursorProperty().bind(Bindings.createObjectBinding(() -> {
            return showProperty.get() ? logoCursor : Cursor.DEFAULT;
        }, showProperty));
        final ImageView snapView = new ImageView();
        final Callback<SnapshotResult, java.lang.Void> callBack = (SnapshotResult result) -> null;
        final Scale scale = new Scale();
        scale.xProperty().bind(scaleFactorProperty());
        scale.yProperty().bind(scaleFactorProperty());
        final SnapshotParameters param = new SnapshotParameters();
        param.setCamera(new ParallelCamera());
        param.setDepthBuffer(true);
        param.setTransform(scale);

        final StackPane mainContent = new StackPane();
        final Circle cEdge = new Circle();
        cEdge.setStyle("-fx-fill:radial-gradient(focus-angle 0deg , focus-distance 0% , center 50% 50% , radius 50% , #f0f8ff 93% , #696969 94% , #FaFaFa 97% , #808080);");
        cEdge.radiusProperty().bind(new DoubleBinding() {
            {
                bind(radiusProperty(), frameWidthProperty());
            }

            @Override
            protected double computeValue() {
                return getRadius() + getFrameWidth();
            }
        });

        final Circle cClip = new Circle();
        cClip.radiusProperty().bind(radiusProperty());
        cClip.translateXProperty().bind(radiusProperty());
        cClip.translateYProperty().bind(radiusProperty());

        final Magnifier clippedNode = new Magnifier(radiusProperty(), radiusProperty());
        clippedNode.setClip(cClip);

        // Adding all parts in a container.
        mainContent.getChildren().addAll(cEdge, clippedNode);

        viewer = new Popup();
        viewer.getContent().add(mainContent);
        viewer.autoHideProperty().set(true);

        mainContent.addEventFilter(ScrollEvent.SCROLL, (ScrollEvent event) -> {
            double dY = event.getDeltaY();
            double oldScale = getScaleFactor();
            double scale1 = oldScale - dY / 40;
            if (scale1 > minScale && scale1 < maxScale) {
                setScaleFactor(scale1);
                fireEvent(new MouseEvent(MouseEvent.MOUSE_ENTERED, event.getX(), event.getY(), event.getScreenX(), event.getScreenY(), MouseButton.NONE, 0, false, false, false, false, false, false, false, false, false, false, null));
                double ratio = scale1 / oldScale;
                double w = clippedNode.getWidth();
                double h = clippedNode.getHeight();
                clippedNode.transXProperty().set((clippedNode.transXProperty().get() + w / 2) * ratio - w / 2);
                clippedNode.transYProperty().set((clippedNode.transYProperty().get() + h / 2) * ratio - h / 2);
            }
        });

        addEventFilter(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
            if (showProperty.get()) {
                viewer.show(MagnifierPane.this, e.getScreenX(), e.getScreenY() - (2 * getRadius()));
                int w = (int) (MagnifierPane.this.getWidth() * getScaleFactor());
                int h = (int) (MagnifierPane.this.getHeight() * getScaleFactor());
                if (w > 0 && h > 0) {
                    writeImg = new WritableImage(w, h);

                    // Get snapshot image
                    MagnifierPane.this.snapshot(callBack, param, writeImg);
                    snapView.setImage(writeImg);
                    clippedNode.setContent(snapView);
                }
            }
        });

        addEventFilter(MouseEvent.MOUSE_MOVED, (MouseEvent e) -> {
            final double r = getRadius();
            final double s = getScaleFactor();
            viewer.setX(e.getScreenX());
            viewer.setY(e.getScreenY() - (2 * r));
            
            clippedNode.transXProperty().set((e.getX() * s) - r);
            clippedNode.transYProperty().set((e.getY() * s) - r);
        });

    }
    
    public BooleanProperty showProperty(){
        return showProperty;
    }
    
    @Override
    protected void layoutChildren() {
        super.layoutChildren();
        if (this.scene == null) {
            this.scene = getScene();
            Stage ownerStage = (Stage) scene.getWindow();
            scene.addEventFilter(MouseEvent.MOUSE_MOVED, (MouseEvent event) -> {
                double x = event.getSceneX();
                double y = event.getSceneY();
                Bounds thisB = localToScene(getLayoutBounds());
                if ((x < thisB.getMinX() || x > thisB.getMaxX() || y < thisB.getMinY() || y > thisB.getMaxY()) || !showProperty.get()) {
                    viewer.hide();
                }
                else if (!ownerStage.isFocused()){
                    ownerStage.requestFocus();
                }
            });
        }
    }

    // Getters & Setters
    public final DoubleProperty radiusProperty() {
        if (this.radius == null) {
            this.radius = new DoublePropertyBase(DEFAULT_RADIUS) {
                @Override
                public String getName() {
                    return "radius";
                }

                @Override
                public Object getBean() {
                    return MagnifierPane.this;
                }
            };
        }
        return this.radius;
    }

    public final void setRadius(Double paramRadius) {
        radiusProperty().setValue(paramRadius);
    }

    public final Double getRadius() {
        return ((this.radius == null) ? DEFAULT_RADIUS : this.radius.getValue());
    }

    public final DoubleProperty frameWidthProperty() {
        if (this.frameWidth == null) {
            this.frameWidth = new DoublePropertyBase(DEFAULT_FRAME_WIDTH) {
                @Override
                public String getName() {
                    return "frameWidth";
                }

                @Override
                public Object getBean() {
                    return MagnifierPane.this;
                }
            };
        }
        return this.frameWidth;
    }

    public final void setFrameWidth(Double paramFrameWidth) {
        frameWidthProperty().setValue(paramFrameWidth);
    }

    public final Double getFrameWidth() {
        return ((this.frameWidth == null) ? DEFAULT_FRAME_WIDTH : this.frameWidth.getValue());
    }

    public final DoubleProperty scaleFactorProperty() {
        if (this.scaleFactor == null) {
            this.scaleFactor = new DoublePropertyBase(DEFAULT_SCALE_FACTOR) {
                @Override
                public String getName() {
                    return "scaleFactor";
                }

                @Override
                public Object getBean() {
                    return MagnifierPane.this;
                }
            };
        }
        return this.scaleFactor;
    }

    public final void setScaleFactor(Double paramScaleFactor) {
        scaleFactorProperty().setValue(paramScaleFactor);
    }

    public final Double getScaleFactor() {
        return ((this.scaleFactor == null) ? DEFAULT_SCALE_FACTOR : this.scaleFactor.getValue());
    }

    public final DoubleProperty scopeLineWidthProperty() {
        if (this.scopeLineWidth == null) {
            this.scopeLineWidth = new DoublePropertyBase(DEFAULT_SCOPELINE_WIDTH) {
                @Override
                public String getName() {
                    return "scopeLineWidth";
                }

                @Override
                public Object getBean() {
                    return MagnifierPane.this;
                }
            };
        }
        return this.scopeLineWidth;
    }

    public final void setScopeLineWidth(Double paramScopeLineWidth) {
        scopeLineWidthProperty().setValue(paramScopeLineWidth);
    }

    public final Double getScopeLineWidth() {
        return ((this.scopeLineWidth == null) ? DEFAULT_SCOPELINE_WIDTH : this.scopeLineWidth.getValue());
    }

    public final BooleanProperty scopeLinesVisibleProperty() {
        if (this.scopeLinesVisible == null) {
            this.scopeLinesVisible = new BooleanPropertyBase(DEFAULT_SCOPELINE_VISIBLE) {

                @Override
                public String getName() {
                    return "scopeLinesVisible";
                }

                @Override
                public Object getBean() {
                    return MagnifierPane.this;
                }
            };
        }
        return this.scopeLinesVisible;
    }

    public final void setScopeLinesVisible(Boolean paramScopeLinesVisible) {
        scopeLinesVisibleProperty().setValue(paramScopeLinesVisible);
    }

    public final Boolean getScopeLinesVisible() {
        return ((this.scopeLinesVisible == null) ? DEFAULT_SCOPELINE_VISIBLE : this.scopeLinesVisible.getValue());
    }

    /**
     * Region that holds the clip area.
     */
    class Magnifier extends Region {

        private Node content;
        private final DoubleProperty width = new SimpleDoubleProperty();
        private final DoubleProperty height = new SimpleDoubleProperty();
        private final Rectangle clip;
        private final SimpleDoubleProperty transX = new SimpleDoubleProperty();
        private final SimpleDoubleProperty transY = new SimpleDoubleProperty();

        public Magnifier(DoubleProperty w, DoubleProperty h) {
            this.width.bind(w.multiply(2));
            this.height.bind(h.multiply(2));
            this.clip = new Rectangle();
            this.clip.widthProperty().bind(this.width);
            this.clip.heightProperty().bind(this.height);
            this.clip.translateXProperty().bind(transX);
            this.clip.translateYProperty().bind(transY);
        }

        public void setContent(Node content) {
            if (this.content != null) {
                this.content.setClip(null);
                this.content.translateXProperty().unbind();
                this.content.translateYProperty().unbind();
                getChildren().clear();
            }
            this.content = content;
            this.content.setClip(this.clip);
            this.content.translateXProperty().bind(transX.multiply(-1));
            this.content.translateYProperty().bind(transY.multiply(-1));
            getChildren().setAll(content);
        }

        @Override
        protected double computeMinWidth(double d) {
            return width.get();
        }

        @Override
        protected double computeMinHeight(double d) {
            return height.get();
        }

        @Override
        protected double computePrefWidth(double d) {
            return width.get();
        }

        @Override
        protected double computePrefHeight(double d) {
            return height.get();
        }

        @Override
        protected double computeMaxWidth(double d) {
            return width.get();
        }

        @Override
        protected double computeMaxHeight(double d) {
            return height.get();
        }

        public SimpleDoubleProperty transXProperty() {
            return transX;
        }

        public SimpleDoubleProperty transYProperty() {
            return transY;
        }
    }
}
