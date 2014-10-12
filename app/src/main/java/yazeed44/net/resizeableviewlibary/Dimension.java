package yazeed44.net.resizeableviewlibary;

/**
 * Created by yazeed44 on 10/11/14.
 */
public  class Dimension {

    private int width;
    private int height;

    public Dimension(){

    }

    public Dimension(int width , int height){
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
