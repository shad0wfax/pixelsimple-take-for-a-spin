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

import com.pixelsimple.appcore.registry.GenericRegistryEntry;
import com.pixelsimple.appcore.registry.RegistryService;
import com.pixelsimple.commons.media.Container;
import com.pixelsimple.commons.media.MediaInspector;
import com.pixelsimple.commons.media.exception.MediaException;
import com.pixelsimple.take4aspin.init.Initializer;
import com.pixelsimple.transcoder.Transcoder;
import com.pixelsimple.transcoder.TranscoderOutputSpec;
import com.pixelsimple.transcoder.config.TranscoderRegistryKeys;
import com.pixelsimple.transcoder.exception.TranscoderException;
import com.pixelsimple.transcoder.profile.Profile;

/**
 *
 * @author Akshay Sharma
 * Nov 28, 2011
 */
public class TakeForManySpins implements Runnable {
	static final Logger LOG = LoggerFactory.getLogger(TakeForManySpins.class);
	static Map<String, String> argParams = null;

	
	/**
	 * Pass three parameters to the main method called "inputFilePathWithFileName", "outputFilePath", and 
	 * "outputFileNameWithoutExtension".
	 * It should have a value (separated by key).
	 * 
	 * Example:
	 * inputFilePathWithFileName=C:/Data/video_test/HTTYD_1-021_poor.mov
	 * outputFilePathWithFileName=C:/Data/video_test/transcoded
	 * outputFileNameWithoutExtension=test_flv_transcoded
	 * 
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
		String profileToUse = argParams.get("profile");
		MediaInspector inspector = new MediaInspector();
		Container inputMedia = null;
		try {
			inputMedia = inspector.createMediaContainer(inputFile);
		} catch (MediaException e) {
			// TODO Auto-generated catch block
			LOG.error("{}", e);
			LOG.debug("Invalid media input file {}", inputFile);
			System.exit(0);
		} 

		String outputFilePath = argParams.get("outputFilePath");
		String outputFileNameWithoutExtension = argParams.get("outputFileNameWithoutExtension");
		
		outputFileNameWithoutExtension = outputFileNameWithoutExtension + "_" + "Thread_" + Thread.currentThread().getId(); 
		
		// TODO: In non-test code, this target profile will use a good profile match algorithm
		GenericRegistryEntry entry =  RegistryService.getGenericRegistryEntry();
		Map<String, Profile> profiles = entry.getEntry(TranscoderRegistryKeys.MEDIA_PROFILES);

		Profile profile = profiles.get(profileToUse);
		
		TranscoderOutputSpec spec = new TranscoderOutputSpec(profile, outputFilePath, outputFileNameWithoutExtension);
		
		LOG.debug("transcode::Traget profile::{} and output file:: {}", profile,  outputFilePath + "/" + outputFileNameWithoutExtension);

		Transcoder transcoder = new Transcoder();
		try {
			transcoder.transcode(inputMedia, spec);
		} catch (TranscoderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
