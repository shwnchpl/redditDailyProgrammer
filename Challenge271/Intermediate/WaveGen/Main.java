/*
 * Code by Shawn M. Chapla, 17 June 2016.
 */

package WaveGen;

import java.io.ByteArrayOutputStream;
import java.util.function.BiFunction;

public class Main {
	private static final BiFunction<Double, Integer, Short> sineWave =
			((a, b) -> (short)(32767 * Math.sin((2 * Math.PI) * (b / a)))); 
	private static final BiFunction<Double, Integer, Short> squareWave = 
			((a, b) -> (short)(32767 * ((Math.sin((2*Math.PI) * (b / a)) > 0) ? 1 : -1))); 
	private static final BiFunction<Double, Integer, Short> triangleWave =
			((a, b) -> (short)(((2 * 32767) / Math.PI) * Math.asin(Math.sin(((2 * Math.PI) / a) * b))));
	private static final BiFunction<Double, Integer, Short> sawWave =
			((a, b) -> (short)(((-2 * 32767) / Math.PI) * Math.asin((1 / Math.tan((b * Math.PI) / a)))));
	
	public static void main(String[] args) {
		int sampleRate = 0;
		int noteDuration = 0;
		String noteSequence = "";
		BiFunction<Double, Integer, Short> waveGenerator = sineWave;
    
		try {
			sampleRate = Integer.parseInt(args[0]);
			noteDuration = Integer.parseInt(args[1]);
			noteSequence = args[2];
			if(args.length == 4) {
				switch(args[3]) {
					case "square": waveGenerator = squareWave; break;
					case "sawtooth": waveGenerator = sawWave; break;
					case "triangle": waveGenerator = triangleWave; break;
					case "sine": break;
					default: System.err.println("Invalid wave form. Defaulting to sine.");
				}
			}
		} catch (Exception e) {
			System.err.println("Invalid arguments.\nUsage:\njava WaveGen.Main "
				+ "<sample rate> <note duration> <note string> [wave form]"
				+ "\nExample:\njava WaveGen.Main 441000 300 \"A B C D E F G _ G F E D C B A _ _\" sawtooth");
			System.exit(1);
		}
  
		double sampleT = (double)sampleRate * ((double)noteDuration / 1000.00);
		ByteArrayOutputStream dataBytes = new ByteArrayOutputStream();
    
		for(String note : noteSequence.split("\\s+")) {
			double waveLength = (double)sampleRate / GetNoteFreq.getFreq(note);
		
			for(int j = 0; j < sampleT; j++) {
				short val = waveGenerator.apply(waveLength, j);
				dataBytes.write((byte)(val & 0xff));
				dataBytes.write((byte)((val >> 8) & 0xff));
			}
		}
	
		WavWriter outputWav = new WavWriter(dataBytes.toByteArray(), sampleRate);
		outputWav.writeToStdout();
	}
}
