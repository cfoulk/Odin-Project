package App.gui.component;

/**
 * EvWoN
 * BORROWED FROM: https://gist.github.com/hitchcock9307/b8d40576f11794c08cae783610771ea8
 *
 * Modified so you can specify which direction should be allowed to resize:
 * Usage: <pre>DragResizer.makeResizable(myPane, DragResizer.EAST + DragResizer.SOUTH);<pre>
 *
 * @Editor EvWoN ----> JohnnyAW (https://gist.github.com/hitchcock9307/b8d40576f11794c08cae783610771ea8#gistcomment-1907833)
 * @Authors atill, hitchcock9307
 */

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;

/**
 * BORROWED FROM: http://andrewtill.blogspot.com/2012/12/dragging-to-resize-javafx-region.html
 * Modified to support all side resizing (no edges)
 *
 * <p>
 * <p>
 * {@link DragResizer} can be used to add mouse listeners to a {@link Region}
 * and make it resizable by the user by clicking and dragging the border in the
 * same way as a window.
 * <p>
 * Only height resizing is currently implemented. Usage: <pre>DragResizer.makeResizable(myAnchorPane);</pre>
 *
 * @author atill, hitchcock9307
 */
public class DragResizer {

    /**
     * The margin around the control that a user can click in to start resizing
     * the region.
     */
    private static final int RESIZE_MARGIN = 2;

    private final Region region;

    private double y, x;

    private boolean initMinHeight;

    private short dragging = 0;

    private int allowedDirection = 0;

    public static final short NOTDRAGGING = 0;
    public static final short NORTH = 1;//changed to bits for easier compare
    public static final short SOUTH = 2;
    public static final short EAST = 4;
    public static final short WEST = 8;

    private DragResizer(Region aRegion, int allowedDirections) {
        this.region = aRegion;
        this.allowedDirection = allowedDirections;
    }

    public static void makeResizable(Region region, int allowedDirections) {
        final DragResizer resizer = new DragResizer(region, allowedDirections);

        region.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                resizer.mousePressed(event);
            }
        });
        region.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                resizer.mouseDragged(event);
            }
        });
        region.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                resizer.mouseOver(event);
            }
        });
        region.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                resizer.mouseReleased(event);
            }
        });
    }

    protected void mouseReleased(MouseEvent event) {
        initMinHeight = false; //Reset each time
        dragging = NOTDRAGGING;
        region.setCursor(Cursor.DEFAULT);
    }

    protected void mouseOver(MouseEvent event) {
        if (isInDraggableZoneS(event) || dragging == SOUTH) {
            region.setCursor(Cursor.S_RESIZE);
        } else if (isInDraggableZoneE(event) || dragging == EAST) {
            region.setCursor(Cursor.E_RESIZE);
        } else if (isInDraggableZoneN(event) || dragging == NORTH) {
            region.setCursor(Cursor.N_RESIZE);
        } else if (isInDraggableZoneW(event) || dragging == WEST) {
            region.setCursor(Cursor.W_RESIZE);
        } else {
            region.setCursor(Cursor.DEFAULT);
        }
    }

    //now we need to change all of this functions like this:
    private boolean isInDraggableZoneN(MouseEvent event) {
        return (this.allowedDirection & NORTH) > 0 && event.getY() < RESIZE_MARGIN;
    }

    private boolean isInDraggableZoneW(MouseEvent event) {
        return (this.allowedDirection & WEST) > 0 && event.getX() < RESIZE_MARGIN;
    }

    private boolean isInDraggableZoneS(MouseEvent event) { return (this.allowedDirection & SOUTH) > 0 &&  event.getY() > (region.getHeight() - RESIZE_MARGIN); }

    private boolean isInDraggableZoneE(MouseEvent event) { return (this.allowedDirection & EAST) > 0 && event.getX() > (region.getWidth() - RESIZE_MARGIN); }


    private void mouseDragged(MouseEvent event) {
        if (dragging == SOUTH) {
            region.setMinHeight(event.getY());
        } else if (dragging == EAST) {
            region.setMinWidth(event.getX());
        } else if (dragging == NORTH) {
            double prevMin = region.getMinHeight();
            region.setMinHeight(region.getMinHeight() - event.getY());
            if (region.getMinHeight() < region.getPrefHeight()) {
                region.setMinHeight(region.getPrefHeight());
                region.setTranslateY(region.getTranslateY() - (region.getPrefHeight() - prevMin));
                return;
            }
            if (region.getMinHeight() > region.getPrefHeight() || event.getY() < 0)
                region.setTranslateY(region.getTranslateY() + event.getY());
        } else if (dragging == WEST) {
            double prevMin = region.getMinWidth();
            region.setMinWidth(region.getMinWidth() - event.getX());
            if (region.getMinWidth() < region.getPrefWidth()) {
                region.setMinWidth(region.getPrefWidth());
                region.setTranslateX(region.getTranslateX() - (region.getPrefWidth() - prevMin));
                return;
            }
            if (region.getMinWidth() > region.getPrefWidth() || event.getX() < 0)
                region.setTranslateX(region.getTranslateX() + event.getX());
        }


    }

    private void mousePressed(MouseEvent event) {
        // ignore clicks outside of the draggable margin
        if (isInDraggableZoneE(event)) {
            dragging = EAST;
        } else if (isInDraggableZoneS(event)) {
            dragging = SOUTH;
        } else if (isInDraggableZoneN(event)) {
            dragging = NORTH;
        } else if (isInDraggableZoneW(event)) {
            dragging = WEST;
        } else
            return;


        // make sure that the minimum height is set to the current height once,
        // setting a min height that is smaller than the current height will
        // have no effect
        if (!initMinHeight) {
            region.setMinHeight(region.getHeight());
            region.setMinWidth(region.getWidth());
            initMinHeight = true;
        }

    }
}