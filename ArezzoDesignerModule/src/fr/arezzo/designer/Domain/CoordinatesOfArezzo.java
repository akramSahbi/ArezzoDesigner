package fr.arezzo.designer.Domain;

import fr.arezzo.designer.DomainWidgets.MyShuttleWidget;
import fr.arezzo.designer.DomainWidgets.types3.MyLoadUnloadWorkstationWidget;
import fr.arezzo.designer.DomainWidgets.types3.MyWorkstationWidget;
import fr.arezzo.designer.DomainWidgets.types4_8_9.MyLoadUnloadSensorWidget;
import fr.arezzo.designer.DomainWidgets.types4_8_9.MySensorWidget;
import fr.arezzo.designer.DomainWidgets.types4_8_9.MyStopSensorWidget;
import fr.arezzo.designer.DomainWidgets.types_6_10_11_12.MyConnectorWidget;
import fr.arezzo.designer.DomainWidgets.types_6_10_11_12.MySwitchInputWidget;
import fr.arezzo.designer.DomainWidgets.types_6_10_11_12.MySwitchIntermediateWidget;
import fr.arezzo.designer.DomainWidgets.types_6_10_11_12.MySwitchOutputWidget;
import fr.arezzo.designer.EditeurModule.Repositories.ConnectorRepository;
import fr.arezzo.designer.EditeurModule.Repositories.LoadUnloadSensorRepository;
import fr.arezzo.designer.EditeurModule.Repositories.LoadUnloadWorkstationRepository;
import fr.arezzo.designer.EditeurModule.Repositories.ShuttleRepository;
import fr.arezzo.designer.EditeurModule.Repositories.StopSensorRepository;
import fr.arezzo.designer.EditeurModule.Repositories.SwitchInputRepository;
import fr.arezzo.designer.EditeurModule.Repositories.SwitchIntermediateRepository;
import fr.arezzo.designer.EditeurModule.Repositories.SwitchOutputRepository;
import fr.arezzo.designer.EditeurModule.Repositories.SwitchSensorRepository;
import fr.arezzo.designer.EditeurModule.Repositories.SwitchStopSensorRepository;
import fr.arezzo.designer.EditeurModule.Repositories.WorkstationRepository;
import java.awt.Point;

/**
 * CoordinatesOfArezzo is a new coordinates system helper class that matches the
 * needs of the arezzo simulator input
 *
 * @author akram.sahbi@esprit.tn
 * @version 1.0
 */
public class CoordinatesOfArezzo {

    //scene boundaries to calculate the scene center
    //scale to have realistic measures from the designer
    static final int AREZZO_SCALE_X_Y = 50;
    //most west widget in the scene x corrdinates
    private static float mostWestWidgetInScene_X = 99999;

    //most east widget in the scene x corrdinates
    private static float mostEastWidgetInScene_X = -99999;

    //most top widget in the scene y corrdinates
    private static float mostTopWidgetInScene_Y = 99999;

    //most bottom widget in the scene y corrdinates
    private static float mostBottomWidgetInScene_Y = -999999;

    //the origin in the scene x coordinates
    private static float originWidgetInScene_X = -999999;
    //the origin in the scene y coordinates
    private static float originWidgetInScene_Y = 999999;

    //singleton design pattern
    //only one instance is present in memory
    static CoordinatesOfArezzo instance = null;

    /**
     * updateOriginOfTheScene is a method used to update the new Arezzo
     * coordinates origin in the scene x and y coordinates when we already have
     * initialized the boundaries of the scene's widgets, so that the new origin
     * matches the center of the widgets in the scene. we calculate the origin
     * as the center of a rectangle; by calculating the center of the west and
     * east side, then the center of the top and bottom sides, where these sides
     * represents the boundary widget positions in the scene
     */
    public static void updateOriginOfTheScene() {
        originWidgetInScene_X = (mostWestWidgetInScene_X + mostEastWidgetInScene_X) / 2;
        originWidgetInScene_Y = (mostTopWidgetInScene_Y
                + mostBottomWidgetInScene_Y) / 2;
    }

    /**
     * updateNewOriginOfTheSceneAndArezzoCoordinates is used to update the new
     * origin of the scene from the widgets in the repository, and their new
     * coordinates, so that we have the new coordinates that can be processed in
     * the simulator
     *
     */
    public static void updateNewOriginOfTheSceneAndArezzoCoordinates() {
        for (MyConnectorWidget widget : ConnectorRepository.getInstance().getAll()) {
            float x = widget.getMyConnectorInfo().getConnectorProperties().getxCoordinate();
            float y = widget.getMyConnectorInfo().getConnectorProperties().getyCoordinate();
            if (x < mostWestWidgetInScene_X) {
                mostWestWidgetInScene_X = x;
            }

            if (y > mostBottomWidgetInScene_Y) {
                mostBottomWidgetInScene_Y = y;
            }

            if (x > mostEastWidgetInScene_X) {
                mostEastWidgetInScene_X = x;
            }

            if (y < mostTopWidgetInScene_Y) {
                mostTopWidgetInScene_Y = y;
            }
        }

        for (MyLoadUnloadSensorWidget widget : LoadUnloadSensorRepository.getInstance().getAll()) {
            float x = widget.getLoadUnloadSensorProperties().getxCoordinate();
            float y = widget.getLoadUnloadSensorProperties().getyCoordinate();
            if (x < mostWestWidgetInScene_X) {
                mostWestWidgetInScene_X = x;
            }

            if (y > mostBottomWidgetInScene_Y) {
                mostBottomWidgetInScene_Y = y;
            }

            if (x > mostEastWidgetInScene_X) {
                mostEastWidgetInScene_X = x;
            }

            if (y < mostTopWidgetInScene_Y) {
                mostTopWidgetInScene_Y = y;
            }
        }

        for (MyLoadUnloadWorkstationWidget widget : LoadUnloadWorkstationRepository.getInstance().getAll()) {
            float x = widget.getLoadUnloadWorkstationProperties().getxCoordinate();
            float y = widget.getLoadUnloadWorkstationProperties().getyCoordinate();
            if (x < mostWestWidgetInScene_X) {
                mostWestWidgetInScene_X = x;
            }

            if (y > mostBottomWidgetInScene_Y) {
                mostBottomWidgetInScene_Y = y;
            }

            if (x > mostEastWidgetInScene_X) {
                mostEastWidgetInScene_X = x;
            }

            if (y < mostTopWidgetInScene_Y) {
                mostTopWidgetInScene_Y = y;
            }
        }

        for (MyShuttleWidget widget : ShuttleRepository.getInstance().getAll()) {
            float x = widget.getParentShuttleProperties().getxCoordinate();
            float y = widget.getParentShuttleProperties().getyCoordinate();
            if (x < mostWestWidgetInScene_X) {
                mostWestWidgetInScene_X = x;
            }

            if (y > mostBottomWidgetInScene_Y) {
                mostBottomWidgetInScene_Y = y;
            }

            if (x > mostEastWidgetInScene_X) {
                mostEastWidgetInScene_X = x;
            }

            if (y < mostTopWidgetInScene_Y) {
                mostTopWidgetInScene_Y = y;
            }
        }

        for (MyStopSensorWidget widget : StopSensorRepository.getInstance().getAll()) {
            float x = widget.getStopSensorProperties().getxCoordinate();
            float y = widget.getStopSensorProperties().getyCoordinate();
            if (x < mostWestWidgetInScene_X) {
                mostWestWidgetInScene_X = x;
            }

            if (y > mostBottomWidgetInScene_Y) {
                mostBottomWidgetInScene_Y = y;
            }

            if (x > mostEastWidgetInScene_X) {
                mostEastWidgetInScene_X = x;
            }

            if (y < mostTopWidgetInScene_Y) {
                mostTopWidgetInScene_Y = y;
            }
        }

        for (MySwitchInputWidget widget : SwitchInputRepository.getInstance().getAll()) {
            float x = widget.getSwitchInputNodeProperties().getxCoordinate();
            float y = widget.getSwitchInputNodeProperties().getyCoordinate();
            if (x < mostWestWidgetInScene_X) {
                mostWestWidgetInScene_X = x;
            }

            if (y > mostBottomWidgetInScene_Y) {
                mostBottomWidgetInScene_Y = y;
            }

            if (x > mostEastWidgetInScene_X) {
                mostEastWidgetInScene_X = x;
            }

            if (y < mostTopWidgetInScene_Y) {
                mostTopWidgetInScene_Y = y;
            }
        }

        for (MySwitchIntermediateWidget widget : SwitchIntermediateRepository.getInstance().getAll()) {
            float x = widget.getSwitchIntermediateNodeProperties().getxCoordinate();
            float y = widget.getSwitchIntermediateNodeProperties().getyCoordinate();
            if (x < mostWestWidgetInScene_X) {
                mostWestWidgetInScene_X = x;
            }

            if (y > mostBottomWidgetInScene_Y) {
                mostBottomWidgetInScene_Y = y;
            }

            if (x > mostEastWidgetInScene_X) {
                mostEastWidgetInScene_X = x;
            }

            if (y < mostTopWidgetInScene_Y) {
                mostTopWidgetInScene_Y = y;
            }
        }

        for (MySwitchOutputWidget widget : SwitchOutputRepository.getInstance().getAll()) {
            float x = widget.getSwitchOutputNodeProperties().getxCoordinate();
            float y = widget.getSwitchOutputNodeProperties().getyCoordinate();
            if (x < mostWestWidgetInScene_X) {
                mostWestWidgetInScene_X = x;
            }

            if (y > mostBottomWidgetInScene_Y) {
                mostBottomWidgetInScene_Y = y;
            }

            if (x > mostEastWidgetInScene_X) {
                mostEastWidgetInScene_X = x;
            }

            if (y < mostTopWidgetInScene_Y) {
                mostTopWidgetInScene_Y = y;
            }
        }

        for (MySensorWidget widget : SwitchSensorRepository.getInstance().getAll()) {
            float x = widget.getSensorProperties().getxCoordinate();
            float y = widget.getSensorProperties().getyCoordinate();
            if (x < mostWestWidgetInScene_X) {
                mostWestWidgetInScene_X = x;
            }

            if (y > mostBottomWidgetInScene_Y) {
                mostBottomWidgetInScene_Y = y;
            }

            if (x > mostEastWidgetInScene_X) {
                mostEastWidgetInScene_X = x;
            }

            if (y < mostTopWidgetInScene_Y) {
                mostTopWidgetInScene_Y = y;
            }
        }

        for (MyStopSensorWidget widget : SwitchStopSensorRepository.getInstance().getAll()) {
            float x = widget.getStopSensorProperties().getxCoordinate();
            float y = widget.getStopSensorProperties().getyCoordinate();
            if (x < mostWestWidgetInScene_X) {
                mostWestWidgetInScene_X = x;
            }

            if (y > mostBottomWidgetInScene_Y) {
                mostBottomWidgetInScene_Y = y;
            }

            if (x > mostEastWidgetInScene_X) {
                mostEastWidgetInScene_X = x;
            }

            if (y < mostTopWidgetInScene_Y) {
                mostTopWidgetInScene_Y = y;
            }
        }

        for (MyWorkstationWidget widget : WorkstationRepository.getInstance().getAll()) {
            float x = widget.getParentWorkstationProperties().getxCoordinate();
            float y = widget.getParentWorkstationProperties().getyCoordinate();
            if (x < mostWestWidgetInScene_X) {
                mostWestWidgetInScene_X = x;
            }

            if (y > mostBottomWidgetInScene_Y) {
                mostBottomWidgetInScene_Y = y;
            }

            if (x > mostEastWidgetInScene_X) {
                mostEastWidgetInScene_X = x;
            }

            if (y < mostTopWidgetInScene_Y) {
                mostTopWidgetInScene_Y = y;
            }
        }

        //update new origin coordinates
        updateOriginOfTheScene();

        //update new arezzo coordinates for each widget
        for (MyConnectorWidget widget : ConnectorRepository.getInstance().getAll()) {
            float x = widget.getMyConnectorInfo().getConnectorProperties().getxCoordinate();
            float y = widget.getMyConnectorInfo().getConnectorProperties().getyCoordinate();
            Point newArezzoCoordinate = CoordinatesOfArezzo.getInstance().updateSceneBoundaries(x, y, true);
            widget.getMyConnectorInfo().getConnectorProperties().setxCoordinateArezzo((float) newArezzoCoordinate.getX());
            widget.getMyConnectorInfo().getConnectorProperties().setyCoordinateArezzo((float) newArezzoCoordinate.getY());
        }

        for (MyLoadUnloadSensorWidget widget : LoadUnloadSensorRepository.getInstance().getAll()) {
            float x = widget.getLoadUnloadSensorProperties().getxCoordinate();
            float y = widget.getLoadUnloadSensorProperties().getyCoordinate();
            Point newArezzoCoordinate = CoordinatesOfArezzo.getInstance().updateSceneBoundaries(x, y, true);
            widget.getLoadUnloadSensorProperties().setxCoordinateArezzo((float) newArezzoCoordinate.getX());
            widget.getLoadUnloadSensorProperties().setyCoordinateArezzo((float) newArezzoCoordinate.getY());
        }

        for (MyLoadUnloadWorkstationWidget widget : LoadUnloadWorkstationRepository.getInstance().getAll()) {
            float x = widget.getLoadUnloadWorkstationProperties().getxCoordinate();
            float y = widget.getLoadUnloadWorkstationProperties().getyCoordinate();
            Point newArezzoCoordinate = CoordinatesOfArezzo.getInstance().updateSceneBoundaries(x, y, true);
            widget.getLoadUnloadWorkstationProperties().setxCoordinateArezzo((float) newArezzoCoordinate.getX());
            widget.getLoadUnloadWorkstationProperties().setyCoordinateArezzo((float) newArezzoCoordinate.getY());
        }

        for (MyShuttleWidget widget : ShuttleRepository.getInstance().getAll()) {
            float x = widget.getParentShuttleProperties().getxCoordinate();
            float y = widget.getParentShuttleProperties().getyCoordinate();
            Point newArezzoCoordinate = CoordinatesOfArezzo.getInstance().updateSceneBoundaries(x, y, true);
            widget.getParentShuttleProperties().setxCoordinateArezzo((float) newArezzoCoordinate.getX());
            widget.getParentShuttleProperties().setyCoordinateArezzo((float) newArezzoCoordinate.getY());
        }

        for (MyStopSensorWidget widget : StopSensorRepository.getInstance().getAll()) {
            float x = widget.getStopSensorProperties().getxCoordinate();
            float y = widget.getStopSensorProperties().getyCoordinate();
            Point newArezzoCoordinate = CoordinatesOfArezzo.getInstance().updateSceneBoundaries(x, y, true);
            widget.getStopSensorProperties().setxCoordinateArezzo((float) newArezzoCoordinate.getX());
            widget.getStopSensorProperties().setyCoordinateArezzo((float) newArezzoCoordinate.getY());
        }

        for (MySwitchInputWidget widget : SwitchInputRepository.getInstance().getAll()) {
            float x = widget.getSwitchInputNodeProperties().getxCoordinate();
            float y = widget.getSwitchInputNodeProperties().getyCoordinate();
            Point newArezzoCoordinate = CoordinatesOfArezzo.getInstance().updateSceneBoundaries(x, y, true);
            widget.getSwitchInputNodeProperties().setxCoordinateArezzo((float) newArezzoCoordinate.getX());
            widget.getSwitchInputNodeProperties().setyCoordinateArezzo((float) newArezzoCoordinate.getY());
        }

        for (MySwitchIntermediateWidget widget : SwitchIntermediateRepository.getInstance().getAll()) {
            float x = widget.getSwitchIntermediateNodeProperties().getxCoordinate();
            float y = widget.getSwitchIntermediateNodeProperties().getyCoordinate();
            Point newArezzoCoordinate = CoordinatesOfArezzo.getInstance().updateSceneBoundaries(x, y, true);
            widget.getSwitchIntermediateNodeProperties().setxCoordinateArezzo((float) newArezzoCoordinate.getX());
            widget.getSwitchIntermediateNodeProperties().setyCoordinateArezzo((float) newArezzoCoordinate.getY());
        }

        for (MySwitchOutputWidget widget : SwitchOutputRepository.getInstance().getAll()) {
            float x = widget.getSwitchOutputNodeProperties().getxCoordinate();
            float y = widget.getSwitchOutputNodeProperties().getyCoordinate();
            Point newArezzoCoordinate = CoordinatesOfArezzo.getInstance().updateSceneBoundaries(x, y, true);
            widget.getSwitchOutputNodeProperties().setxCoordinateArezzo((float) newArezzoCoordinate.getX());
            widget.getSwitchOutputNodeProperties().setyCoordinateArezzo((float) newArezzoCoordinate.getY());
        }

        for (MySensorWidget widget : SwitchSensorRepository.getInstance().getAll()) {
            float x = widget.getSensorProperties().getxCoordinate();
            float y = widget.getSensorProperties().getyCoordinate();
            Point newArezzoCoordinate = CoordinatesOfArezzo.getInstance().updateSceneBoundaries(x, y, true);
            widget.getSensorProperties().setxCoordinateArezzo((float) newArezzoCoordinate.getX());
            widget.getSensorProperties().setyCoordinateArezzo((float) newArezzoCoordinate.getY());
        }

        for (MyStopSensorWidget widget : SwitchStopSensorRepository.getInstance().getAll()) {
            float x = widget.getStopSensorProperties().getxCoordinate();
            float y = widget.getStopSensorProperties().getyCoordinate();
            Point newArezzoCoordinate = CoordinatesOfArezzo.getInstance().updateSceneBoundaries(x, y, true);
            widget.getStopSensorProperties().setxCoordinateArezzo((float) newArezzoCoordinate.getX());
            widget.getStopSensorProperties().setyCoordinateArezzo((float) newArezzoCoordinate.getY());
        }

        for (MyWorkstationWidget widget : WorkstationRepository.getInstance().getAll()) {
            float x = widget.getParentWorkstationProperties().getxCoordinate();
            float y = widget.getParentWorkstationProperties().getyCoordinate();
            Point newArezzoCoordinate = CoordinatesOfArezzo.getInstance().updateSceneBoundaries(x, y, true);
            widget.getParentWorkstationProperties().setxCoordinateArezzo((float) newArezzoCoordinate.getX());
            widget.getParentWorkstationProperties().setyCoordinateArezzo((float) newArezzoCoordinate.getY());
        }

    }

    /**
     * updateSceneBoundaries update new boundaries of the scene to find the new
     * center and new coordinates
     *
     * @param x represents the x coordinates of the widget based on the scene
     * coordinates system
     * @param y represents the y coordinates of the widget based on the scene
     * coordinates system
     * @param changedLocation to know whether the widget has been moved to
     * remake the process of calculating the new coordinates from scratch
     * @return a Point that represents the coordinates of the widget in the
     * arezzo coordinates system
     */
    public Point updateSceneBoundaries(float x, float y, boolean changedLocation) {
        boolean changedBoundaries = false;
        if (x < mostWestWidgetInScene_X) {
            changedBoundaries = true;
            mostWestWidgetInScene_X = x;
        }

        if (y > mostBottomWidgetInScene_Y) {
            changedBoundaries = true;
            mostBottomWidgetInScene_Y = y;
        }

        if (x > mostEastWidgetInScene_X) {
            changedBoundaries = true;
            mostEastWidgetInScene_X = x;
        }

        if (y < mostTopWidgetInScene_Y) {
            changedBoundaries = true;
            mostTopWidgetInScene_Y = y;
        }

        if (changedBoundaries) {
            updateOriginOfTheScene();

            return new ArezzoPoint(((x - originWidgetInScene_X) / AREZZO_SCALE_X_Y), (int) ((originWidgetInScene_Y - y) / AREZZO_SCALE_X_Y));
        }
        if (changedLocation) {
            return new ArezzoPoint(((x - originWidgetInScene_X) / AREZZO_SCALE_X_Y), ((originWidgetInScene_Y - y) / AREZZO_SCALE_X_Y));
        }
        changedBoundaries = false;
        return new Point((int) x, (int) y);
    }

    /**
     * ArezzoPoint represents the new arezzo coordinates point
     *
     */
    public class ArezzoPoint extends Point {

        //x coordinate
        private double arezzo_x;
        //y coordinate
        private double arezzo_y;

        /**
         * ArezzoPoint constructor to create a new point for the arezzo
         * coordinates system
         *
         * @param x
         * @param y
         */
        public ArezzoPoint(double x, double y) {
            arezzo_x = x;
            arezzo_y = y;
        }

        /**
         * getArezzo_x returns the x coordinates of the point (in the arezzo
         * coordinates system)
         *
         * @return the x coordinates of the point (in the arezzo coordinates
         * system)
         */
        public double getArezzo_x() {
            return arezzo_x;
        }

        /**
         * setArezzo_x sets the x coordinates of the point (in the arezzo
         * coordinates system)
         *
         * @param arezzo_x1 represents the x coordinates of the point (in the
         * arezzo coordinates system)
         */
        public void setArezzo_x(double arezzo_x1) {
            arezzo_x = arezzo_x1;
        }

        /**
         * getArezzo_y returns the y coordinate of the point (in the arezzo
         * coordinates system)
         *
         * @return a double value that represents the y coordinate of the point
         * (in the arezzo coordinates system)
         */
        public double getArezzo_y() {
            return arezzo_y;
        }

        /**
         * setArezzo_y sets the y coordinate of the point (in the arezzo
         * coordinates system)
         *
         * @param arezzo_y1
         */
        public void setArezzo_y(double arezzo_y1) {
            arezzo_y = arezzo_y1;
        }

        /**
         * getX returns the x coordinates of the point (in the arezzo
         * coordinates system) (is inherited and overridden)
         *
         * @return a double value that represents the y coordinate of the point
         * (in the arezzo coordinates system)
         */
        @Override
        public double getX() {
            return arezzo_x;
        }

        /**
         * setX sets the x coordinates of the point (in the arezzo coordinates
         * system) (is inherited and overridden)
         *
         * @param x a double value that represents the x coordinate of the point
         * (in the arezzo coordinates system)
         */
        public void setX(double x) {
            this.x = (int) x;
        }

        /**
         * getY returns the y coordinates of the point (in the arezzo
         * coordinates system) (is inherited and overridden)
         *
         * @return a double value that represents the y coordinate of the point
         * (in the arezzo coordinates system)
         */
        public double getY() {
            return arezzo_y;
        }

        /**
         * setY sets the y coordinates of the point (in the arezzo coordinates
         * system) (is inherited and overridden)
         *
         * @param y is the y coordinates of the point (in the arezzo coordinates
         * system)
         */
        public void setY(double y) {
            this.y = (int) y;
        }

    }

    /**
     * getInstance returns a singleton to have only one instance at a time
     * present in memory
     *
     * @return CoordinatesOfArezzo is an instance of this class to manage arezzo
     * coordinates system
     */
    public static CoordinatesOfArezzo getInstance() {
        if (instance == null) {
            instance = new CoordinatesOfArezzo();
        }
        return instance;
    }

    /**
     * setInstance sets a singleton to have only one instance at a time present
     * in memory
     *
     * @param instance is a CoordinatesOfArezzo that is an instance of this
     * class to manage arezzo coordinates system
     */
    public void setInstance(CoordinatesOfArezzo instance) {
        CoordinatesOfArezzo.instance = instance;
    }

    /**
     * getMostWestWidgetInScene_X returns the x coordinate of the widget that is
     * at most west in the scene
     *
     * @return a float that represents the x coordinate of the widget that is at
     * most west in the scene
     */
    public static float getMostWestWidgetInScene_X() {
        return mostWestWidgetInScene_X;
    }

    /**
     * setMostWestWidgetInScene_X sets the x coordinate of the widget that is at
     * most west in the scene
     *
     * @param mostWestWidgetInScene_X is a float that represents the x
     * coordinate of the widget that is at most west in the scene
     */
    public static void setMostWestWidgetInScene_X(float mostWestWidgetInScene_X) {
        CoordinatesOfArezzo.mostWestWidgetInScene_X = mostWestWidgetInScene_X;
    }

    /**
     * getMostEastWidgetInScene_X returns the x coordinate of the widget that is
     * at most east in the scene
     *
     * @return a float that represents the x coordinate of the widget that is at
     * most east in the scene
     */
    public static float getMostEastWidgetInScene_X() {
        return mostEastWidgetInScene_X;
    }

    /**
     * setMostEastWidgetInScene_X sets the x coordinate of the widget that is at
     * most east in the scene
     *
     * @param mostEastWidgetInScene_X a float that represents the x coordinate
     * of the widget that is at most east in the scene
     */
    public static void setMostEastWidgetInScene_X(float mostEastWidgetInScene_X) {
        CoordinatesOfArezzo.mostEastWidgetInScene_X = mostEastWidgetInScene_X;
    }

    /**
     * getMostTopWidgetInScene_Y returns the y coordinate of the widget that is
     * at most top in the scene
     *
     * @return a float that represents the y coordinate of the widget that is at
     * most top in the scene
     */
    public static float getMostTopWidgetInScene_Y() {
        return mostTopWidgetInScene_Y;
    }

    /**
     * setMostTopWidgetInScene_Y sets the y coordinate of the widget that is at
     * most top in the scene
     *
     * @param mostTopWidgetInScene_Y is a float that represents the y coordinate
     * of the widget that is at most top in the scene
     */
    public static void setMostTopWidgetInScene_Y(float mostTopWidgetInScene_Y) {
        CoordinatesOfArezzo.mostTopWidgetInScene_Y = mostTopWidgetInScene_Y;
    }

    /**
     * getMostBottomWidgetInScene_Y returns the y coordinate of the widget that
     * is at most bottom in the scene
     *
     * @return is a float that represents the y coordinate of the widget that is
     * at most bottom in the scene
     */
    public static float getMostBottomWidgetInScene_Y() {
        return mostBottomWidgetInScene_Y;
    }

    /**
     * setMostBottomWidgetInScene_Y sets the y coordinate of the widget that is
     * at most bottom in the scene
     *
     * @param mostBottomWidgetInScene_Y is a float that represents the y
     * coordinate of the widget that is at most bottom in the scene
     */
    public static void setMostBottomWidgetInScene_Y(float mostBottomWidgetInScene_Y) {
        CoordinatesOfArezzo.mostBottomWidgetInScene_Y = mostBottomWidgetInScene_Y;
    }

    /**
     * getOriginWidgetInScene_X returns the x coordinate of the origin in the
     * arezzo coordinates system
     *
     * @return a float that represents the x coordinate of the origin in the
     * arezzo coordinates system
     */
    public static float getOriginWidgetInScene_X() {
        return originWidgetInScene_X;
    }

    /**
     * setOriginWidgetInScene_X sets the x coordinate of the origin in the
     * arezzo coordinates system
     *
     * @param originWidgetInScene_X represents the x coordinate of the origin in
     * the arezzo coordinates system
     */
    public static void setOriginWidgetInScene_X(float originWidgetInScene_X) {
        CoordinatesOfArezzo.originWidgetInScene_X = originWidgetInScene_X;
    }

    /**
     * getOriginWidgetInScene_Y returns the y coordinate of the origin in the
     * arezzo coordinates system
     *
     * @return a float that represents the y coordinate of the origin in the
     * arezzo coordinates system
     */
    public static float getOriginWidgetInScene_Y() {
        return originWidgetInScene_Y;
    }

    /**
     * setOriginWidgetInScene_Y sets the y coordinate of the origin in the
     * arezzo coordinates system
     *
     * @param originWidgetInScene_Y represents a float that represents the y
     * coordinate of the origin in the arezzo coordinates system
     */
    public static void setOriginWidgetInScene_Y(float originWidgetInScene_Y) {
        CoordinatesOfArezzo.originWidgetInScene_Y = originWidgetInScene_Y;
    }

}
