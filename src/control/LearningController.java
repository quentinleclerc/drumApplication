package control;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

import midi.Event;
import midi.Midifile;
import midi.SoundRecord;

public class LearningController extends JFrame
{
	/**
	 * 
	 */
	private static final double PRECISION = 0.1;
	private static final long serialVersionUID = 1L;
	static int first =0;
	static boolean succes= false;
	static Date dateDeb;
	static Date actualDate;
	static int timing;
	static JButton led1;
	static JButton led2;
	static JButton ledret1;
	static JButton ledret2;
	static JLabel timer;
	static JLabel statut;
	static JButton levelup;
	static Midifile midifile= new Midifile();
	static SoundRecord local;
	static SoundRecord example;
	final static JFrame fenetre = new JFrame();
	
	static long score_tempo;
	static long score_decalage;
	
	static long dernier_bat=0;
	
	static JLabel score_t;
	static JLabel score_d;
	
	final static int win_score=1;

    public static void main(String[] args) 
    {
    	init();    	
    } 

    
    public static void init(){
    	
    	GridLayout lay = new GridLayout(3,2);
    	statut = new JLabel("Salut");
    	example = midifile.getFile2comp();
    	local = new SoundRecord("Macarena");
        fenetre.setLayout(lay);
    	fenetre.setSize(600, 600);
    	fenetre.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        TextArea input = new TextArea("");
        
        led1 = new JButton("a");
        led2 = new JButton("z");
        levelup = new JButton("LevelUp");
		led1.setFont(new Font("Arial", Font.PLAIN, 40));
		led2.setFont(new Font("Arial", Font.PLAIN, 40));
        led1.setForeground(Color.GREEN);
        led2.setForeground(Color.GREEN);
        timer = new JLabel("00:00");
        
        ledret1 = new JButton("a");
        ledret2 = new JButton("a");
        ledret1.setFont(new Font("Arial", Font.PLAIN, 40));
		ledret2.setFont(new Font("Arial", Font.PLAIN, 40));
        ledret1.setForeground(Color.BLACK);
        ledret2.setForeground(Color.BLACK);

        score_d = new JLabel(Long.toString(score_decalage));
        score_t = new JLabel(Long.toString(score_tempo));

        
        fenetre.add(input);
        
        fenetre.add(led1);
        fenetre.add(led2);
    	fenetre.add(timer);
    	fenetre.add(score_d);
    	fenetre.add(score_t);
    	fenetre.add(statut);
    	fenetre.add(levelup);
        fenetre.add(ledret1);
        fenetre.add(ledret2);
    	levelup.setVisible(false);
    	fenetre.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		fenetre.setVisible(true);
    	
		input.addKeyListener(new KeyListener() {
					
			@Override
			public void keyTyped(KeyEvent e) {
				if (first ==0){
					dateDeb = new Date();
					//majt();
					//play();
					first++;
				}
				char key = e.getKeyChar();
				if (key==97 || key==122){
					actualDate = new Date();
					timing = (int) (actualDate.getTime() - dateDeb.getTime());
					Event event = new Event(timing-dernier_bat, key, 100);
					local.addEvent(event);
					allumage(key);
					System.out.println("local : " + local.toString());
				}
			}
			
			
			

			@Override
			public void keyReleased(KeyEvent e) {
								
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
								
			}
		});
		
		levelup.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//raz();
				example.augmenter_difficlute(0.8);
				statut.setText("now you level up!");			
				levelup.setVisible(false);
				
			}
		});
    }

    protected static void allumage(int led) {
		int size_local = local.getEvents().size();
		long tempo = example.getTempo();
		Color col = Color.GREEN;
		if (size_local==0){
			col = Color.BLACK;
		}
		else{
			long last_ex = example.getEvents().get(size_local-1).getTemps();
			long last_loc = local.getEvents().get(size_local-1).getTemps();
			if (Math.abs(last_ex-last_loc)<=tempo*PRECISION){
				col = Color.GREEN;
			}
			else if (Math.abs(last_ex-last_loc)<=tempo*PRECISION*2){
				col = Color.ORANGE;
			}
			else{
				col = Color.RED;
			}
		}

		if (led==97){
			ledret1.setForeground(col);
		}
		else{
			ledret2.setForeground(col);
		}
	}



	public static void allumer_led(int note){
		try {
		if (note==97){
			led1.setForeground(Color.RED);
			Thread.sleep(100);
			led1.setForeground(Color.GREEN);
		}
		else{
	    	led2.setForeground(Color.RED);
			Thread.sleep(100);
	    	led2.setForeground(Color.GREEN);
		}
    	
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
    
  
}
