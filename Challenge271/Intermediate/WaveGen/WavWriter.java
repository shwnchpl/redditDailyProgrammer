/*
 * Code by Shawn M. Chapla, 17 June 2016.
 */
 
package WaveGen;

public class WavWriter {
	byte[] header;
	byte[] data;
	int sampleRate;
	
	private static byte[] shortToByteArr(short num) {  // Little Endian Format
  		return new byte[] {
  			(byte)(num & 0xff),
  			(byte)((num >> 8) & 0xff)
  		};
  	}
    
	private static byte[] intToByteArr(int num) {	// Little Endian Format
		return new byte[] {
			(byte)(num & 0xff),
			(byte)((num >> 8) & 0xff),
			(byte)((num >> 16) & 0xff),
			(byte)((num >> 24) & 0xff)
		};
	}
	
	public WavWriter(byte[] d, int sr) {
		header = new byte[44]; 
		data = d;
	
		System.arraycopy("RIFF".getBytes(), 0, header, 0, 4);
		System.arraycopy(intToByteArr(data.length + 44 - 8), 0, header, 4, 4);
		System.arraycopy("WAVEfmt ".getBytes(), 0, header, 8, 8);
		System.arraycopy(intToByteArr(16), 0, header, 16, 4);
		System.arraycopy(shortToByteArr((short)1), 0, header, 20, 2);   // PCM format
		System.arraycopy(shortToByteArr((short)1), 0, header, 22, 2);   // one channel
		System.arraycopy(intToByteArr(sr), 0, header, 24, 4);
		System.arraycopy(intToByteArr(sr * 2), 0, header, 28, 4); 		// for 1 channel and 16bits per sample
		System.arraycopy(shortToByteArr((short)2), 0, header, 32, 2); 	// bits per sample * channels / 8
		System.arraycopy(shortToByteArr((short)16), 0, header, 34, 2);
		System.arraycopy("data".getBytes(), 0, header, 36, 4);
		System.arraycopy(intToByteArr(data.length), 0, header, 40, 4);  // size of the data chunk
	}
	
	public void writeToStdout() {
		try {
			System.out.write(header);
			System.out.write(data);
			System.out.flush();
		} catch (Exception e) {
			System.err.println("Failed to write WAV file to STDOUT.");
		}
	}
}

