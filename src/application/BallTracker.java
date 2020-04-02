package application;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;

public class BallTracker {
	
	private double _minH;
	
	private double _maxH;
	
	private double _minS;
	
	private double _maxS;
	
	private double _minV;
	
	private double _maxV;
	
	private Scalar _minValues;
	
	private Scalar _maxValues;
	
	private Mat _masked = new Mat();
	
	private Mat _morph = new Mat();
	
	private Ball ball;
	
	public BallTracker(double minH, double maxH, double minS, double maxS, double minV, double maxV, Mat hsvImg) {
		_minH = minH;
		_maxH = maxH;
		_minS = minS;
		_maxS = maxS;
		_minV = minV;
		_maxV = maxV;
		_minValues = new Scalar(_minH, _minS, _minV);
		_maxValues = new Scalar(_maxH, _maxS, _maxV);
		this.drawMask(hsvImg);
		this.dilateAndErode();
	}
	
	private void drawMask(Mat source) {
		Core.inRange(source, _minValues, _maxValues, _masked);
	}
	
	private void dilateAndErode() {
		Mat dilateElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(24, 24));
		Mat erodeElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(12, 12));
		
		Imgproc.erode(_masked, _morph, erodeElement);
		Imgproc.erode(_morph, _morph, erodeElement);
		
		Imgproc.dilate(_morph, _morph, dilateElement);
		Imgproc.dilate(_morph, _morph, dilateElement);
	}
	
	public Mat findAndDrawBall(Mat camFeed){
		List<MatOfPoint> contours = new ArrayList<>();
		Mat hierarchy = new Mat();
		// find contours
		Imgproc.findContours(_morph, contours, hierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_NONE);
		// if any contour exist...
		if (hierarchy.size().height > 0 && hierarchy.size().width > 0)
		{
			for (int idx = 0; idx >= 0; idx = (int) hierarchy.get(0, idx)[0])
			{
				Moments moment = Imgproc.moments(contours.get(idx));
				double area  = moment.m00;
				
				double x = Math.round(moment.m10 / area);
				double y = Math.round(moment.m01 / area);
				ball = new Ball(x,y);
				
				String desc = "" + x + "," + y;
				
				Imgproc.drawMarker(camFeed, new Point(x,y), new Scalar(255,255,255),Imgproc.MARKER_CROSS, 10,2);
				Imgproc.putText(camFeed, desc , new Point(x,y+30), 1, 1, new Scalar(255,0,0));
			}
		}
		
		return camFeed;
	}

	public Mat getMaskMat() {
		return _masked;
	}

	public Mat getMorphMat() {
		return _morph;
	}
	
	public Ball getBall() {
		return ball;
	}
	
	
}
