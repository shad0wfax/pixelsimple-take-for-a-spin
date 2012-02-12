/**
 * © PixelSimple 2011-2012.
 */
package com.pixelsimple.take4aspin.transcoder;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pixelsimple.appcore.Registrable;
import com.pixelsimple.appcore.RegistryService;
import com.pixelsimple.commons.media.Container;
import com.pixelsimple.commons.media.MediaInspector;
import com.pixelsimple.commons.media.exception.MediaException;
import com.pixelsimple.take4aspin.init.Initializer;
import com.pixelsimple.transcoder.TranscoderOutputSpec;
import com.pixelsimple.transcoder.VideoTranscoder;
import com.pixelsimple.transcoder.profile.Profile;

/**
 *
 * @author Akshay Sharma
 * Nov 28, 2011
 */
public class TakeForASpin {
	static final Logger LOG = LoggerFactory.getLogger(TakeForASpin.class);
	
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
		
		try {
			TakeForASpin forASpin = new TakeForASpin();
			forASpin.takeForASpin(args);
		} catch (Throwable t) {
			LOG.error("{}", t);
		}

	}
	
	public void takeForASpin(String[] args) {
		Map<String, String> argParams = null;
		
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
		
		this.transcode(argParams);
	}
	
	public void transcode(Map<String, String> argParams) {
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
		
		// TODO: In non-test code, this target profile will use a good profile match algorithm
		@SuppressWarnings("unchecked")
		Map<String, Profile> profiles = (Map<String, Profile>) RegistryService.getRegisteredEntry(Registrable.MEDIA_PROFILES);
		
		Profile profile = profiles.get(profileToUse);
		
		TranscoderOutputSpec spec = new TranscoderOutputSpec(profile, argParams.get("outputFilePath"), argParams.get("outputFileNameWithoutExtension"));
		
		LOG.debug("transcode::Traget profile::{} and output file:: {}", profile,  argParams.get("outputFilePathWithFileName"));
		
		VideoTranscoder videoTranscoder = new VideoTranscoder();
		videoTranscoder.transcode(inputMedia, spec);
	}

}
