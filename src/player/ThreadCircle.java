package player;

import java.util.HashMap;
import java.util.Map;
import control.PlayInRythmController;
import javafx.animation.PathTransition;
import javafx.application.Platform;
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
	private final PlayInRythmController controller;
	private Map<Integer, Double> kickDistance;
	private Map<Integer, Circle> liaisonToms;
	private Map<Circle, PathTransition> liaisonCirclesToPath = new HashMap<>();
	private Map<Ellipse, PathTransition> liaisonEllipsesToPath = new HashMap<>();
	private Ellipse pedale;
	private HandlerCircle hc;
	private CerclesRepresentation cercleRepresentation;
	private SoundRecord song;

	// Default Constructor
	public ThreadCircle(PlayInRythmController controller, Map<Integer, Double> map, Map<Integer, Circle> map2, Ellipse ellipse) {
		this.kickDistance = map;
		this.liaisonToms = map2;
		this.pedale = ellipse;
		this.controller = controller;
		hc = new HandlerCircle();
		initializeCirclesRep();
	}

	private void initializeCirclesRep(){
		MidiFileToSong translator = new MidiFileToSong("test.mid",7F,0);
		song = translator.getSong();
		this.cercleRepresentation = new CerclesRepresentation(song, kickDistance);
		System.out.println(cercleRepresentation);

	}

	public void removeCircle(Circle sh){
		this.liaisonCirclesToPath.get(sh).setOnFinished(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Platform.runLater(() -> controller.removeShape(sh));
			}
		});
	}

	public void removeEllipse(Ellipse sh){
		this.liaisonEllipsesToPath.get(sh).setOnFinished(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Platform.runLater(() -> controller.removeShape(sh));
			}
		});
	}
	
	public void moveToTom(int note){
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

		PlayerSong player = new PlayerSong(song);
		player.playSong();

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
				Thread.currentThread().interrupt();
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
