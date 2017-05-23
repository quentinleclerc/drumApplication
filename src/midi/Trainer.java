package midi;

import control.NoteChannel;
import player.Drummer;

import java.util.ArrayList;
import java.util.Date;

public class Trainer implements NoteChannel {


    private final double PRECISION = 0.1;
    private final int win_score = 1;
    private final Drummer drummer;

    private Boolean success = false;
    private Date dateDeb;
    private int timing;

    private int first;

    private long score_tempo;
    private long score_decalage;

    private long dernier_bat = 0;
    private final SoundRecord record;
    private Date actualDate;
    private SoundRecord played;

    public Trainer(SoundRecord record, Drummer drummer) {
        this.record = record;
        this.drummer = drummer;
    }

    public void startTraining() {
        this.success = false;
        this.dateDeb = new Date();
        System.out.println("datedeb initialisee  : " + dateDeb);
        majt();
        playTraining();

    }

    private void majt(){
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!success){
                    Date actualDate = new Date();
                    timing = (int) (actualDate.getTime() - dateDeb.getTime());
                    //timer.setText(Long.toString(timing/1000)+":"+Long.toString(timing%1000));
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    private void playTraining() {
        first = 0;
        int size = 0;

        ArrayList<Long> list = this.record.getIntervales();

        long temps = list.get(0);
        System.out.println();
        try {
            Thread.sleep(temps);
            int note = record.getEvents().get(0).getNote();
            int velocity = record.getEvents().get(0).getVelocity();

            drummer.noteOn(note, velocity);
            // allumer_led(note);

            while(!success){

                int i;
                System.out.println("Debut file");
                for (i = 0; i <= list.size()-1; i++) {
                    if (first==0){
                        first++;
                    }
                    else{
                        if (i==list.size()){
                            temps = list.get(0);
                        }
                        else{
                            temps = list.get(i);
                        }

                        Thread.sleep(temps-100);

                        System.out.println("valeur de i = " + i);
                        note = record.getEvents().get(i).getNote();
                        velocity = record.getEvents().get(i).getVelocity();

                        drummer.noteOn(note, velocity);
                    }


                }
                System.out.println("sortis de la premiere boucle");
                compare_table(); //checks if the user has entered something and gets a score from it
                if (score_decalage==win_score && score_tempo==win_score){
                    success = true;
                    System.out.println("succes = true");
                }
                size = played.getEvents().size();
                dernier_bat += played.getEvents().get(size-1).getTemps();
                played.clean();
            }
            //timer.setText("00:00:00");
            //statut.setText("YOU WINN!!!!");
            //levelup.setVisible(true);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void raz(){
        this.timing = 0;
        this.first = 0;
        this.score_decalage = 0;
        this.score_tempo = 0;
        refresh_decalage(score_decalage);
        refresh_tempo(score_tempo);
        this.dernier_bat = 0;
        this.success = false;
    }

    private void compare_table(){
        long tempo= score_tempo;
        long decalage = score_decalage;
        if (this.played.getEvents().size()==0){
            System.out.println("coucou");
            raz();
            //levelup.setVisible(false);
            //statut.setText("pause");
        }
        else {
            if (record.getEvents().size() != this.played.getEvents().size() ){
                tempo -= 1;
                decalage -= 1;
            }
            else{
                tempo += tempo((int) (this.record.getTempo()*PRECISION));//compare le decalage par rapport au tempo (PGCD)
                decalage += decalage((int) (this.record.getTempo()*PRECISION));//compare le decalage par rapport au morceau initial
            }
            refresh_tempo(tempo);
            refresh_decalage(decalage);
        }
    }

    private void refresh_decalage(long decalage) {
        if (decalage >= win_score){
            decalage = win_score;
        }
        score_decalage = decalage;
        //score_d.setText(Long.toString(decalage));
    }

    private void refresh_tempo(long tempo) {
        if (tempo>=win_score){
            tempo = win_score;
        }
        score_tempo= tempo;
        //score_t.setText(Long.toString(tempo));
    }

    private int decalage(int precision) {
        ArrayList<Event> intR = record.getEvents();
        ArrayList<Event> intL = played.getEvents();
        int score=0;
        for (int i = 0; i < intR.size(); i++) {
            if ( intL.get(i).getTemps() >= (intR.get(i).getTemps()-precision) && intL.get(i).getTemps() <= (intR.get(i).getTemps()+precision) && intR.get(i).getNote() == intL.get(i).getNote()){
                score+=2;
                System.out.println("augmentation de 1 : " + Integer.toString(score));
            }
            else{
                score-=1;
                System.out.println("diminution de 0.5 : " + Integer.toString(score));
            }
        }
        return score;
    }

    private int tempo(int precision) {
        ArrayList<Long> intR = record.getIntervales();
        ArrayList<Long> intL = played.getIntervales();
        System.out.println("interale de intR : " + intR.size() +"interale de intL : " + intL.size());
        int score=0;
        for (int i = 0; i < intR.size(); i++) {
            if ( intL.get(i) >= (intR.get(i)-precision) && intL.get(i) <= (intR.get(i)+precision)){
                score+=2;
                System.out.println("augmentation de 1 : " + Integer.toString(score));
            }
            else{
                score-=1;
                System.out.println("diminution de 0.5 : " + Integer.toString(score));
            }
        }
        return score;
    }

    @Override
    public void receivedNote(int note, int velocity, long time) {
        actualDate = new Date();
        System.out.println("actualDate = " + actualDate + "dateDeb = " + dateDeb);
        System.out.println("actualDate.getTime = " + actualDate.getTime() + "dateDeb.getTime = " + dateDeb.getTime());
        timing = (int) (actualDate.getTime() - dateDeb.getTime());
        Event event = new Event(timing - dernier_bat, note, velocity);
        played.addEvent(event);
        // allumage(key);
        System.out.println("local : " + played.toString());
    }
}
