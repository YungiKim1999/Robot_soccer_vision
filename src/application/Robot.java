package application;

public class Robot {
	
	private double xPrim;
	private double yPrim;
	private double xSecondary;
	private double ySecondary;
	private double angle;
	
	public Robot(double x1, double y1, double x2, double y2){
		xPrim = x1;
		yPrim = y1;
		xSecondary = x2;
		ySecondary = y2;
		angle = (Math.atan2( ySecondary - yPrim, xSecondary - xPrim)) * 180 / Math.PI;
	}

	public double getXPrim() {
		return xPrim;
	}

	public double getYPrim() {
		return yPrim;
	}

	public double getXSecondary() {
		return xSecondary;
	}

	public double getYSecondary() {
		return ySecondary;
	}

	public double getAngle() {
		return angle;
	}
	

}
