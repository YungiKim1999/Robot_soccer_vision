package application;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;
import org.opencv.videoio.VideoCapture;

import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class TrackingController {
	// FXML camera button
		@FXML
		private Button cameraButton;
		// the FXML area for showing the current frame
		@FXML
		private ImageView originalFrame;
		
		// the FXML area for showing the mask
		@FXML
		private ImageView ballMaskImage;
		// the FXML area for showing the output of the morphological operations
		@FXML
		private ImageView ballMorphImage;
		// FXML slider for setting HSV ranges
		@FXML
		private Slider ballHueStart;
		@FXML
		private Slider ballHueStop;
		@FXML
		private Slider ballSaturationStart;
		@FXML
		private Slider ballSaturationStop;
		@FXML
		private Slider ballValueStart;
		@FXML
		private Slider ballValueStop;
		
		@FXML
		private ImageView robotPrimaryMaskImage;
		
		@FXML
		private ImageView robotPrimaryMorphImage;
		
		@FXML
		private ImageView robot1SecondaryMaskImage;
		
		@FXML
		private ImageView robot1SecondaryMorphImage;
		
		@FXML
		private ImageView robot2SecondaryMaskImage;
		
		@FXML
		private ImageView robot2SecondaryMorphImage;
		
		@FXML
		private ImageView robot3SecondaryMorphImage;
		
		@FXML
		private ImageView robot3SecondaryMaskImage;
		
		@FXML
		private ImageView robot4SecondaryMorphImage;
		
		@FXML
		private ImageView robot4SecondaryMaskImage;
		
		@FXML
		private ImageView robot5SecondaryMorphImage;
		
		@FXML
		private ImageView robot5SecondaryMaskImage;
		// the FXML area for showing the output of the morphological operations
		
		// FXML slider for setting HSV ranges
		@FXML
		private Slider robotPrimaryHueStart;
		@FXML
		private Slider robotPrimaryHueStop;
		@FXML
		private Slider robotPrimarySaturationStart;
		@FXML
		private Slider robotPrimarySaturationStop;
		@FXML
		private Slider robotPrimaryValueStart;
		@FXML
		private Slider robotPrimaryValueStop;
		
		@FXML
		private Slider robot1SecondaryHueStart;
		@FXML
		private Slider robot1SecondaryHueStop;
		@FXML
		private Slider robot1SecondarySaturationStart;
		@FXML
		private Slider robot1SecondarySaturationStop;
		@FXML
		private Slider robot1SecondaryValueStart;
		@FXML
		private Slider robot1SecondaryValueStop;
		
		@FXML
		private Slider robot2SecondaryHueStart;
		@FXML
		private Slider robot2SecondaryHueStop;
		@FXML
		private Slider robot2SecondarySaturationStart;
		@FXML
		private Slider robot2SecondarySaturationStop;
		@FXML
		private Slider robot2SecondaryValueStart;
		@FXML
		private Slider robot2SecondaryValueStop;
		
		@FXML
		private Slider robot3SecondaryHueStart;
		@FXML
		private Slider robot3SecondaryHueStop;
		@FXML
		private Slider robot3SecondarySaturationStart;
		@FXML
		private Slider robot3SecondarySaturationStop;
		@FXML
		private Slider robot3SecondaryValueStart;
		@FXML
		private Slider robot3SecondaryValueStop;
		
		@FXML
		private Slider robot4SecondaryHueStart;
		@FXML
		private Slider robot4SecondaryHueStop;
		@FXML
		private Slider robot4SecondarySaturationStart;
		@FXML
		private Slider robot4SecondarySaturationStop;
		@FXML
		private Slider robot4SecondaryValueStart;
		@FXML
		private Slider robot4SecondaryValueStop;
		
		@FXML
		private Slider robot5SecondaryHueStart;
		@FXML
		private Slider robot5SecondaryHueStop;
		@FXML
		private Slider robot5SecondarySaturationStart;
		@FXML
		private Slider robot5SecondarySaturationStop;
		@FXML
		private Slider robot5SecondaryValueStart;
		@FXML
		private Slider robot5SecondaryValueStop;
		@FXML
		private ChoiceBox<String> dotSelector;
		@FXML
		private ImageView calibrationView;
		
		// a timer for acquiring the video stream
		private ScheduledExecutorService timer;
		// the OpenCV object that performs the video capture
		private VideoCapture capture = new VideoCapture();
		// a flag to change the button behavior
		private boolean cameraActive;
		
		Point redPoint = new Point(1,1);
		Point yellowPoint = new Point(639,1);
		Point greenPoint = new Point(1,419);
		Point bluePoint = new Point(639,419);
		
		// property for object binding
		
			
		/**
		 * The action triggered by pushing the button on the GUI
		 */
		@FXML
		private void startCamera(){
			// set a fixed width for all the image to show and preserve image ratio
			// the ratio is always set to 640*480 (affects coordinate system)
			this.imageViewProperties(this.originalFrame, 600);
	
			this.dotSelector.setItems(FXCollections.observableArrayList("Red Dot", "Green Dot", "Blue Dot", "Yellow Dot"));
			
			//THIS FUNCTION NEEDS MORE WORK!!!
			//does not put coordinates exactly on the place clicked
			// need to calibrate between the event handler and the opencv matrix.
			calibrationView.setOnMouseClicked(new EventHandler<MouseEvent>() {
		        @Override
		        public void handle(MouseEvent me) {
		            //System.out.println("Clicked, x:" + (me.getSceneX()) + " y:" + (me.getSceneY()));
		            
		        	if(dotSelector.getValue().equals("Red Dot")) {
		        		redPoint = new Point(me.getX(),me.getY());
		        	}else if(dotSelector.getValue().equals("Green Dot")) {
		        		greenPoint = new Point(me.getX(),me.getY());
		        	}else if(dotSelector.getValue().equals("Blue Dot")) {
		        		bluePoint = new Point(me.getX(),me.getY());
		        	}else {
		        		yellowPoint = new Point(me.getX(),me.getY());
		        	}
		        	
		            //the event will be passed only to the circle which is on front
		            me.consume();
		        }
		    });
			if (!this.cameraActive){
				// start the video capture
				this.capture.open(0);
				
				// is the video stream available?
				if (this.capture.isOpened()){
					this.cameraActive = true;
					
					// grab a frame every 33 ms (30 frames/sec)
					Runnable frameGrabber = new Runnable() {
						
						@Override
						public void run(){
							
							Point[] calibArray = {redPoint,greenPoint,bluePoint,yellowPoint};
					        
							for (int i = 0; i < calibArray.length-1; i++){
					            for (int j = 0; j < calibArray.length-i-1; j++) 
					                if (calibArray[j].y > calibArray[j+1].y){ 
					                    Point temp = calibArray[j]; 
					                    calibArray[j] = calibArray[j+1]; 
					                    calibArray[j+1] = temp; 
					                } 
					        }
					        
					        if(calibArray[0].x > calibArray[1].x) {
					        	Point temp = calibArray[0]; 
			                    calibArray[0] = calibArray[1]; 
			                    calibArray[1] = temp; 
					        }
					        
					        if(calibArray[2].x > calibArray[3].x) {
					        	Point temp = calibArray[2]; 
			                    calibArray[2] = calibArray[3]; 
			                    calibArray[3] = temp; 
					        }
							
							Point [] sizeOfCalib = {new Point(0,0),new Point(640,0),new Point(0,480),new Point(640,480)};
						
							MatOfPoint2f calibMat = new MatOfPoint2f(calibArray);
							MatOfPoint2f sizeMat = new MatOfPoint2f(sizeOfCalib);
							
							// effectively grab and process a single frame
							Mat frame = grabFrame();
							Imgproc.drawMarker(frame, redPoint,new Scalar(0,0,255), Imgproc.MARKER_CROSS,3,2);
							Imgproc.drawMarker(frame, yellowPoint,new Scalar(0,255,255), Imgproc.MARKER_CROSS,3,2);
							Imgproc.drawMarker(frame, greenPoint,new Scalar(0,255,0), Imgproc.MARKER_CROSS,3,2);
							Imgproc.drawMarker(frame, bluePoint,new Scalar(255,0,0), Imgproc.MARKER_CROSS,3,2);
							
							// convert and show the frame
							Image imageToShow = Utils.mat2Image(frame);
							
							Mat warpMat = Imgproc.getPerspectiveTransform(calibMat, sizeMat);
							Mat destImg = new Mat();
							Imgproc.warpPerspective(frame,destImg, warpMat, frame.size());
							Mat trackedDestImg = addTracking(destImg);
							Image warpedImage = Utils.mat2Image(trackedDestImg);
							
							updateImageView(originalFrame, warpedImage);
							updateImageView(calibrationView, imageToShow);
							
						}
					};
					
					this.timer = Executors.newSingleThreadScheduledExecutor();
					this.timer.scheduleAtFixedRate(frameGrabber, 0, 33, TimeUnit.MILLISECONDS);
					
					// update the button content
					this.cameraButton.setText("Stop Camera");
				}
				else{
					// log the error
					System.err.println("Failed to open the camera connection...");
				}
			}
			else{
				// the camera is not active at this point
				this.cameraActive = false;
				// update again the button content
				this.cameraButton.setText("Start Camera");
				
				// stop the timer
				this.stopAcquisition();
			}
		}
		
		//Issue:
		//grabframe should only "grab frame" if we want to warp perspective as the tracking are being done on the non-warped image
		// tracking should be done on the after image/ warped image
		
		private Mat grabFrame(){
			Mat frame = new Mat();
			
			// check if the capture is open
			if (this.capture.isOpened()){
				try{
					// read the current frame
					this.capture.read(frame);
						// if the frame is not empty, process it
					
				}
				catch (Exception e){
					// log the (full) error
					System.err.print("Exception during the image elaboration...");
					e.printStackTrace();
				}
			}
			return frame;
		}
		
		private Mat addTracking(Mat originalImage) {
			Mat frame = originalImage;
			Mat blurredImage = new Mat();
			Mat hsvImage = new Mat();
			
			
			// remove some noise
			Imgproc.blur(originalImage, blurredImage, new Size(7, 7));
			
			// convert the frame to HSV
			Imgproc.cvtColor(blurredImage, hsvImage, Imgproc.COLOR_BGR2HSV);
			
			// get thresholding values from the UI
			// remember: H ranges 0-180, S and V range 0-255
			
			BallTracker ballTracker = new BallTracker(this.ballHueStart.getValue(),this.ballHueStop.getValue(),this.ballSaturationStart.getValue(),this.ballSaturationStop.getValue(),this.ballValueStart.getValue(), this.ballValueStop.getValue(), hsvImage );
			
			ArrayList<Double> secondMinH = new ArrayList<Double>();
			ArrayList<Double> secondMaxH = new ArrayList<Double>();
			ArrayList<Double> secondMinS = new ArrayList<Double>();
			ArrayList<Double> secondMaxS = new ArrayList<Double>();
			ArrayList<Double> secondMinV = new ArrayList<Double>();
			ArrayList<Double> secondMaxV = new ArrayList<Double>();
			

			secondMinH.add(robot1SecondaryHueStart.getValue());
			secondMaxH.add(robot1SecondaryHueStop.getValue());
			secondMinS.add(robot1SecondarySaturationStart.getValue());
			secondMaxS.add(robot1SecondarySaturationStop.getValue());
			secondMinV.add(robot1SecondaryValueStart.getValue());
			secondMaxV.add(robot1SecondaryValueStop.getValue());
			
			secondMinH.add(robot2SecondaryHueStart.getValue());
			secondMaxH.add(robot2SecondaryHueStop.getValue());
			secondMinS.add(robot2SecondarySaturationStart.getValue());
			secondMaxS.add(robot2SecondarySaturationStop.getValue());
			secondMinV.add(robot2SecondaryValueStart.getValue());
			secondMaxV.add(robot2SecondaryValueStop.getValue());
			
			secondMinH.add(robot3SecondaryHueStart.getValue());
			secondMaxH.add(robot3SecondaryHueStop.getValue());
			secondMinS.add(robot3SecondarySaturationStart.getValue());
			secondMaxS.add(robot3SecondarySaturationStop.getValue());
			secondMinV.add(robot3SecondaryValueStart.getValue());
			secondMaxV.add(robot3SecondaryValueStop.getValue());
			
			secondMinH.add(robot4SecondaryHueStart.getValue());
			secondMaxH.add(robot4SecondaryHueStop.getValue());
			secondMinS.add(robot4SecondarySaturationStart.getValue());
			secondMaxS.add(robot4SecondarySaturationStop.getValue());
			secondMinV.add(robot4SecondaryValueStart.getValue());
			secondMaxV.add(robot4SecondaryValueStop.getValue());
			
			secondMinH.add(robot5SecondaryHueStart.getValue());
			secondMaxH.add(robot5SecondaryHueStop.getValue());
			secondMinS.add(robot5SecondarySaturationStart.getValue());
			secondMaxS.add(robot5SecondarySaturationStop.getValue());
			secondMinV.add(robot5SecondaryValueStart.getValue());
			secondMaxV.add(robot5SecondaryValueStop.getValue());
			
			RobotTracker robotTracker = new RobotTracker(this.robotPrimaryHueStart.getValue(),this.robotPrimaryHueStop.getValue(),this.robotPrimarySaturationStart.getValue(),this.robotPrimarySaturationStop.getValue(),this.robotPrimaryValueStart.getValue(), this.robotPrimaryValueStop.getValue(),secondMinH, secondMaxH, secondMinS , secondMaxS , secondMinV , secondMaxV, hsvImage);
			
		
			// show the partial output
			this.updateImageView(this.ballMaskImage, Utils.mat2Image(ballTracker.getMaskMat()));						
			this.updateImageView(this.ballMorphImage, Utils.mat2Image(ballTracker.getMorphMat()));
			
			this.updateImageView(this.robotPrimaryMaskImage, Utils.mat2Image(robotTracker.get_primMask()));
			this.updateImageView(this.robotPrimaryMorphImage, Utils.mat2Image(robotTracker.get_primMorph()));
			
			this.updateImageView(this.robot1SecondaryMaskImage, Utils.mat2Image(robotTracker.get_maskA()));
			this.updateImageView(this.robot1SecondaryMorphImage, Utils.mat2Image(robotTracker.get_morphA()));
			
			this.updateImageView(this.robot2SecondaryMaskImage, Utils.mat2Image(robotTracker.get_maskB()));
			this.updateImageView(this.robot2SecondaryMorphImage, Utils.mat2Image(robotTracker.get_morphB()));
			
			this.updateImageView(this.robot3SecondaryMaskImage, Utils.mat2Image(robotTracker.get_maskC()));
			this.updateImageView(this.robot3SecondaryMorphImage, Utils.mat2Image(robotTracker.get_morphC()));

			this.updateImageView(this.robot4SecondaryMaskImage, Utils.mat2Image(robotTracker.get_maskD()));
			this.updateImageView(this.robot4SecondaryMorphImage, Utils.mat2Image(robotTracker.get_morphD()));
			
			this.updateImageView(this.robot5SecondaryMaskImage, Utils.mat2Image(robotTracker.get_maskE()));
			this.updateImageView(this.robot5SecondaryMorphImage, Utils.mat2Image(robotTracker.get_morphE()));					
			frame = ballTracker.findAndDrawBall(frame);
			frame = robotTracker.findAndDrawRobot(frame);
			return frame;
		}
		

		//this function can be molded to customize height as well
		void imageViewProperties(ImageView image, int dimension){
			// set a fixed width for the given ImageView
			//image.setFitWidth(dimension);
			// preserve the image ratio
			image.setPreserveRatio(true);
		}
		
		/**
		 * Stop the acquisition from the camera and release all the resources
		 */
		private void stopAcquisition(){
			if (this.timer!=null && !this.timer.isShutdown()){
				try{
					// stop the timer
					this.timer.shutdown();
					this.timer.awaitTermination(33, TimeUnit.MILLISECONDS);
				}
				catch (InterruptedException e){
					// log any exception
					System.err.println("Exception in stopping the frame capture, trying to release the camera now... " + e);
				}
			}
			
			if (this.capture.isOpened()){
				// release the camera
				this.capture.release();
			}
		}
		
		private void updateImageView(ImageView view, Image image){
			Utils.onFXThread(view.imageProperty(), image);
		}
		
		/**
		 * On application close, stop the acquisition from the camera
		 */
		protected void setClosed(){
			this.stopAcquisition();
		}

}
