package controller;

import org.locationtech.jts.geom.*;

public class test {

    private static GeometryFactory factory = new GeometryFactory();


    public static void main(String[] args) {

        // مستطیل به عنوان چندضلعی
        Coordinate[] rectangleCoords = new Coordinate[] {
                new Coordinate(0, 0),
                new Coordinate(4, 0),
                new Coordinate(4, 3),
                new Coordinate(0, 3),
                new Coordinate(0, 0) // بستن حلقه
        };

        Polygon rectangle = factory.createPolygon(rectangleCoords);

        // مثلث به عنوان چندضلعی
        Coordinate[] triangleCoords = new Coordinate[] {
                new Coordinate(2, 1),
                new Coordinate(6, 1),
                new Coordinate(4, 4),
                new Coordinate(2, 1) // بستن حلقه
        };
        Polygon triangle = factory.createPolygon(triangleCoords);

        // بررسی برخورد
        if (rectangle.intersects(triangle)) {
            Geometry intersection = rectangle.intersection(triangle);
            System.out.println("برخورد وجود دارد!");
            System.out.println("نقطه/اشکال برخورد: " + intersection);
        } else {
            System.out.println("برخوردی وجود ندارد.");
        }
    }
}
