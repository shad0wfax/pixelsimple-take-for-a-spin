/**
 * © PixelSimple 2011-2012.
 */
package com.pixelsimple.take4aspin.transcoder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pixelsimple.appcore.RegistryService;
import com.pixelsimple.appcore.media.Profile;
import com.pixelsimple.commons.media.Container;
import com.pixelsimple.commons.media.MediaInspector;
import com.pixelsimple.take4aspin.init.Initializer;
import com.pixelsimple.transcoder.TranscoderOutputSpec;
import com.pixelsimple.transcoder.VideoTranscoder;

/**
 *
 * @author Akshay Sharma
 * Nov 28, 2011
 */
public class TakeForManySpins implements Runnable {
	static final Logger LOG = LoggerFactory.getLogger(TakeForManySpins.class);
	static Map<String, String> argParams = null;

	
	/**
	 * Pass two paramter to the main method called "inputFilePathWithFileName" and "outputFilePathWithFileName".
	 * It should have a value (separated by key).
	 * 
	 * Example:
	 * inputFilePathWithFileName=C:/Data/video_test/HTTYD_1-021_poor.mov
	 * outputFilePathWithFileName=C:/Data/video_test/transcoded/HTTYD_1-021_poor_default_one.flv
	 * @param args
	 */
	public static void main(String[] args) {
		Initializer.initAppForTakingASpin();

		if (args != null && args.length > 0) {
			argParams = new HashMap<String, String>(8);
			
			for (String a : args) {
				int indexEquals = a.indexOf("=");
				
				if (indexEquals != -1) {
					argParams.put(a.substring(0, indexEquals), a.substring(indexEquals + 1));
				}
			}
		}
		LOG.debug("takeForASping:Override params::{}", argParams);
		
		try {
			List<Thread> threads = new ArrayList<Thread>();
			
			for (int i  = 0; i < 8; i++) {
				Thread t = new Thread(new TakeForManySpins());
				threads.add(t);
			}
			
			for (Thread t : threads) {
				t.start();
			}
		} catch (Throwable t) {
			LOG.error("{}", t);
		}

	}
	
	private void transcode(Map<String, String> argParams) {
		String inputFile = argParams.get("inputFilePathWithFileName");
		MediaInspector inspector = new MediaInspector();
		Container inputMedia = inspector.createMediaContainer(inputFile); 

		String outputFileName = argParams.get("outputFilePathWithFileName");
		outputFileName = outputFileName.substring(0, outputFileName.indexOf(".")) + "_" + "Thread_" + Thread.currentThread().getId() 
				+ outputFileName.substring(outputFileName.indexOf("."), outputFileName.length());
		
		// TODO: In non-test code, this target profile will use a good profile match algorithm
		Map<String, Profile> profiles = RegistryService.getMediaProfiles();
		Profile profile = profiles.get("IE_6_7_8_high_bandwidth");
		
		TranscoderOutputSpec spec = new TranscoderOutputSpec(profile, outputFileName);
		
		LOG.debug("transcode::Traget profile::{} and output file:: {}", profile,  outputFileName);

		VideoTranscoder videoTranscoder = new VideoTranscoder();
		videoTranscoder.transcode(inputMedia, spec);
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		LOG.debug("run::Starting to transcode - will not block");
		this.transcode(argParams);
		LOG.debug("run::Finished to calling transcode");
	}
}
