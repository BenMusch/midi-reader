import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

public class Main {
  private static int toTempo(double bpm) {
    bpm /= 4;
    bpm = 1 / bpm;
    return (int) (10000 * 60 * bpm);
  }

  public static void main(String[] args) throws Exception {
    Sequence sequence = MidiSystem.getSequence(new File("letitgo.mid"));
    System.out.println("tempo " + toTempo(160));
    for (Track track :  sequence.getTracks()) {
      List<Note> unfinished = new ArrayList();
      for (int i=0; i < track.size(); i++) {
        MidiEvent event = track.get(i);
        MidiMessage message = event.getMessage();
        if (message instanceof ShortMessage) {
          ShortMessage sm = (ShortMessage) message;
          int channel = sm.getChannel() + 1;
          int key = sm.getData1();
          int velocity = sm.getData2();
          int beat = (int) Math.floor(4 * event.getTick() / (sequence.getResolution() / 4));
          if (velocity != 0) {
            Note n = new Note(beat, channel, velocity, key);
            unfinished.add(n);
          } else {
            Note temp = new Note(0, channel, velocity, key);
            for (int j = 0; j < unfinished.size(); j++) {
              Note n = unfinished.get(j);
              if (n.equals(temp)) {
                unfinished.remove(j);
                n.finish(beat);
                System.out.println(n.toString());
              }
            }
          }
        }
      }
    }

  }
}