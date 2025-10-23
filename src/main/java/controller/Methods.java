package controller;

import javafx.geometry.Point2D;
import javafx.scene.shape.Polygon;
import mains.Configg;
import model.MainGame_Logics;
import model.*;
import org.locationtech.jts.geom.*;

import java.util.List;
import java.util.Objects;

import static java.lang.Math.cos;
import static java.lang.Math.sin;


public class Methods {
    MainGame_Logics mainGameModel;
    public Methods(MainGame_Logics mainGamemodel){
        mainGameModel = mainGamemodel;
    }

    public boolean found_in_pairs(Signal signal1, Signal signal2) {
        for(Pairs pair : mainGameModel.staticDataModel.collapsedPairs){
            if(pair.signal1==signal1 && pair.signal2==signal2 || pair.signal1==signal2 && pair.signal2==signal1){
                return true;
            }

        }

        return false;
    }

    //simple just find first better
    public Object recommended_gate(Sysbox sysbox, Signal signal) {
        boolean second_found = false;
        Gate secound_gate = null;
        for(Gate gate:sysbox.outer_gates){
            if(gate.getWire().getSecondgate().getSysbox().isHealthy()) {
                if (Objects.equals(gate.getTypee().getShapeName(), signal.getTypee().getShapeName()) && !gate.isIn_use() && gate.getWire() != null) {
                    return gate;
                }
                if (!gate.isIn_use() && !second_found && gate.getWire() != null) {
                    second_found = true;
                    secound_gate = gate;
                }
            }
        }
        if(second_found){
            return secound_gate;
        }
        return null;

    }
    public Object nonRecommended_gate(Sysbox sysbox, Signal signal) {
        boolean second_found = false;
        Gate secound_gate = null;
        for(Gate gate:sysbox.outer_gates){
            if(gate.getWire().getSecondgate().getSysbox().isHealthy()) {
                if (!Objects.equals(gate.getTypee().getShapeName(), signal.getTypee().getShapeName()) && !gate.isIn_use() && gate.getWire() != null) {
                    return gate;
                }
                if (!gate.isIn_use() && !second_found && gate.getWire() != null) {
                    second_found = true;
                    secound_gate = gate;
                }
            }
        }
        if(second_found){
            return secound_gate;
        }
        return null;

    }
    public void update_signal_OnWire(Signal signal) {
        Wire wire=signal.getLinked_wire();

        Point2D pos = AllCurvesMethods.positionOnALength(wire,signal.getLength_on_wire());
        signal.setX_on_wire(pos.getX());
        signal.setY_on_wire(pos.getY());
        one_signal_update_polygon(signal);
    }
    private void one_signal_update_polygon(Signal signal){
        Configg cons = Configg.getInstance();
        double pi=3.01415;

        Polygon poly =signal.poly;
        poly.getPoints().clear();

        //just for debugging
        if(mainGameModel.virtual_run){
            System.out.println("virtual run polygon");
        }
        if(!signal.isSecure()) {
            if (Objects.equals(signal.getTypee().getShapeName(), "rectangle")) {
                poly.getPoints().addAll(signal.getX() - cons.getSignal_rectangle_width() / 2, signal.getY() - cons.getSignal_rectangle_height() / 2);
                poly.getPoints().addAll(signal.getX() - cons.getSignal_rectangle_width() / 2, signal.getY() + cons.getSignal_rectangle_height() / 2);
                poly.getPoints().addAll(signal.getX() + cons.getSignal_rectangle_width() / 2, signal.getY() + cons.getSignal_rectangle_height() / 2);
                poly.getPoints().addAll(signal.getX() + cons.getSignal_rectangle_width() / 2, signal.getY() - cons.getSignal_rectangle_height() / 2);
                poly.setFill(cons.getSignal_rectangle_color());
                System.out.println("add rectangle");
            }
            if (Objects.equals(signal.getTypee().getShapeName(), "triangle")) {
                for (int i = 0; i < 3; i++) {
                    poly.getPoints().addAll(signal.getX() - cons.getSignal_triangle_radius() * sin(i * 2 * pi / 3), signal.getY() - cons.getSignal_triangle_radius() * cos(i * 2 * pi / 3));
                }
                poly.setFill(cons.getSignal_triangle_color());
                System.out.println("add triangle");
            }
            if (Objects.equals(signal.getTypee().getShapeName(), "two6")) {
                /*
                 * This implements "two hexagons" as a single 10-sided polygon
                 * (decagon) representing two regular hexagons touching along
                 * a vertical edge. This is necessary because signal.poly
                 * can only be one continuous polygon.
                 *
                 * Requires:
                 * 1. cons.getSignal_two6_radius() [double]: The radius from center
                 * to vertex of one of the hexagons.
                 * 2. cons.getSignal_two6_color() [javafx.scene.paint.Paint]
                 *
                 * Note: Uses Math.PI and Math.cos/sin for accuracy, differing
                 * from the 'pi' variable and assumed static imports in the
                 * "triangle" implementation.
                 */

                // Center point of the combined shape
                double cx = signal.getX();
                double cy = signal.getY();

                // Radius of a single hexagon
                double r = cons.getSignal_two6_radius();

                // Pre-calculate geometric offsets based on a 60-degree (PI/3) angle
                // w_offset is the horizontal distance from a hex center to its vertical edge
                double w_offset = r * Math.sin(Math.PI / 3.0); // r * 0.866
                // h_offset is the vertical distance from a hex center to its shared vertex
                double h_offset = r * Math.cos(Math.PI / 3.0); // r * 0.5

                // Add the 10 vertices of the combined outline in counter-clockwise order
                // The shape is centered around (cx, cy)

                // Start at the top-middle "waist" vertex
                poly.getPoints().addAll(cx, cy - h_offset);             // V1

                // Left Hexagon outline
                poly.getPoints().addAll(cx - w_offset, cy - r);         // V2
                poly.getPoints().addAll(cx - (2 * w_offset), cy - h_offset); // V3
                poly.getPoints().addAll(cx - (2 * w_offset), cy + h_offset); // V4
                poly.getPoints().addAll(cx - w_offset, cy + r);         // V5

                // Bottom-middle "waist" vertex
                poly.getPoints().addAll(cx, cy + h_offset);             // V6

                // Right Hexagon outline
                poly.getPoints().addAll(cx + w_offset, cy + r);         // V7
                poly.getPoints().addAll(cx + (2 * w_offset), cy + h_offset); // V8
                poly.getPoints().addAll(cx + (2 * w_offset), cy - h_offset); // V9
                poly.getPoints().addAll(cx + w_offset, cy - r);         // V10

                poly.setFill(cons.getSignal_two6_color());
                System.out.println("add two6 (decagon)");
            }

        }
        else {
            //it's secure
            if (Objects.equals(signal.getTypee().getShapeName(), "hidden")) {

            }


            else {
                //secureCasual type
                poly.getPoints().clear();

                double lockBodyWidth = cons.getSignal_lock_length();   // e.g., 2 * r_outer_shackle
                double lockBodyHeight = cons.getSignal_lock_length(); // e.g., 2 * r_outer_shackle
                double shackleOuterRadius = cons.getSignal_lock_length();
                double shackleInnerRadius = 0.8*cons.getSignal_lock_length(); // Must be less than outer
                double shackleThickness = shackleOuterRadius - shackleInnerRadius; // Not directly used but defines the thickness

                // Define center of the *lock body*
                double cx = signal.getX();
                double cy = signal.getY();

                int arcSegments = 20; // More segments for smoother arcs

                // --- Start by defining the outer perimeter of the combined shape ---

                // 1. Bottom-left corner of the lock body
                poly.getPoints().addAll(cx - lockBodyWidth / 2, cy + lockBodyHeight / 2);

                // 2. Bottom-right corner of the lock body
                poly.getPoints().addAll(cx + lockBodyWidth / 2, cy + lockBodyHeight / 2);

                // 3. Top-right corner of the lock body (start of outer shackle arc)
                poly.getPoints().addAll(cx + lockBodyWidth / 2, cy - lockBodyHeight / 2);

                // Calculate the center for the shackle arcs
                // This center is typically aligned with the top edge of the body.
                double shackleArcCenterX = cx;
                double shackleArcCenterY = cy - lockBodyHeight / 2; // Top of the lock body

                // 4. Draw the outer semi-circle arc for the shackle (from right to left)
                // Angle goes from 2*pi (right) down to pi (left)
                for (int i = 0; i <= arcSegments; i++) {
                    // Angle from 2*pi down to pi (top half of a circle, going counter-clockwise visually)
                    double angle = (2 * pi) - (i * pi / (double) arcSegments);
                    poly.getPoints().addAll(shackleArcCenterX + shackleOuterRadius * cos(angle),
                            shackleArcCenterY + shackleOuterRadius * sin(angle));
                }

                // Now, we've traced the entire outer perimeter.
                // To create the "hole", we connect back to the inner perimeter and trace it
                // in the opposite direction (clockwise) to create a continuous path.

                // 5. Connect from the end of the outer arc (top-left of outer shackle)
                // to the start of the inner arc (top-left of inner shackle).
                // The start of the inner arc will be at angle 'pi' (left) for tracing clockwise.
                // However, a direct connection won't work perfectly. We need to jump directly
                // to the inner arc's start and then trace it.
                // So, we'll start tracing the inner arc from the top-left (angle 'pi').

                // We jump to the point where the inner arc *starts* if we trace it clockwise
                // from left to right, which corresponds to angle 'pi' to '2*pi'.
                // So, for the first point of the inner arc, we use angle 'pi'.
                poly.getPoints().addAll(shackleArcCenterX + shackleInnerRadius * cos(pi),
                        shackleArcCenterY + shackleInnerRadius * sin(pi));

                // 6. Draw the inner semi-circle arc (from left to right, going clockwise)
                // Angle goes from pi (left) up to 2*pi (right)
                for (int i = 1; i < arcSegments; i++) {
                    // Angle from pi up to 2*pi (top half of a circle, going clockwise visually)
                    double angle = pi + (i * pi / (double) arcSegments);
                    poly.getPoints().addAll(shackleArcCenterX + shackleInnerRadius * cos(angle),
                            shackleArcCenterY + shackleInnerRadius * sin(angle));
                }

                // 7. Connect from the end of the inner arc (top-right of inner shackle)
                // back to the lock body. This point is at (cx + lockBodyWidth / 2, cy - lockBodyHeight / 2)
                // This is actually the same as point 3 if shackle is aligned.
                // We've completed the inner loop. The polygon will now implicitly close by
                // connecting the last point of the inner arc back to the starting point
                // of the outer polygon (bottom-left). However, we need to explicitly connect
                // the inner arc's right side to the body.

                // So, after tracing the inner arc clockwise, the last point added is the top-right
                // of the inner shackle. We need to connect this point to the top-right corner of the
                // lock body, which we already visited as point 3.
                // It's cleaner to explicitly connect to the corner of the body for a continuous path.

                // Last point of inner arc is at angle 2*pi. We need to connect from here
                // back to the top-right corner of the *lock body*.
                // poly.getPoints().addAll(cx + lockBodyWidth / 2, cy - lockBodyHeight / 2); // This closes the inner gap

                // The JavaFX Polygon renders fill based on the "winding rule".
                // Tracing the outer perimeter counter-clockwise and the inner perimeter clockwise
                // usually creates the "hole" effect.

                // Let's re-evaluate the connection points for simplicity for a robust hole.
                // Trace outer from top-right of body (angle 2*pi) to top-left of body (angle pi).
                // Then, connect from top-left of body *to* top-left of inner shackle.
                // Then, trace inner from top-left of inner shackle (angle pi) to top-right of inner shackle (angle 2*pi).
                // Then, connect from top-right of inner shackle back to top-right of body.

                // This is the correct winding order for a "hole" with a single Polygon:
                // 1. Outer boundary (e.g., counter-clockwise)
                // 2. Connect to inner boundary
                // 3. Inner boundary (e.g., clockwise)
                // 4. Connect back to outer boundary to close the overall shape.

                // --- Corrected logic for a solid lock body with a thick shackle ---
                poly.getPoints().clear();

                // Lock Body (simple rectangle)
                // Bottom-left
                poly.getPoints().addAll(cx - lockBodyWidth / 2, cy + lockBodyHeight / 2);
                // Bottom-right
                poly.getPoints().addAll(cx + lockBodyWidth / 2, cy + lockBodyHeight / 2);
                // Top-right
                poly.getPoints().addAll(cx + lockBodyWidth / 2, cy - lockBodyHeight / 2);
                // Top-left
                poly.getPoints().addAll(cx - lockBodyWidth / 2, cy - lockBodyHeight / 2);
                // This completes the lock body. Now we will *add* the shackle on top of it.
                // This means the rectangle points will be part of the overall polygon.

                // Let's refine the shape: we want a lock with a rectangular body and a thick semi-circular shackle.
                // The Polygon must define the entire *outer* contour, then the *inner* contour of the hole.

                // Define the corners of the main lock body
                double bodyLeft = cx - lockBodyWidth / 2;
                double bodyRight = cx + lockBodyWidth / 2;
                double bodyBottom = cy + lockBodyHeight / 2;
                double bodyTop = cy - lockBodyHeight / 2;

                // Start tracing the outer perimeter of the entire lock (body + shackle)

                // 1. Bottom-left of body
                poly.getPoints().addAll(bodyLeft, bodyBottom);
                // 2. Bottom-right of body
                poly.getPoints().addAll(bodyRight, bodyBottom);

                // Now trace up the right side of the body, then around the outer shackle arc
                // 3. Top-right of body (this is where the outer shackle arc begins)
                poly.getPoints().addAll(bodyRight, bodyTop);

                // Outer shackle arc (from top-right of body, counter-clockwise)
                // Center for shackle arcs is (shackleArcCenterX, shackleArcCenterY)
                // which is bodyTop aligned with bodyWidth. Let's make it simpler,
                // center for shackle is (cx, bodyTop).

                shackleArcCenterX = cx;
                shackleArcCenterY = bodyTop; // Center for the arc is at the top of the body

                for (int i = 0; i <= arcSegments; i++) {
                    // Angle from 0 (right) to pi (left) for the top semi-circle (counter-clockwise)
                    double angle = (i * pi / (double) arcSegments); // Use Math.PI if available for precision
                    poly.getPoints().addAll(shackleArcCenterX + shackleOuterRadius * cos(angle),
                            shackleArcCenterY - shackleOuterRadius * sin(angle)); // -sin for arc *above* center
                }
                // At the end of this loop, we are at the top-left point of the outer shackle.

                // Now we connect from the outer contour to the inner contour to define the hole.
                // Jump to the top-left point of the *inner* shackle arc.
                // This point is at (shackleArcCenterX - shackleInnerRadius, shackleArcCenterY - shackleInnerRadius)

                // Ensure the inner arc also starts and ends at the vertical lines of the body for a clean look
                // The inner arc should also align with the top of the body, but offset by its radius

                // To make the lock clean, the inner shackle usually starts from the top-left and top-right of the body's internal section.
                // Let's make the "hole" directly above the main body, centered.

                // Trace the entire OUTER outline first (rectangle body + outer shackle)
                // Then trace the entire INNER outline (inner shackle + top of body that forms the base of the hole).

                // --- Simpler combined shape for the lock with a hole ---
                // This will define the outer edge of the lock, and then the inner edge of the shackle's hole.
                poly.getPoints().clear();

                // 1. Outer perimeter of the lock body + outer shackle

                // Bottom-left of body
                poly.getPoints().addAll(cx - lockBodyWidth / 2, cy + lockBodyHeight / 2);
                // Bottom-right of body
                poly.getPoints().addAll(cx + lockBodyWidth / 2, cy + lockBodyHeight / 2);
                // Top-right of body
                poly.getPoints().addAll(cx + lockBodyWidth / 2, cy - lockBodyHeight / 2);

                // Outer shackle arc (from top-right of body, counter-clockwise to top-left of body)
                // Center of the shackle's arc is at the mid-point of the top of the body.
                shackleArcCenterX = cx;
                shackleArcCenterY = cy - lockBodyHeight / 2;

                // Trace from angle 0 (right) to pi (left) for the upper semi-circle
                for (int i = 0; i <= arcSegments; i++) {
                    double angle = (i * pi / (double) arcSegments);
                    poly.getPoints().addAll(shackleArcCenterX + shackleOuterRadius * cos(angle),
                            shackleArcCenterY - shackleOuterRadius * sin(angle)); // -sin for arc *above* center
                }
                // After this loop, we are at the top-left of the outer shackle, which should align with (cx - lockBodyWidth / 2, cy - lockBodyHeight / 2) if lockBodyWidth == 2*shackleOuterRadius.
                // Let's assume shackleOuterRadius is half the body width for a neat fit.
                // If not, the polygon will auto-close from the last point of the arc to the initial bottom-left point.
                // To ensure it closes cleanly to the body, ensure the last arc point connects to (cx - lockBodyWidth / 2, cy - lockBodyHeight / 2).

                // This completes the *outer* boundary: bottom-left -> bottom-right -> top-right -> outer shackle arc -> top-left.
                // Now, we define the inner hole. We jump to a point on the inner boundary and trace it in the opposite direction (clockwise).

                // Inner shackle arc (from top-left of inner shackle, clockwise to top-right of inner shackle)
                // Start from the top-left of the inner shackle.
                // We'll jump to the inner arc's *start point* and trace it clockwise.
                // The start point will be (shackleArcCenterX - shackleInnerRadius, shackleArcCenterY - shackleInnerRadius)

                // First point of the inner loop (top-left of the inner shackle)
                poly.getPoints().addAll(shackleArcCenterX - shackleInnerRadius, shackleArcCenterY - shackleInnerRadius);

                // Trace the inner semi-circle clockwise (from left to right)
                // Angle goes from pi (left) to 2*pi (right), so starting at pi and incrementing.
                for (int i = 0; i <= arcSegments; i++) {
                    double angle = pi + (i * pi / (double) arcSegments); // +sin for arc *above* center, if we assume Y-down
                    // But since the shackle is 'up', it's still -sin
                    // Oh wait, for clockwise, sin values go negative then positive.
                    // Let's use negative angles for clockwise
                    poly.getPoints().addAll(shackleArcCenterX + shackleInnerRadius * cos(angle),
                            shackleArcCenterY - shackleInnerRadius * sin(angle));
                }

                // At the end of this loop, we are at the top-right of the inner shackle.
                // We now need to connect this point to the top-right of the body to complete the hole's base.
                // This is (shackleArcCenterX + shackleInnerRadius, shackleArcCenterY - shackleInnerRadius)

                // This creates a polygon that fills the space between the outer and inner paths.
                poly.setFill(cons.getSignal_lock_color());
                System.out.println("add lock with thick shackle");

                // --- Configuration in Configg.java (example, you'll need to add these) ---
                // private double signal_lock_body_width = 30;
                // private double signal_lock_body_height = 30;
                // private double signal_shackle_outer_radius = 15; // half of body width for nice fit
                // private double signal_shackle_inner_radius = 8;
                // private Paint signal_lock_color = Color.DARKGOLDENROD;


            }
        }
        System.out.println("end poly");
    }


    public static double calculate_wire_length(Wire wire) {
        double dx =  wire.getFirstgate().getX() - wire.getSecondgate().getX();
        double dy =  wire.getFirstgate().getY() - wire.getSecondgate().getY();
        return Math.sqrt(dx*dx+dy*dy);
    }


















    private static final GeometryFactory factory = new GeometryFactory();

    public static Object checkCollisionAndGetPoint(Polygon fxPoly1, Polygon fxPoly2) {



        org.locationtech.jts.geom.Polygon jtsPoly1 = toJTSPolygon(fxPoly1);
        org.locationtech.jts.geom.Polygon jtsPoly2 = toJTSPolygon(fxPoly2);

        if (jtsPoly1 == null || jtsPoly2 == null) return null;
        double mean_x=0;
        double mean_y=0;
        if (jtsPoly1.intersects(jtsPoly2)) {
            Geometry intersection = jtsPoly1.intersection(jtsPoly2);
            for (Coordinate coord : intersection.getCoordinates()) {
                mean_x+=coord.getX()/intersection.getCoordinates().length;
                mean_y+=coord.getY()/intersection.getCoordinates().length;
            }
            Coordinate interaction_point = new Coordinate(mean_x, mean_y);
            return interaction_point;
        } else {
            return null; // بدون برخورد
        }
    }

    private static org.locationtech.jts.geom.Polygon toJTSPolygon(Polygon fxPolygon) {
        List<Double> fxPoints = fxPolygon.getPoints();
        int numPoints = fxPoints.size();
        if (numPoints < 6 || numPoints % 2 != 0) return null;

        Coordinate[] coords = new Coordinate[(numPoints / 2) + 1];
        for (int i = 0; i < numPoints; i += 2) {
            coords[i / 2] = new Coordinate(fxPoints.get(i), fxPoints.get(i + 1));
        }
        coords[coords.length - 1] = coords[0]; // بستن حلقه

        return factory.createPolygon(coords);
    }

    public double calculate_distance(double x, double y, double x1, double y1) {
        double dx=x-x1;
        double dy=y-y1;
        return Math.sqrt(dx*dx+dy*dy);
    }

    public int number_of_healthy_spy(Sysbox sysbox1) {
        int number =0;
        for (Sysbox sysbox:mainGameModel.staticDataModel.sysboxes){
            if(sysbox.getState()=="data_spying" &&sysbox.isHealthy() && !Objects.equals(sysbox,sysbox1)){
                number=number+1;
            }
        }
        return number;
    }

    public Sysbox i_healthy_spy_sysbox(int targetIndex) {
        for (Sysbox sysbox:mainGameModel.staticDataModel.sysboxes){
            if(sysbox.getState()=="data_spying" &&sysbox.isHealthy()){
                if(targetIndex ==0){
                    return sysbox;
                }
                targetIndex = targetIndex -1;
            }
        }
        System.out.println("error in finding the spy");
        return null;
    }
}
