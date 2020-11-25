package utils;

public class Coordinates {

    private double X, Y;

    public Coordinates(double x, double y) {
        X = x;
        Y = y;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Coordinates){
            return ((Coordinates) obj).getX() == this.getX() && ((Coordinates) obj).getY() == this.getY();
        }
        return super.equals(obj);
    }
    public double getX() {
        return X;
    }
    public double getY() {
        return Y;
    }

    public void setX(double x) {
        X = x;
    }
    public void setY(double y) {
        Y = y;
    }

    @Override
    public String toString() {
        return "Coordinates: (" + "X=" + X + ", Y=" + Y + ")";
    }
}