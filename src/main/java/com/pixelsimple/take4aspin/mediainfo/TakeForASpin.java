/**
 * © PixelSimple 2011-2012.
 */
package com.pixelsimple.take4aspin.mediainfo;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pixelsimple.commons.media.MediaInspector;
import com.pixelsimple.take4aspin.init.Initializer;

/**
 *
 * @author Akshay Sharma
 * Nov 28, 2011
 */
public class TakeForASpin {
	static final Logger LOG = LoggerFactory.getLogger(TakeForASpin.class);
	
	/**
	 * Pass a paramter to the main method called "filePathWithFileName". It should have a value (separated by key).
	 * Example: filePathWithFileName=C:/Data/video_test/HTTYD_1-024.mkv
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
		Map<String, String> overrideParams = null;
		
		if (args != null && args.length > 0) {
			overrideParams = new HashMap<String, String>(8);
			
			for (String a : args) {
				int indexEquals = a.indexOf("=");
				
				if (indexEquals != -1) {
					overrideParams.put(a.substring(0, indexEquals), a.substring(indexEquals + 1));
				}
			}
		}
		LOG.debug("takeForASping:Override params::{}", overrideParams);
		
		this.getContainerInfo(overrideParams);
	}
	
	private void getContainerInfo(Map<String, String> overrideParams) {
		Map<String, String> params = new HashMap<String, String>(8);
		
		if (overrideParams != null) {
			params.putAll(overrideParams);
		}
		LOG.debug("transcode::final params::{}", params);

		String filePathWithFileName = params.get("filePathWithFileName");
		new MediaInspector().createMediaContainer(filePathWithFileName);
	}

}
