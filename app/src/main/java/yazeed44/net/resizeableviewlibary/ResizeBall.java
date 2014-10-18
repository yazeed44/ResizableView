package yazeed44.net.resizeableviewlibary;

import android.graphics.PointF;

/**
 * Created by yazeed44 on 10/11/14.
 */
public class ResizeBall {

    static int count = 0;
    private PointF point;
    private int id;

    public ResizeBall(PointF point) {
        this.id = count++;

        if (id > 4) {
            throw new IllegalStateException("There's should be four balls only !!" + "\n" + id);
        }

        this.point = point;
    }

    public int getX() {
        return (int) point.x;
    }

    public void setX(int x) {
        point.x = x;
    }

    public int getY() {
        return (int) point.y;
    }

    public void setY(int y) {
        point.y = y;
    }

    public int getID() {
        return id;
    }
}
