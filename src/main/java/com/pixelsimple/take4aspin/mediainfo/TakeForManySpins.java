/**
 * © PixelSimple 2011-2012.
 */
package com.pixelsimple.take4aspin.mediainfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pixelsimple.appcore.Resource;
import com.pixelsimple.appcore.Resource.RESOURCE_TYPE;
import com.pixelsimple.commons.media.MediaInspector;
import com.pixelsimple.commons.media.exception.MediaException;
import com.pixelsimple.take4aspin.init.Initializer;

/**
 *
 * @author Akshay Sharma
 * Nov 28, 2011
 */
public class TakeForManySpins implements Runnable {
	static final Logger LOG = LoggerFactory.getLogger(TakeForManySpins.class);
	static Map<String, String> overrideParams = null;
	
	/**
	 * Pass a paramter to the main method called "filePathWithFileName". It should have a value (separated by key).
	 * Example: filePathWithFileName=C:/Data/video_test/HTTYD_1-024.mkv
	 * @param args
	 */
	public static void main(String[] args) {
		Initializer.initAppForTakingASpin();

		if (args != null && args.length > 0) {
			overrideParams = new HashMap<String, String>(8);
			
			for (String a : args) {
				int indexEquals = a.indexOf("=");
				
				if (indexEquals != -1) {
					overrideParams.put(a.substring(0, indexEquals), a.substring(indexEquals + 1));
				}
			}
		}
		LOG.debug("TakeForManySpinsg:Override params::{}", overrideParams);
		
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
	
	private void getContainerInfo(Map<String, String> overrideParams) {
		Map<String, String> params = new HashMap<String, String>(8);
		
		if (overrideParams != null) {
			params.putAll(overrideParams);
		}
		LOG.debug("transcode::final params::{}", params);

		String filePathWithFileName = params.get("filePathWithFileName");
		try {
			new MediaInspector().createMediaContainer(new Resource(filePathWithFileName, RESOURCE_TYPE.FILE));
		} catch (MediaException e) {
			// TODO Auto-generated catch block
			LOG.error("{}", e);
			LOG.debug("Invalid media input file {}", filePathWithFileName);
			System.exit(0);
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		LOG.debug("run::Starting to get the container info - will block");
		this.getContainerInfo(overrideParams);
		LOG.debug("run::Finished to get the container info - after block");
	}

}
