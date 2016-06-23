// My solution in Java to the problem posted here:
// https://www.reddit.com/r/dailyprogrammer/comments/4paxp4/20160622_challenge_272_intermediate_dither_that/

// Expansion idea: allow user to specify their own error diffusion matrix (from file)

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;


class ErrorAtOffset {
	public final int xOffset;
	public final int yOffset;
	public final double coefficient;
	
	public ErrorAtOffset(int x, int y, double c) {
		xOffset = x;
		yOffset = y; 
		coefficient = c;
	}
}

public class Challenge272INTR {
	private static final ErrorAtOffset[] floydSteinberg =
		{new ErrorAtOffset(1, 0, 7.0 / 16.0), new ErrorAtOffset(-1, 1, 3.0 / 16.0),
		 new ErrorAtOffset(0, 1, 5.0 / 16.0), new ErrorAtOffset(1, 1, 1.0 / 16.0)};
	
	private static final ErrorAtOffset[] jarvisJudiceNinke =
		{new ErrorAtOffset(1, 0, 7.0 / 48.0), new ErrorAtOffset(2, 0, 5.0 / 48.0),
		 new ErrorAtOffset(-2, 1, 3.0 / 48.0), new ErrorAtOffset(-1, 1, 5.0 / 48.0),
		 new ErrorAtOffset(0, 1, 7.0 / 48.0), new ErrorAtOffset(1, 1, 5.0 / 48.0),
		 new ErrorAtOffset(2, 1, 3.0 / 48.0), new ErrorAtOffset(-2, 2, 1.0 / 48.0),
		 new ErrorAtOffset(-1, 2, 3.0 / 48.0), new ErrorAtOffset(0, 2, 5.0 / 48.0),
		 new ErrorAtOffset(1, 2, 3.0 / 48.0), new ErrorAtOffset(2, 2, 1.0 / 48.0)};
	
	private static final ErrorAtOffset[] stucki =
		{new ErrorAtOffset(1, 0, 8.0 / 42.0), new ErrorAtOffset(2, 0, 4.0 / 42.0),
		 new ErrorAtOffset(-2, 1, 2.0 / 42.0), new ErrorAtOffset(-1, 1, 4.0 / 42.0),
		 new ErrorAtOffset(0, 1, 8.0 / 42.0), new ErrorAtOffset(1, 1, 4.0 / 42.0),
		 new ErrorAtOffset(2, 1, 2.0 / 42.0), new ErrorAtOffset(-2, 2, 1.0 / 42.0),
		 new ErrorAtOffset(-1, 2, 2.0 / 42.0), new ErrorAtOffset(0, 2, 4.0 / 42.0),
		 new ErrorAtOffset(1, 2, 2.0 / 42.0), new ErrorAtOffset(2, 2, 1.0 / 42.0)};	
		
	private static final ErrorAtOffset[] atkinson =
		{new ErrorAtOffset(1, 0, 1.0 / 8.0), new ErrorAtOffset(2, 0, 1.0 / 8.0),
		 new ErrorAtOffset(-1, 1, 1.0 / 8.0), new ErrorAtOffset(0, 1, 1.0 / 8.0),
		 new ErrorAtOffset(1, 1, 1.0 / 8.0), new ErrorAtOffset(0, 2, 1.0 / 8.0)};
	
	private static final ErrorAtOffset[] burkes =
		{new ErrorAtOffset(1, 0, 8.0 / 32.0), new ErrorAtOffset(2, 0, 4.0 / 32.0),
		 new ErrorAtOffset(-2, 1, 2.0 / 32.0), new ErrorAtOffset(-1, 1, 4.0 / 32.0),
		 new ErrorAtOffset(0, 1, 8.0 / 32.0), new ErrorAtOffset(1, 1, 4.0 / 32.0),
		 new ErrorAtOffset(2, 1, 2.0 / 32.0)};
	
	private static final ErrorAtOffset[] sierra3 =
		{new ErrorAtOffset(1, 0, 5.0 / 32.0), new ErrorAtOffset(2, 0, 3.0 / 32.0),
		 new ErrorAtOffset(-2, 1, 2.0 / 32.0), new ErrorAtOffset(-1, 1, 4.0 / 32.0),
		 new ErrorAtOffset(0, 1, 5.0 / 32.0), new ErrorAtOffset(1, 1, 4.0 / 32.0),
		 new ErrorAtOffset(2, 1, 2.0 / 32.0), new ErrorAtOffset(-1, 2, 2.0 / 32.0),
		 new ErrorAtOffset(0, 2, 3.0 / 32.0), new ErrorAtOffset(1, 2, 2.0 / 32.0)};
		
	private static final ErrorAtOffset[] sierra2 =
		{new ErrorAtOffset(1, 0, 4.0 / 16.0), new ErrorAtOffset(2, 0, 3.0 / 16.0),
		 new ErrorAtOffset(-2, 1, 1.0 / 16.0), new ErrorAtOffset(-1, 1, 2.0 / 16.0),
		 new ErrorAtOffset(0, 1, 3.0 / 16.0), new ErrorAtOffset(1, 1, 2.0 / 16.0),
		 new ErrorAtOffset(2, 1, 1.0 / 16.0)};		
	
	private static final ErrorAtOffset[] sierraLite =
		{new ErrorAtOffset(1, 0, 2.0 / 4.0), new ErrorAtOffset(-1, 1, 1.0 / 4.0),
		 new ErrorAtOffset(0, 1, 1.0 / 4.0)};
	
	private static final ErrorAtOffset[] threshold = {};
	
	private static ErrorAtOffset[] getMatrix(String matrS) {
		switch(matrS) {
			case "fs": return floydSteinberg;
			case "jjn": return jarvisJudiceNinke;
			case "stucki": return stucki;
			case "atkinson": return atkinson;
			case "burkes": return burkes;
			case "s3": return sierra3;
			case "s2": return sierra2;
			case "sl": return sierraLite;
			case "threshold": return threshold;
			default: 
				System.err.println("Invalid dithering algorithm.");
				printHelp();
				return null; // unreachable
		}
	}	
		
	static double getLuminance(int rgb) {
		return (0.2126 * ((rgb >> 16) & 0xff)) + (0.7152 * ((rgb >> 8) & 0xff)) + (0.0722 * (rgb & 0xff));
	}
	
	static void printUsage() {
		System.err.println("Usage: java Challenge272INTR <in_file> [out_file] [dithering_alg] [theshold #]");
	}
	
	static void printHelp() {
		printUsage();
		System.err.println("\n* If no file name is specified, defaults to input_image.dithered.\n"
							+ "* If no algorithm is supplied, defaults to Floyd-Steinberg.\n"
							+ "* If no threshold value is suppied, defaults to the image mean.\nA number greater "
							+ "than 255 defaults to 255 and a number less than 0 defaults to 0.\n\n"
							+ "Supported algorithms:\n"
							+ "\tArgument:\tFull Algorithm Name:\n"
							+ "\tfs\t\tFloyd-Steinberg [default]\n"
							+ "\tjjn\t\tJarvis, Judice, Ninke\n"
							+ "\tstucki\t\tStucki\n"
							+ "\tatkinson\tAtkinson\n"
							+ "\tburkes\t\tBurkes\n"
							+ "\ts3\t\tSierra-3\n"
							+ "\ts2\t\t2-Line Sierra\n"
							+ "\tsl\t\tSierra Lite\n"
							+ "\tthreshold\tThreshold (average) -- no error diffusion\n");
		System.exit(1);
	}

	public static void main(String[] args) {
		try {
			if(args[0].equals("--help"))
				printHelp();
			
			File inputFile = new File(args[0]);			
			BufferedImage inputImage = ImageIO.read(inputFile);
			
			final ErrorAtOffset[] ditheringMatrix = getMatrix((args.length > 2) ? args[2] : "fs");
			
			int inputHeight = inputImage.getHeight();
			int inputWidth = inputImage.getWidth();
			
			int[] copiedPixels = Arrays.stream(inputImage.getRGB(0, 0, inputWidth, inputHeight, null, 0, inputWidth))
									.map(i -> (int)getLuminance(i)).toArray();
			
			int pixelMeanLuminance = (args.length > 3)
							? Math.max(0, Math.min(255, Integer.parseInt(args[3]))) 
							: (int)Arrays.stream(copiedPixels)
									.mapToDouble(i -> i)
									.reduce(0.0, (a, b) -> a + (b / ((double)(copiedPixels.length))));
			
			int[] ditheredPixels = new int[copiedPixels.length];
			
			for(int i = 0; i < copiedPixels.length; i++) {
				ditheredPixels[i] = (copiedPixels[i] > pixelMeanLuminance) ? 0xffffffff : 0xff000000;
				
				int err = copiedPixels[i] - ((ditheredPixels[i] == 0xffffffff) ? 255 : 0);
				
				for(ErrorAtOffset eao : ditheringMatrix)
					if((i + eao.xOffset + (inputWidth * eao.yOffset)) < copiedPixels.length)
						copiedPixels[i + eao.xOffset + (inputWidth * eao.yOffset)] += (int)(err * eao.coefficient);
			}
			
			BufferedImage outputImage = new BufferedImage(inputWidth, inputHeight, BufferedImage.TYPE_BYTE_GRAY);
			outputImage.setRGB(0, 0, inputWidth, inputHeight, ditheredPixels, 0, inputWidth);
			
			String outputName = ((args.length < 2) ? args[0] + ".dithered" : args[1]); 
			String imageFormat = ImageIO.getImageReaders(ImageIO.createImageInputStream(inputFile)).next().getFormatName();
			
			ImageIO.write(outputImage, imageFormat, new File(outputName));
					
			System.out.println("Wrote file " + outputName + " in format " + imageFormat);
		} catch (ArrayIndexOutOfBoundsException e) {
			printUsage();
			System.err.println("To see a list of supported algorithms: java Challenge272INTR --help");
			System.exit(1);
		} catch (NumberFormatException e) {
			System.err.println("ERROR: threshold must be an integer between 0 and 255.");
			System.exit(1);
		} catch (IOException e) {
			System.err.println("ERROR: Could not read file " + args[0] + "\nPerhaps it doesn't exist?");
			System.exit(1);
		} catch (NullPointerException e) {
			System.err.println("ERROR: Could not load " + args[0] + " as an image file.");
			System.exit(1);
		}
	}
}
