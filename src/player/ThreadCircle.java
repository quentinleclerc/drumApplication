package player;

import java.util.HashMap;
import java.util.Map;
import control.PlayInRythmController;
import javafx.animation.PathTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import midi.CerclesRepresentation;
import midi.Event;
import midi.MidiFileToSong;
import midi.SoundRecord;
import shapes.HandlerCircle;

public class ThreadCircle implements Runnable {
	private Map<Integer, Double> kickDistance;
	private Map<Integer, Circle> liaisonToms;
	private Map<Circle, PathTransition> liaisonCirclesToPath = new HashMap<>();
	private Map<Ellipse, PathTransition> liaisonEllipsesToPath = new HashMap<>();
	private Ellipse pedale;
	private HandlerCircle hc;
	private CerclesRepresentation cercleRepresentation;

	// Default Constructor
	public ThreadCircle(Map<Integer, Double> map, Map<Integer, Circle> map2, Ellipse ellipse) {
		this.kickDistance = map;
		this.liaisonToms = map2;
		this.pedale = ellipse;
		hc = new HandlerCircle();
		initializeCirclesRep();
	}

	private void initializeCirclesRep(){
		MidiFileToSong translator = new MidiFileToSong("test.mid",3F,0);
		SoundRecord song = translator.getSong();
		this.cercleRepresentation = new CerclesRepresentation(song, kickDistance);
		System.out.println(cercleRepresentation);

	}

	public void removeCircle(Circle sh){
		this.liaisonCirclesToPath.get(sh).setOnFinished(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				PlayInRythmController.removeShape(sh);
			}
		});
	}

	public void removeEllipse(Ellipse sh){
		this.liaisonEllipsesToPath.get(sh).setOnFinished(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				PlayInRythmController.removeShape(sh);
			}
		});
	}
	
	public void moveToTom(int note){
		if(note == 35){
			Ellipse pedaleTemp = hc.makeEllipse(pedale);
			System.out.println("PEDALE NULLE ? : " + pedaleTemp == null  + "!!!!!!!");
			PlayInRythmController.addShape(pedaleTemp);
			liaisonEllipsesToPath.put(pedaleTemp, hc.moveEllipse(pedaleTemp, pedale));
		}
		else{
			Circle target = liaisonToms.get(note);
			Circle circleTemp = hc.makeCircle(target);
			System.out.println("CERCLE NUL ? : " + (circleTemp == null) + "!!!!!!!");
			PlayInRythmController.addShape(circleTemp);
			liaisonCirclesToPath.put(circleTemp, hc.moveCircle(circleTemp, target));
		}
	}

	public void run() {
		int i;
		for (i = 0; i < cercleRepresentation.size()-1; i++) {
			Event event = cercleRepresentation.get(i);
			double timestamp = event.getTemps();
			int note = event.getNote();
			int velocity = event.getVelocity();
			moveToTom(note);
			try {
				Thread.sleep((long)(cercleRepresentation.get(i+1).getTemps()-timestamp));
			} catch (ArrayIndexOutOfBoundsException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		//last event
		Event event = cercleRepresentation.get(i);
		int note = event.getNote();
		int velocity = event.getVelocity();
		moveToTom(note);
	}
	
	public CerclesRepresentation getCercleRepresentation(){
		return this.cercleRepresentation;
	}
}
