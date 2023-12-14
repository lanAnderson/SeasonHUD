package club.iananderson.seasonhud;

import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Common {
	public static final String MOD_ID = "seasonhud";
	public static final String MOD_NAME = "SeasonHUD";
	public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);
	public static final ResourceLocation SEASON_ICONS = new ResourceLocation(MOD_ID,"season_icons");
	public static final Style SEASON_STYLE = Style.EMPTY.withFont(SEASON_ICONS);
}