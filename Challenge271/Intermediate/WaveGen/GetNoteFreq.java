/*
 * Code by Shawn M. Chapla, 17 June 2016.
 */

package WaveGen;

public class GetNoteFreq {
	public static double getFreq(String note) {
		switch(note) {	// all notes C0 through B8 tuned A4 = 440.0hz, hardcoded
			case "C0":  return 16.35;
			case "C#0":
			case "Db0": return 17.32;
			case "D0":  return 18.35;
			case "D#0":
			case "Eb0": return 19.45;
			case "E0":  return 20.60;
			case "F0":  return 21.83;
			case "F#0":
			case "Gb0": return 23.12;
			case "G0":  return 24.50;
			case "G#0":
			case "Ab0": return 25.96;
			case "A0":  return 27.50;
			case "A#0":
			case "Bb0": return 29.14;
			case "B0":  return 30.87;
			case "C1":  return 32.70;
			case "C#1":
			case "Db1": return 34.65;
			case "D1":  return 36.71;
			case "D#1":
			case "Eb1": return 38.89;
			case "E1":  return 41.20;
			case "F1":  return 43.65;
			case "F#1":
			case "Gb1": return 46.25;
			case "G1":  return 49.00;
			case "G#1":
			case "Ab1": return 51.91;
			case "A1":  return 55.00;
			case "A#1":
			case "Bb1": return 58.27;
			case "B1":  return 61.74;
			case "C2":  return 65.41;
			case "C#2":
			case "Db2": return 69.30;
			case "D2":  return 73.42;
			case "D#2":
			case "Eb2": return 77.78;
			case "E2":  return 82.41;
			case "F2":  return 87.31;
			case "F#2":
			case "Gb2": return 92.50;
			case "G2":  return 98.00;
			case "G#2":
			case "Ab2": return 103.83;
			case "A2":  return 110.00;
			case "A#2":
			case "Bb2": return 116.54;
			case "B2":  return 123.47;
			case "C3":  return 130.81;
			case "C#3":
			case "Db3": return 138.59;
			case "D3":  return 146.83;
			case "D#3":
			case "Eb3": return 155.56;
			case "E3":  return 164.81;
			case "F3":  return 174.61;
			case "F#3":
			case "Gb3": return 185.00;
			case "G3":  return 196.00;
			case "G#3":
			case "Ab3": return 207.65;
			case "A3":  return 220.00;
			case "A#3":
			case "Bb3": return 233.08;
			case "B3":  return 246.94;
			case "C4":  return 261.63;
			case "C#4":
			case "Db4": return 277.18;
			case "D4":  return 293.66;
			case "D#4":
			case "Eb4": return 311.13;
			case "E4":  return 329.63;
			case "F4":  return 349.23;
			case "F#4":
			case "Gb4": return 369.99;
			case "G4":  return 392.00;
			case "G#4":
			case "Ab4": return 415.30;
			case "A":
			case "A4":  return 440.00;
			case "A#4":
			case "Bb4": return 466.16;
			case "B":
			case "B4":  return 493.88;
			case "C":
			case "C5":  return 523.25;
			case "C#5":
			case "Db5": return 554.37;
			case "D":
			case "D5":  return 587.33;
			case "D#5":
			case "Eb5": return 622.25;
			case "E":
			case "E5":  return 659.25;
			case "F":
			case "F5":  return 698.46;
			case "F#5":
			case "Gb5": return 739.99;
			case "G":
			case "G5":  return 783.99;
			case "G#5":
			case "Ab5": return 830.61;
			case "A5":  return 880.00;
			case "A#5":
			case "Bb5": return 932.33;
			case "B5":  return 987.77;
			case "C6":  return 1046.50;
			case "C#6":
			case "Db6": return 1108.73;
			case "D6":  return 1174.66;
			case "D#6":
			case "Eb6": return 1244.51;
			case "E6":  return 1318.51;
			case "F6":  return 1396.91;
			case "F#6":
			case "Gb6": return 1479.98;
			case "G6":  return 1567.98;
			case "G#6":
			case "Ab6": return 1661.22;
			case "A6":  return 1760.00;
			case "A#6":
			case "Bb6": return 1864.66;
			case "B6":  return 1975.53;
			case "C7":  return 2093.00;
			case "C#7":
			case "Db7": return 2217.46;
			case "D7":  return 2349.32;
			case "D#7":
			case "Eb7": return 2489.02;
			case "E7":  return 2637.02;
			case "F7":  return 2793.83;
			case "F#7":
			case "Gb7": return 2959.96;
			case "G7":  return 3135.96;
			case "G#7":
			case "Ab7": return 3322.44;
			case "A7":  return 3520.00;
			case "A#7":
			case "Bb7": return 3729.31;
			case "B7":  return 3951.07;
			case "C8":  return 4186.01;
			case "C#8":
			case "Db8": return 4434.92;
			case "D8":  return 4698.63;
			case "D#8":
			case "Eb8": return 4978.03;
			case "E8":  return 5274.04;
			case "F8":  return 5587.65;
			case "F#8":
			case "Gb8": return 5919.91;
			case "G8":  return 6271.93;
			case "G#8":
			case "Ab8": return 6644.88;
			case "A8":  return 7040.00;
			case "A#8":
			case "Bb8": return 7458.62;
			case "B8":  return 7902.13;
			case "_":
			default: return 0;
		}
	}
}
