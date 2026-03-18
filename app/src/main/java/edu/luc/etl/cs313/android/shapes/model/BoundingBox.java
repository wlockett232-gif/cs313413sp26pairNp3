package edu.luc.etl.cs313.android.shapes.model;

/**
 * A shape visitor for calculating the bounding box, that is, the smallest
 * rectangle containing the shape. The resulting bounding box is returned as a
 * rectangle at a specific location.
 */
public class BoundingBox implements Visitor<Location> {

    // TODO entirely your job (except onCircle)

    @Override
    public Location onCircle(final Circle c) {
        final int radius = c.getRadius();
        return new Location(-radius, -radius, new Rectangle(2 * radius, 2 * radius));
    }

    @Override
    public Location onFill(final Fill f) {
        return f.getShape().accept(this);
    }

    @Override
    public Location onStrokeColor(final StrokeColor c) {
        return c.getShape().accept(this);
    }

    @Override
    public Location onOutline(final Outline o) {
        return o.getShape().accept(this);
    }

    @Override
    public Location onRectangle(final Rectangle r) {
        return new Location(0, 0, r);
    }

    @Override
    public Location onLocation(final Location l) {
        Location inner = l.getShape().accept(this);

        return new Location(
                l.getX() + inner.getX(),
                l.getY() + inner.getY(),
                inner.getShape()
        );
    }

    @Override
    public Location onGroup(final Group g) {
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;

        for (Shape s : g.getShapes()) {
            Location b = s.accept(this);
            Rectangle r = (Rectangle) b.getShape();

            minX = Math.min(minX, b.getX());
            minY = Math.min(minY, b.getY());
            maxX = Math.max(maxX, b.getX() + r.getWidth());
            maxY = Math.max(maxY, b.getY() + r.getHeight());
        }

        return new Location(
                minX,
                minY,
                new Rectangle(maxX - minX, maxY - minY)
        );
    }

    @Override
    public Location onPolygon(final Polygon p) {
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;

        for (Point pt : p.getPoints()) {
            int x = pt.getX();
            int y = pt.getY();

            minX = Math.min(minX, x);
            minY = Math.min(minY, y);
            maxX = Math.max(maxX, x);
            maxY = Math.max(maxY, y);
        }

        return new Location(
                minX,
                minY,
                new Rectangle(maxX - minX, maxY - minY)
        );
    }
}