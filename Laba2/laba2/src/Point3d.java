public class Point3d extends Point2d {
    private double zCoord;
    public Point3d(double x, double y, double z) {
        super(x, y);
        zCoord = z;
    }
    public Point3d() {
        this(0, 0, 0);
    }
    public boolean Comp(double x, double y, double z) {
        if (getX() == x && getY() == y && zCoord == z) return false;
        else return true;
    }
    public double getZ() {
        return zCoord;
    }
}
