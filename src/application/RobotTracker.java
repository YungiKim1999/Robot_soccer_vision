package application;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;

public class RobotTracker {
	
	private double primMinH;
	
	private double primMaxH;
	
	private double primMinS;
	
	private double primMaxS;
	
	private double primMinV;
	
	private double primMaxV;
	
	private ArrayList<Double> secondMinH = new ArrayList<Double>();
	
	private ArrayList<Double> secondMaxH = new ArrayList<Double>();
	
	private ArrayList<Double> secondMinS = new ArrayList<Double>();
	
	private ArrayList<Double> secondMaxS = new ArrayList<Double>();
	
	private ArrayList<Double> secondMinV = new ArrayList<Double>();
	
	private ArrayList<Double> secondMaxV = new ArrayList<Double>();
	
	Scalar primMin;
	
	Scalar primMax;
	
	Scalar second1Min;
	
	Scalar second1Max;
	
	Scalar second2Min;
	
	Scalar second2Max;
	
	Scalar second3Min;
	
	Scalar second3Max;
	
	Scalar second4Min;
	
	Scalar second4Max;
	
	Scalar second5Min;
	
	Scalar second5Max;
	
	private Mat hsvImg = new Mat();
	
	private Mat _primMask = new Mat();
	private Mat _maskA = new Mat();
	private Mat _maskB = new Mat();
	private Mat _maskC = new Mat();
	private Mat _maskD = new Mat();
	private Mat _maskE = new Mat();
	
	private Mat _primMorph = new Mat();
	private Mat _morphA = new Mat();
	private Mat _morphB = new Mat();
	private Mat _morphC = new Mat();
	private Mat _morphD = new Mat();
	private Mat _morphE = new Mat();
	
	private ArrayList<Robot> robotTeam = new ArrayList<Robot>();
	
	public RobotTracker(double primHMin, double primHMax,double primSMin,double primSMax,double primVMin,double primVMax,ArrayList<Double> secondMinH,ArrayList<Double> secondMaxH,ArrayList<Double> secondMinS,ArrayList<Double> secondMaxS,ArrayList<Double> secondMinV,ArrayList<Double> secondMaxV, Mat hsvImg) {
		primMinH = primHMin;
		primMaxH = primHMax;
		primMinS = primSMin;
		primMaxS = primSMax;
		primMinV = primVMin;
		primMaxV = primVMax;
		this.secondMinH = secondMinH;
		this.secondMaxH =secondMaxH;
		this.secondMinS = secondMinS;
		this.secondMaxS = secondMaxS;
		this.secondMinV = secondMinV;
		this.secondMaxV = secondMaxV;
		
		this.hsvImg = hsvImg;
		
		this.getAllScalarValues();
		this.drawMaskforAll(hsvImg);
		this.dilateAndErode();
		
	}
	
	private void getAllScalarValues() {
		primMin = new Scalar(primMinH,primMinS, primMinV);
		primMax = new Scalar(primMaxH,primMaxS, primMaxV);
		second1Min = new Scalar(secondMinH.get(0), secondMinS.get(0), secondMinV.get(0));
		second1Max = new Scalar(secondMaxH.get(0), secondMaxS.get(0), secondMaxV.get(0));
		
		second2Min = new Scalar(secondMinH.get(1), secondMinS.get(1), secondMinV.get(1));
		second2Max = new Scalar(secondMaxH.get(1), secondMaxS.get(1), secondMaxV.get(1));
		
		second3Min = new Scalar(secondMinH.get(2), secondMinS.get(2), secondMinV.get(2));
		second3Max = new Scalar(secondMaxH.get(2), secondMaxS.get(2), secondMaxV.get(2));
		
		second4Min = new Scalar(secondMinH.get(3), secondMinS.get(3), secondMinV.get(3));
		second4Max = new Scalar(secondMaxH.get(3), secondMaxS.get(3), secondMaxV.get(3));
		
		second5Min = new Scalar(secondMinH.get(4), secondMinS.get(4), secondMinV.get(4));
		second5Max = new Scalar(secondMaxH.get(4), secondMaxS.get(4), secondMaxV.get(4));
	}
	
	private void drawMaskforAll(Mat hsvimg) {
		Core.inRange(hsvimg, primMin, primMax, _primMask);
		Core.inRange(hsvimg, second1Min, second1Max, _maskA);
		Core.inRange(hsvimg, second2Min, second2Max, _maskB);
		Core.inRange(hsvimg, second3Min, second3Max, _maskC);
		Core.inRange(hsvimg, second4Min, second4Max, _maskD);
		Core.inRange(hsvimg, second5Min, second5Max, _maskE);
	}
	private void dilateAndErode() {
		Mat dilateElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(24, 24));
		Mat erodeElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(12, 12));
		
		Imgproc.erode(_primMask, _primMorph, erodeElement);
		Imgproc.erode(_primMorph, _primMorph, erodeElement);
		
		Imgproc.dilate(_primMorph, _primMorph, dilateElement);
		Imgproc.dilate(_primMorph, _primMorph, dilateElement);
		
		Imgproc.erode(_maskA, _morphA, erodeElement);
		Imgproc.erode(_morphA, _morphA, erodeElement);
		
		Imgproc.dilate(_morphA, _morphA, dilateElement);
		Imgproc.dilate(_morphA, _morphA, dilateElement);
		
		Imgproc.erode(_maskB, _morphB, erodeElement);
		Imgproc.erode(_morphB, _morphB, erodeElement);
		
		Imgproc.dilate(_morphB, _morphB, dilateElement);
		Imgproc.dilate(_morphB, _morphB, dilateElement);
		
		Imgproc.erode(_maskC, _morphC, erodeElement);
		Imgproc.erode(_morphC, _morphC, erodeElement);
		
		Imgproc.dilate(_morphC, _morphC, dilateElement);
		Imgproc.dilate(_morphC, _morphC, dilateElement);
		
		Imgproc.erode(_maskD, _morphD, erodeElement);
		Imgproc.erode(_morphD, _morphD, erodeElement);
		
		Imgproc.dilate(_morphD, _morphD, dilateElement);
		Imgproc.dilate(_morphD, _morphD, dilateElement);
		
		Imgproc.erode(_maskE, _morphE, erodeElement);
		Imgproc.erode(_morphE, _morphE, erodeElement);
		
		Imgproc.dilate(_morphE, _morphE, dilateElement);
		Imgproc.dilate(_morphE, _morphE, dilateElement);
	}
	
	public Mat findAndDrawRobot(Mat camFeed){
		double xPrim,yPrim,xSecond,ySecond;
		ArrayList<Robot> teamOfRobots = new ArrayList<Robot>();
		
		ArrayList<Double> xPrimList = new ArrayList<Double>();
		ArrayList<Double> yPrimList = new ArrayList<Double>();
		
		ArrayList<Double> x1SecondaryList = new ArrayList<Double>();
		ArrayList<Double> y1SecondaryList = new ArrayList<Double>();
		
		ArrayList<Double> x2SecondaryList = new ArrayList<Double>();
		ArrayList<Double> y2SecondaryList = new ArrayList<Double>();
		
		ArrayList<Double> x3SecondaryList = new ArrayList<Double>();
		ArrayList<Double> y3SecondaryList = new ArrayList<Double>();
		
		ArrayList<Double> x4SecondaryList = new ArrayList<Double>();
		ArrayList<Double> y4SecondaryList = new ArrayList<Double>();
		
		ArrayList<Double> x5SecondaryList = new ArrayList<Double>();
		ArrayList<Double> y5SecondaryList = new ArrayList<Double>();
		
		List<MatOfPoint> primaryContours = new ArrayList<>();
		
		List<MatOfPoint> secondaryContours1 =  new ArrayList<>();
		List<MatOfPoint> secondaryContours2 =  new ArrayList<>();
		List<MatOfPoint> secondaryContours3 =  new ArrayList<>();
		List<MatOfPoint> secondaryContours4 =  new ArrayList<>();
		List<MatOfPoint> secondaryContours5 =  new ArrayList<>();
		MatOfPoint2f approx = new MatOfPoint2f();
		
		Mat primaryHeirarchy = new Mat();
		Mat secondaryHeirarchy1 = new Mat();
		Mat secondaryHeirarchy2 = new Mat();
		Mat secondaryHeirarchy3 = new Mat();
		Mat secondaryHeirarchy4 = new Mat();
		Mat secondaryHeirarchy5 = new Mat();
		
		Imgproc.findContours(_primMorph, primaryContours, primaryHeirarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_NONE);
		Imgproc.findContours(_morphA, secondaryContours1, secondaryHeirarchy1, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_NONE);
		Imgproc.findContours(_morphB, secondaryContours2, secondaryHeirarchy2, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_NONE);
		Imgproc.findContours(_morphC, secondaryContours3, secondaryHeirarchy3, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_NONE);
		Imgproc.findContours(_morphD, secondaryContours4, secondaryHeirarchy4, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_NONE);
		Imgproc.findContours(_morphE, secondaryContours5, secondaryHeirarchy5, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_NONE);
		
		
		//primary find object
		if(primaryHeirarchy.size().height > 0 && primaryHeirarchy.size().width > 0) {
				for(int idx = 0; idx >= 0; idx = (int) primaryHeirarchy.get(0, idx)[0]) {
					MatOfPoint2f contour2f = new MatOfPoint2f(primaryContours.get(idx).toArray());
					double approxDistance = Imgproc.arcLength(contour2f, true) * 0.05;
					
					Imgproc.approxPolyDP(contour2f, approx, approxDistance, true);
					MatOfPoint points = new MatOfPoint(approx.toArray());
					
					
					//specify that it is a hexagon
					if(points.total() >0) {
					Moments moment = Imgproc.moments(primaryContours.get(idx));
					double area  = moment.m00;
					
					xPrim = Math.round(moment.m10 / area);
					yPrim = Math.round(moment.m01 / area);
					
					xPrimList.add(xPrim);
					yPrimList.add(yPrim);
					}
				}
			}
		
		//first secondary
		if(secondaryHeirarchy1.size().height > 0 && secondaryHeirarchy1.size().width > 0) {
			for(int idx = 0; idx >= 0; idx = (int) secondaryHeirarchy1.get(0, idx)[0]) {
				MatOfPoint2f contour2f = new MatOfPoint2f(secondaryContours1.get(idx).toArray());
				double approxDistance = Imgproc.arcLength(contour2f, true) * 0.05;
				
				Imgproc.approxPolyDP(contour2f, approx, approxDistance, true);
				MatOfPoint points = new MatOfPoint(approx.toArray());
				
				//specify that it is a triangle
				if(points.total() >0) {
				Moments moment = Imgproc.moments(secondaryContours1.get(idx));
				double area  = moment.m00;
				
				xSecond = Math.round(moment.m10 / area);
				ySecond = Math.round(moment.m01 / area);
				
				x1SecondaryList.add(xSecond);
				y1SecondaryList.add(ySecond);
				}
			}
		}
		
		// second secondary
		if(secondaryHeirarchy2.size().height > 0 && secondaryHeirarchy2.size().width > 0) {
			for(int idx = 0; idx >= 0; idx = (int) secondaryHeirarchy2.get(0, idx)[0]) {
				MatOfPoint2f contour2f = new MatOfPoint2f(secondaryContours2.get(idx).toArray());
				double approxDistance = Imgproc.arcLength(contour2f, true) * 0.05;
				
				Imgproc.approxPolyDP(contour2f, approx, approxDistance, true);
				MatOfPoint points = new MatOfPoint(approx.toArray());
				
				//specify that it is a triangle
				if(points.total() >0) {
				Moments moment = Imgproc.moments(secondaryContours2.get(idx));
				double area  = moment.m00;
				
				xSecond = Math.round(moment.m10 / area);
				ySecond = Math.round(moment.m01 / area);
				
				x2SecondaryList.add(xSecond);
				y2SecondaryList.add(ySecond);
				}
			}
		}
		
		
		if(secondaryHeirarchy3.size().height > 0 && secondaryHeirarchy3.size().width > 0) {
			for(int idx = 0; idx >= 0; idx = (int) secondaryHeirarchy3.get(0, idx)[0]) {
				MatOfPoint2f contour2f = new MatOfPoint2f(secondaryContours3.get(idx).toArray());
				double approxDistance = Imgproc.arcLength(contour2f, true) * 0.05;
				
				Imgproc.approxPolyDP(contour2f, approx, approxDistance, true);
				MatOfPoint points = new MatOfPoint(approx.toArray());
				
				//specify that it is a triangle
				if(points.total() >0) {
				Moments moment = Imgproc.moments(secondaryContours3.get(idx));
				double area  = moment.m00;
				
				xSecond = Math.round(moment.m10 / area);
				ySecond = Math.round(moment.m01 / area);
				
				x3SecondaryList.add(xSecond);
				y3SecondaryList.add(ySecond);
				}
			}
		}
		

		if(secondaryHeirarchy4.size().height > 0 && secondaryHeirarchy4.size().width > 0) {
			for(int idx = 0; idx >= 0; idx = (int) secondaryHeirarchy4.get(0, idx)[0]) {
				MatOfPoint2f contour2f = new MatOfPoint2f(secondaryContours4.get(idx).toArray());
				double approxDistance = Imgproc.arcLength(contour2f, true) * 0.05;
				
				Imgproc.approxPolyDP(contour2f, approx, approxDistance, true);
				MatOfPoint points = new MatOfPoint(approx.toArray());
				
				//specify that it is a triangle
				if(points.total() >0) {
				Moments moment = Imgproc.moments(secondaryContours4.get(idx));
				double area  = moment.m00;
				
				xSecond = Math.round(moment.m10 / area);
				ySecond = Math.round(moment.m01 / area);
				
				x4SecondaryList.add(xSecond);
				y4SecondaryList.add(ySecond);
				}
			}
		}
		

		if(secondaryHeirarchy5.size().height > 0 && secondaryHeirarchy5.size().width > 0) {
			for(int idx = 0; idx >= 0; idx = (int) secondaryHeirarchy5.get(0, idx)[0]) {
				MatOfPoint2f contour2f = new MatOfPoint2f(secondaryContours5.get(idx).toArray());
				double approxDistance = Imgproc.arcLength(contour2f, true) * 0.05;
				
				Imgproc.approxPolyDP(contour2f, approx, approxDistance, true);
				MatOfPoint points = new MatOfPoint(approx.toArray());
				
				//specify that it is a triangle
				if(points.total() >0) {
				Moments moment = Imgproc.moments(secondaryContours5.get(idx));
				double area  = moment.m00;
				
				xSecond = Math.round(moment.m10 / area);
				ySecond = Math.round(moment.m01 / area);
				
				x5SecondaryList.add(xSecond);
				y5SecondaryList.add(ySecond);
				}
			}
		}
		
		
		
		
		
		//working
		//duplicate for all 5 robots and see if working
		if(x1SecondaryList.size() > 0 && xPrimList.size() > 0) {
			double lowest = 800;
			int primKeeper = 0;
			int secondKeeper = 0;
			for(int i = 0; i < x1SecondaryList.size() ; i++) {
				for(int j = 0; j < xPrimList.size() ; j++) {
					if(lowest > Math.sqrt(Math.pow(xPrimList.get(j) - x1SecondaryList.get(i), 2) +  Math.pow(yPrimList.get(j) - y1SecondaryList.get(i), 2))) {
						lowest = Math.sqrt(Math.pow(xPrimList.get(j) - x1SecondaryList.get(i), 2) +  Math.pow(yPrimList.get(j) - y1SecondaryList.get(i), 2));
						primKeeper = j;
						secondKeeper = i;
					}
				}
			}
			teamOfRobots.add(new Robot(xPrimList.get(primKeeper), yPrimList.get(primKeeper), x1SecondaryList.get(secondKeeper), y1SecondaryList.get(secondKeeper)));
		}
		
		if(x2SecondaryList.size() > 0 && xPrimList.size() > 0) {
			double lowest = 800;
			int primKeeper = 0;
			int secondKeeper = 0;
			for(int i = 0; i < x2SecondaryList.size() ; i++) {
				for(int j = 0; j < xPrimList.size() ; j++) {
					if(lowest > Math.sqrt(Math.pow(xPrimList.get(j) - x2SecondaryList.get(i), 2) +  Math.pow(yPrimList.get(j) - y2SecondaryList.get(i), 2))) {
						lowest = Math.sqrt(Math.pow(xPrimList.get(j) - x2SecondaryList.get(i), 2) +  Math.pow(yPrimList.get(j) - y2SecondaryList.get(i), 2));
						primKeeper = j;
						secondKeeper = i;
					}
				}
			}
			teamOfRobots.add(new Robot(xPrimList.get(primKeeper), yPrimList.get(primKeeper), x2SecondaryList.get(secondKeeper), y2SecondaryList.get(secondKeeper)));
		}
		
		
		if(x3SecondaryList.size() > 0 && xPrimList.size() > 0) {
			double lowest = 800;
			int primKeeper = 0;
			int secondKeeper = 0;
			for(int i = 0; i < x3SecondaryList.size() ; i++) {
				for(int j = 0; j < xPrimList.size() ; j++) {
					if(lowest > Math.sqrt(Math.pow(xPrimList.get(j) - x3SecondaryList.get(i), 2) +  Math.pow(yPrimList.get(j) - y3SecondaryList.get(i), 2))) {
						lowest = Math.sqrt(Math.pow(xPrimList.get(j) - x3SecondaryList.get(i), 2) +  Math.pow(yPrimList.get(j) - y3SecondaryList.get(i), 2));
						primKeeper = j;
						secondKeeper = i;
					}
				}
			}
			teamOfRobots.add(new Robot(xPrimList.get(primKeeper), yPrimList.get(primKeeper), x3SecondaryList.get(secondKeeper), y3SecondaryList.get(secondKeeper)));
		}
		if(x4SecondaryList.size() > 0 && xPrimList.size() > 0) {
			double lowest = 800;
			int primKeeper = 0;
			int secondKeeper = 0;
			for(int i = 0; i < x4SecondaryList.size() ; i++) {
				for(int j = 0; j < xPrimList.size() ; j++) {
					if(lowest > Math.sqrt(Math.pow(xPrimList.get(j) - x4SecondaryList.get(i), 2) +  Math.pow(yPrimList.get(j) - y4SecondaryList.get(i), 2))) {
						lowest = Math.sqrt(Math.pow(xPrimList.get(j) - x4SecondaryList.get(i), 2) +  Math.pow(yPrimList.get(j) - y4SecondaryList.get(i), 2));
						primKeeper = j;
						secondKeeper = i;
					}
				}
			}
			teamOfRobots.add(new Robot(xPrimList.get(primKeeper), yPrimList.get(primKeeper), x4SecondaryList.get(secondKeeper), y4SecondaryList.get(secondKeeper)));
		}
		if(x5SecondaryList.size() > 0 && xPrimList.size() > 0) {
			double lowest = 800;
			int primKeeper = 0;
			int secondKeeper = 0;
			for(int i = 0; i < x5SecondaryList.size() ; i++) {
				for(int j = 0; j < xPrimList.size() ; j++) {
					if(lowest > Math.sqrt(Math.pow(xPrimList.get(j) - x5SecondaryList.get(i), 2) +  Math.pow(yPrimList.get(j) - y5SecondaryList.get(i), 2))) {
						lowest = Math.sqrt(Math.pow(xPrimList.get(j) - x5SecondaryList.get(i), 2) +  Math.pow(yPrimList.get(j) - y5SecondaryList.get(i), 2));
						primKeeper = j;
						secondKeeper = i;
					}
				}
			}
			teamOfRobots.add(new Robot(xPrimList.get(primKeeper), yPrimList.get(primKeeper), x5SecondaryList.get(secondKeeper), y5SecondaryList.get(secondKeeper)));
		}
		
		
			for(int i = 0; i < teamOfRobots.size(); i++) {
				Imgproc.drawMarker(camFeed, new Point(teamOfRobots.get(i).getXPrim(),teamOfRobots.get(i).getYPrim()), new Scalar(255,0,255), Imgproc.MARKER_CROSS,10,2);
				Imgproc.drawMarker(camFeed, new Point(teamOfRobots.get(i).getXSecondary(),teamOfRobots.get(i).getYSecondary()), new Scalar(0,255,255), Imgproc.MARKER_CROSS,10,2);
				Imgproc.arrowedLine(camFeed, new Point(teamOfRobots.get(i).getXPrim(),teamOfRobots.get(i).getYPrim()), new Point(teamOfRobots.get(i).getXSecondary(),teamOfRobots.get(i).getYSecondary()), new Scalar(255,255,0));
				//System.out.println(teamOfRobots.get(i).getAngle());
			}
		
		return camFeed;
	}

	public Mat get_maskA() {
		return _maskA;
	}

	public Mat get_maskB() {
		return _maskB;
	}

	public Mat get_primMask() {
		return _primMask;
	}

	public Mat get_maskC() {
		return _maskC;
	}

	public Mat get_maskD() {
		return _maskD;
	}

	public Mat get_maskE() {
		return _maskE;
	}

	public Mat get_primMorph() {
		return _primMorph;
	}

	public Mat get_morphA() {
		return _morphA;
	}

	public Mat get_morphB() {
		return _morphB;
	}

	public Mat get_morphC() {
		return _morphC;
	}

	public Mat get_morphD() {
		return _morphD;
	}

	public Mat get_morphE() {
		return _morphE;
	}
	
	
	

}
