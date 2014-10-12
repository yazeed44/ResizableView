package yazeed44.net.resizeableviewlibary;

import android.graphics.Bitmap;
import android.graphics.Point;

/**
 * Created by yazeed44 on 10/11/14.
 */
public class ColorBall {

   private Bitmap bitmap;
    private Point point;
   private int id;
    static int count = 0;

    public ColorBall(final Bitmap bitmap, Point point) {
        this.id = count++;

        if (id > 4){
            throw new IllegalStateException("There's should be four balls only !!" + "\n"  + id);
        }

        this.bitmap = bitmap;
        this.point = point;
    }

    public int getWidthOfBall() {
        return bitmap.getWidth();
    }

    public int getHeightOfBall() {
        return bitmap.getHeight();
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public int getX() {
        return point.x;
    }

    public int getY() {
        return point.y;
    }

    public int getID() {
        return id;
    }

    public void setX(int x) {
        point.x = x;
    }

    public void setY(int y) {
        point.y = y;
    }
}
