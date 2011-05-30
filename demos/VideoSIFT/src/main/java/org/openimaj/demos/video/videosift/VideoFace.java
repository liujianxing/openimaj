package org.openimaj.demos.video.videosift;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

import org.openimaj.feature.local.keypoints.face.FacialDescriptor;
import org.openimaj.feature.local.keypoints.face.FacialDescriptor.FacialPartDescriptor;
import org.openimaj.feature.local.list.LocalFeatureList;
import org.openimaj.feature.local.matcher.MatchingUtilities;
import org.openimaj.feature.local.matcher.consistent.ConsistentKeypointMatcher;
import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.FImage;
import org.openimaj.image.MBFImage;
import org.openimaj.image.colour.RGBColour;
import org.openimaj.image.colour.Transforms;
import org.openimaj.image.feature.local.engine.DoGSIFTEngine;
import org.openimaj.image.feature.local.keypoints.Keypoint;
import org.openimaj.image.processing.face.parts.FacePipeline;
import org.openimaj.image.processing.resize.ResizeProcessor;
import org.openimaj.math.geometry.point.Point2d;
import org.openimaj.math.geometry.shape.Polygon;
import org.openimaj.math.geometry.shape.Shape;
import org.openimaj.math.geometry.transforms.HomographyModel;
import org.openimaj.math.geometry.transforms.MatrixTransformProvider;
import org.openimaj.math.geometry.transforms.TransformUtilities;
import org.openimaj.math.model.fit.RANSAC;
import org.openimaj.video.VideoDisplay;
import org.openimaj.video.VideoDisplayListener;
import org.openimaj.video.capture.VideoCapture;

public class VideoFace implements KeyListener, VideoDisplayListener<MBFImage> {
	private VideoCapture capture;
	private VideoDisplay<MBFImage> videoFrame;
	private JFrame modelFrame;
	private JFrame matchFrame;
	private MBFImage modelImage;

	private ConsistentKeypointMatcher<Keypoint> matcher;
	private FacePipeline engine;
	private PolygonDrawingListener polygonListener;
	private float rescale;

	public VideoFace() throws Exception {
		capture = new VideoCapture(320, 240);
		engine = new FacePipeline();
		polygonListener = new PolygonDrawingListener();
		videoFrame = VideoDisplay.createVideoDisplay(capture);
		videoFrame.getScreen().addKeyListener(this);
		videoFrame.getScreen().getContentPane().addMouseListener(polygonListener);
		videoFrame.getScreen().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		videoFrame.addVideoListener(this);
		this.rescale = 1.0f;
		
	}

	@Override
	public void keyPressed(KeyEvent key) {

	}

	@Override
	public void keyReleased(KeyEvent arg0) { }

	@Override
	public void keyTyped(KeyEvent arg0) { }

	public static void main(String [] args) throws Exception {		
		new VideoFace();
	}

	@Override
	public void afterUpdate(VideoDisplay<MBFImage> display) {

	}

	@Override
	public void beforeUpdate(MBFImage frame) {
		MBFImage resized = frame.process(new ResizeProcessor(1/rescale));
		LocalFeatureList<FacialDescriptor> faces = engine.extractFaces(Transforms.calculateIntensityNTSC(resized));
		for(FacialDescriptor face : faces){
			Shape transBounds = face.bounds.transform(TransformUtilities.scaleMatrix(rescale, rescale));
			frame.drawPolygon(transBounds.asPolygon(), RGBColour.RED);
			for(FacialPartDescriptor part: face.faceParts){
				frame.drawPoint(part.position, RGBColour.GREEN, 3);
			}
			
		}
		
	}
}
