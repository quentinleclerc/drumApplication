package shapes;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import control_view.ListeningController;
import javafx.animation.PathTransition;
import javafx.application.Platform;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import midi.CerclesRepresentation;
import midi.Event;
import midi.MidiFileToSong;
import midi.SoundRecord;
import player.PlayerSong;

public class ThreadCircle implements Runnable {

	private final ListeningController controller;

	private Map<Integer, Double> kickDistance;
	private Map<Integer, Circle> liaisonToms;
	private Map<Circle, PathTransition> liaisonCirclesToPath = new HashMap<>();
	private Map<Ellipse, PathTransition> liaisonEllipsesToPath = new HashMap<>();

	private Ellipse pedale;

	private HandlerCircle hc;

	private CerclesRepresentation cercleRepresentation;

	private SoundRecord song;

	// Default Constructor
	public ThreadCircle(SoundRecord song, ListeningController controller, Map<Integer, Double> map, Map<Integer, Circle> map2, Ellipse ellipse) {
		this.kickDistance = map;
		this.liaisonToms = map2;
		this.pedale = ellipse;
		this.controller = controller;
		this.song = song;
		hc = new HandlerCircle();
		initializeCirclesRep();
	}

	private void initializeCirclesRep(){
		this.cercleRepresentation = new CerclesRepresentation(this.song, this.kickDistance);
		System.out.println(cercleRepresentation);

	}

	public void removeCircle(Circle sh){
		this.liaisonCirclesToPath.get(sh).setOnFinished(event -> Platform.runLater(() -> controller.removeShape(sh)));
	}

	public void removeEllipse(Ellipse sh){
		this.liaisonEllipsesToPath.get(sh).setOnFinished(event -> Platform.runLater(() -> controller.removeShape(sh)));
	}
	
	private void moveToTom(int note){
		if(note == 35){
			Ellipse pedaleTemp = hc.makeEllipse(pedale);
			Platform.runLater(() -> controller.addShape(pedaleTemp));
			liaisonEllipsesToPath.put(pedaleTemp, hc.moveEllipse(pedaleTemp, pedale));
		}
		else{
			Circle target = liaisonToms.get(note);
			Circle circleTemp = hc.makeCircle(target);
			Platform.runLater(() -> controller.addShape(circleTemp));
			liaisonCirclesToPath.put(circleTemp, hc.moveCircle(circleTemp, target));
		}
	}

	public void run() {
		int i;

		for (i = 0; i < cercleRepresentation.size()-1; i++) {
			Event event = cercleRepresentation.get(i);
			double timestamp = event.getTemps();
			int note = event.getNote();
			// int velocity = event.getVelocity();
			moveToTom(note);
			try {
				Thread.sleep((long)(cercleRepresentation.get(i+1).getTemps()-timestamp));
			} catch (ArrayIndexOutOfBoundsException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				this.deleteCircles();
				this.deleteEllipses();
				Thread.currentThread().interrupt();
			}
		}
		//last event
		Event event = cercleRepresentation.get(i);
		int note = event.getNote();
		// int velocity = event.getVelocity();
		moveToTom(note);
	}
	
	public CerclesRepresentation getCercleRepresentation(){
		return this.cercleRepresentation;
	}
	
	private void deleteCircles(){
	    Iterator it = liaisonCirclesToPath.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        Circle c = (Circle) pair.getKey();
	        it.remove(); // avoids a ConcurrentModificationException
	        Platform.runLater(() -> controller.removeShape(c));
	    }
	}
	
	private void deleteEllipses(){
	    Iterator it = liaisonEllipsesToPath.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        Ellipse e = (Ellipse) pair.getKey();
	        it.remove(); // avoids a ConcurrentModificationException
	        Platform.runLater(() -> controller.removeShape(e));
	    }
	}
		
}
