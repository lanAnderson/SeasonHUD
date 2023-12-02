package club.iananderson.seasonhud.impl.minimaps;

import club.iananderson.seasonhud.SeasonHUD;
import journeymap.client.JourneymapClient;
import journeymap.client.api.IClientAPI;
import journeymap.client.api.IClientPlugin;
import journeymap.client.api.event.ClientEvent;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.EnumSet;

import static club.iananderson.seasonhud.config.Config.journeyMapAboveMap;
import static journeymap.client.api.event.ClientEvent.Type.*;

@ParametersAreNonnullByDefault
public class JourneyMapAPI implements IClientPlugin {
    // API reference
    private IClientAPI jmAPI = null;

    @Override
    public void initialize(final IClientAPI jmAPI) {
        this.jmAPI = jmAPI;

        jmAPI.subscribe(getModId(), EnumSet.of(MAPPING_STARTED, MAPPING_STOPPED, MAP_CLICKED, MAP_DRAGGED, MAP_MOUSE_MOVED, REGISTRY));
    }

    @Override
    public String getModId() {
        return SeasonHUD.MOD_ID;
    }

    @Override
    public void onEvent(ClientEvent event) {

    }

    public static int infoLabelCount(){
        JourneymapClient jm = JourneymapClient.getInstance();
        String emptyLabel = "jm.theme.labelsource.blank";
        String info1Label = jm.getActiveMiniMapProperties().info1Label.get();
        String info2Label = jm.getActiveMiniMapProperties().info2Label.get();
        String info3Label = jm.getActiveMiniMapProperties().info3Label.get();
        String info4Label = jm.getActiveMiniMapProperties().info4Label.get();

        int infoLabelCount = 0;
            if (journeyMapAboveMap.get()){
                infoLabelCount = 1;

                if (!info1Label.equals(emptyLabel)) {
                    infoLabelCount++;
                }
                if (!info2Label.equals(emptyLabel)) {
                    infoLabelCount++;
                }
            }
            else{
                if (!info3Label.equals(emptyLabel)) {
                    infoLabelCount++;
                }
                if (!info4Label.equals(emptyLabel)) {
                    infoLabelCount++;
                }
            }
            return infoLabelCount;
    }
}
