package club.iananderson.seasonhud;

import club.iananderson.seasonhud.platform.CommonPlatformHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Common {
	public static final String MOD_ID = "seasonhud";
	public static final String MOD_NAME = "SeasonHUD";
	public static CommonPlatformHelper platformHelper = new CommonPlatformHelper();
	public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);
}