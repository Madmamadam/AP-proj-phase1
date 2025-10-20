//package handelers;
//
//import javafx.scene.shape.*;
//import org.locationtech.jts.geom.Coordinate;
//import org.locationtech.jts.geom.GeometryFactory;
//import org.locationtech.jts.geom.Polygon;
//import org.locationtech.jts.geom.LinearRing;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Utility class to convert JavaFX Shapes to JTS Geometry objects.
// * This version corrects defects in Polygon and Path handling.
// */
//public class FxJtsConverter {
//
//    /**
//     * Shared JTS GeometryFactory.
//     */
//    private static final GeometryFactory factory = new GeometryFactory();
//
//    /**
//     * Default number of segments used to approximate curves (e.g., Circles, Ellipses).
//     */
//    private static final int DEFAULT_SEGMENTS = 64;
//
//    /**
//     * Attempts to convert a generic JavaFX Shape into a JTS Polygon.
//     *
//     * @param fxShape The input JavaFX Shape (e.g., Polygon, Rectangle, Circle).
//     * @return A JTS Polygon representation, or null if the shape type is unsupported
//     * or the geometry is invalid.
//     */
//    public static org.locationtech.jts.geom.Polygon toJTSPolygon(Shape fxShape) {
//        if (fxShape instanceof Polygon) {
//            Polygon jtsPolygon = toJTSPolygon((Polygon) fxShape);
//            return jtsPolygon;
//        }
//        if (fxShape instanceof Rectangle) {
//            return toJTSPolygon((Rectangle) fxShape);
//        }
//        if (fxShape instanceof Circle) {
//            return toJTSPolygon((Circle) fxShape, DEFAULT_SEGMENTS);
//        }
//        if (fxShape instanceof Ellipse) {
//            return toJTSPolygon((Ellipse) fxShape, DEFAULT_SEGMENTS);
//        }
//        if (fxShape instanceof Path) {
//            return toJTSPolygon((Path) fxShape);
//        }
//
//        // Return null for unsupported shapes (e.g., Line, Arc, SVGPath)
//        return null;
//    }
//
//    /**
//     * Converts a JavaFX Polygon to a JTS Polygon.
//     *
//     * This implementation correctly handles JavaFX Polygons that are
//     * either "open" (first and last points differ) or "closed"
//     * (first and last points are identical).
//     *
//     * @param fxPolygon The JavaFX Polygon.
//     * @return A JTS Polygon, or null if points are invalid.
//     */
//    private static org.locationtech.jts.geom.Polygon toJTSPolygon(Polygon fxPolygon) {
//        List<Double> fxPoints = fxPolygon.getPoints();
//        int numPoints = fxPoints.size();
//
//        // A valid polygon shell requires at least 3 vertices (6 coordinates)
//        if (numPoints < 6 || numPoints % 2 != 0) {
//            return null;
//        }
//
//        int numVertices = numPoints / 2;
//        Coordinate[] coords = new Coordinate[numVertices];
//        for (int i = 0; i < numPoints; i += 2) {
//            coords[i / 2] = new Coordinate(fxPoints.get(i), fxPoints.get(i + 1));
//        }
//
//        // Check if the JavaFX Polygon was already explicitly closed
//        if (coords[0].equals(coords[numVertices - 1])) {
//            // JTS requires 4+ points for a LinearRing (A, B, C, A)
//            if (numVertices < 4) {
//                return null;
//            }
//            return factory.createPolygon(coords);
//        }
//
//        // If not closed, we must close it.
//        // The shell must have at least 3 vertices (A, B, C)
//        if (numVertices < 3) {
//            return null;
//        }
//
//        Coordinate[] closedCoords = new Coordinate[numVertices + 1];
//        System.arraycopy(coords, 0, closedCoords, 0, numVertices);
//        closedCoords[numVertices] = coords[0]; // Add closing point
//
//        return factory.createPolygon(closedCoords);
//    }
//
//    /**
//     * Converts a JavaFX Rectangle to a JTS Polygon.
//     *
//     * @param fxRectangle The JavaFX Rectangle.
//     * @return A 5-point JTS Polygon (4 corners + 1 closing point).
//     */
//    private static org.locationtech.jts.geom.Polygon toJTSPolygon(Rectangle fxRectangle) {
//        double x = fxRectangle.getX();
//        double y = fxRectangle.getY();
//        double width = fxRectangle.getWidth();
//        double height = fxRectangle.getHeight();
//
//        Coordinate[] coords = new Coordinate[5];
//        coords[0] = new Coordinate(x, y);
//        coords[1] = new Coordinate(x + width, y);
//        coords[2] = new Coordinate(x + width, y + height);
//        coords[3] = new Coordinate(x, y + height);
//        coords[4] = new Coordinate(x, y); // Close the ring
//
//        return factory.createPolygon(coords);
//    }
//
//    /**
//     * Converts a JavaFX Circle to an approximated JTS Polygon.
//     *
//     * @param fxCircle    The JavaFX Circle.
//     * @param numSegments The number of line segments to use for approximation.
//     * @return A JTS Polygon.
//     */
//    public static org.locationtech.jts.geom.Polygon toJTSPolygon(Circle fxCircle, int numSegments) {
//        if (numSegments < 3) numSegments = DEFAULT_SEGMENTS;
//
//        double centerX = fxCircle.getCenterX();
//        double centerY = fxCircle.getCenterY();
//        double radius = fxCircle.getRadius();
//
//        Coordinate[] coords = new Coordinate[numSegments + 1];
//        double angleStep = 2 * Math.PI / numSegments;
//
//        for (int i = 0; i < numSegments; i++) {
//            double angle = i * angleStep;
//            double x = centerX + radius * Math.cos(angle);
//            double y = centerY + radius * Math.sin(angle);
//            coords[i] = new Coordinate(x, y);
//        }
//        coords[numSegments] = coords[0]; // Close the ring
//
//        return factory.createPolygon(coords);
//    }
//
//    /**
//     * Converts a JavaFX Ellipse to an approximated JTS Polygon.
//     *
//     * @param fxEllipse   The JavaFX Ellipse.
//     * @param numSegments The number of line segments to use for approximation.
//     * @return A JTS Polygon.
//     */
//    public static org.locationtech.jts.geom.Polygon toJTSPolygon(Ellipse fxEllipse, int numSegments) {
//        if (numSegments < 3) numSegments = DEFAULT_SEGMENTS;
//
//        double centerX = fxEllipse.getCenterX();
//        double centerY = fxEllipse.getCenterY();
//        double radiusX = fxEllipse.getRadiusX();
//        double radiusY = fxEllipse.getRadiusY();
//
//        Coordinate[] coords = new Coordinate[numSegments + 1];
//        double angleStep = 2 * Math.PI / numSegments;
//
//        for (int i = 0; i < numSegments; i++) {
//            double angle = i * angleStep;
//            double x = centerX + radiusX * Math.cos(angle);
//            double y = centerY + radiusY * Math.sin(angle);
//            coords[i] = new Coordinate(x, y);
//        }
//        coords[numSegments] = coords[0]; // Close the ring
//
//        return factory.createPolygon(coords);
//    }
//
//    /**
//     * Converts a JavaFX Path to a JTS Polygon (Simplified).
//     *
//     * WARNING: This method remains simplified. It only processes the first
//     * continuous sequence of MoveTo and LineTo elements and IGNORES all
//     * curve elements (QuadCurveTo, CubicCurveTo, ArcTo).
//     *
//     * This corrected version *does* respect the isAbsolute() flag.
//     *
//     * @param fxPath The JavaFX Path.
//     * @return A JTS Polygon, or null if no valid linear path is found.
//     */
//    private static org.locationtech.jts.geom.Polygon toJTSPolygon(Path fxPath) {
//        List<Coordinate> coordinates = new ArrayList<>();
//        boolean inPath = false;
//        double currentX = 0.0;
//        double currentY = 0.0;
//        double startX = 0.0;
//        double startY = 0.0;
//
//        for (PathElement element : fxPath.getElements()) {
//            if (element instanceof MoveTo) {
//                if (inPath) {
//                    // Found a new sub-path. This converter only handles the first shell.
//                    break;
//                }
//                MoveTo moveTo = (MoveTo) element;
//                double x, y;
//                if (moveTo.isAbsolute()) {
//                    x = moveTo.getX();
//                    y = moveTo.getY();
//                } else {
//                    x = currentX + moveTo.getX();
//                    y = currentY + moveTo.getY();
//                }
//
//                coordinates.add(new Coordinate(x, y));
//                currentX = x;
//                currentY = y;
//                startX = x; // Store start of this sub-path
//                startY = y;
//                inPath = true;
//
//            } else if (element instanceof LineTo) {
//                if (!inPath) continue; // Ignore lines without a starting MoveTo
//
//                LineTo lineTo = (LineTo) element;
//                double x, y;
//                if (lineTo.isAbsolute()) {
//                    x = lineTo.getX();
//                    y = lineTo.getY();
//                } else {
//                    x = currentX + lineTo.getX();
//                    y = currentY + lineTo.getY();
//                }
//
//                coordinates.add(new Coordinate(x, y));
//                currentX = x;
//                currentY = y;
//
//            } else if (element instanceof ClosePath) {
//                if (!inPath) continue;
//                // ClosePath connects back to the start of this sub-path
//                if (currentX != startX || currentY != startY) {
//                    coordinates.add(new Coordinate(startX, startY));
//                }
//                currentX = startX;
//                currentY = startY;
//                inPath = false; // Mark sub-path as complete
//                break; // Stop processing (only handle one shell)
//
//            } else if (element instanceof ArcTo || element instanceof CubicCurveTo || element instanceof QuadCurveTo) {
//                // As documented, curves are ignored in this simplified version.
//                if (inPath) {
//                    System.err.println("Warning: FxJtsConverter skipping curve element: " + element.getClass().getSimpleName());
//                }
//            }
//        }
//
//        // A polygon needs at least 3 vertices
//        if (coordinates.size() < 3) {
//            return null;
//        }
//
//        // Ensure the ring is closed (if ClosePath was missing)
//        Coordinate first = coordinates.get(0);
//        Coordinate last = coordinates.get(coordinates.size() - 1);
//        if (!first.equals(last)) {
//            coordinates.add(first);
//        }
//
//        // Final check for JTS validity (4+ points)
//        if (coordinates.size() < 4) {
//            return null;
//        }
//
//        try {
//            LinearRing shell = factory.createLinearRing(coordinates.toArray(new Coordinate[0]));
//            return factory.createPolygon(shell);
//        } catch (Exception e) {
//            // Could fail if coordinates form a self-intersecting ring
//            System.err.println("Error creating JTS Polygon from Path: " + e.getMessage());
//            return null;
//        }
//    }
//}