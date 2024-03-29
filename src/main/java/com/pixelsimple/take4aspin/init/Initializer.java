/**
 * � PixelSimple 2011-2012.
 */
package com.pixelsimple.take4aspin.init;

import java.util.HashMap;
import java.util.Map;

import com.pixelsimple.appcore.init.AppInitializer;
import com.pixelsimple.appcore.init.BootstrapInitializer;
import com.pixelsimple.commons.util.OSUtils;
import com.pixelsimple.transcoder.init.TranscoderInitializer;

/**
 *
 * @author Akshay Sharma
 * Jan 19, 2012
 */
public class Initializer {

	public static void initAppForTakingASpin() {
		
		Map<String, String> configs = new HashMap<String, String>();
		
		if (OSUtils.isWindows()) {
			// Keep this path up to date with ffmpeg updates
			configs.put(BootstrapInitializer.JAVA_SYS_ARG_APP_HOME_DIR, "c:/dev/pixelsimple");
			configs.put("ffprobePath", "c:/dev/pixelsimple/ffprobe/32_bit/1.0/ffprobe.exe"); 
			configs.put("ffmpegPath", "c:/dev/pixelsimple/ffmpeg/32_bit/1.0/ffmpeg.exe"); 
		} else if (OSUtils.isMac()) {
			// Keep this path up to date with ffmpeg updates
			configs.put(BootstrapInitializer.JAVA_SYS_ARG_APP_HOME_DIR,  OSUtils.USER_SYSTEM_HOME_DIR + "/dev/pixelsimple");
			configs.put("ffprobePath",  OSUtils.USER_SYSTEM_HOME_DIR + "/dev/pixelsimple/ffprobe/32_bit/1.0/ffprobe"); 
			configs.put("ffmpegPath",  OSUtils.USER_SYSTEM_HOME_DIR + "/dev/pixelsimple/ffmpeg/32_bit/1.0/ffmpeg"); 
		}  else {
			// add linux based tests when ready :-)
		}
		
		AppInitializer initializer = new AppInitializer();

		// Depends on Transcode initializer
		initializer.addModuleInitializable(new TranscoderInitializer());
		try {
			initializer.init(configs);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
