package de.zordak.vizit;


import de.zordak.vizit.platform.Services;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VizItCommon {


        /* TODO:
            On load, ensure consistent state of display blocks against loaded data
             restore display blocks from saved data
             on chunk load, check if display blocks are still valid and add/remove them
             on start tracking display blocks, check if players should see them based on visibility settings
        */


    public static void init() {
        Constants.LOGGER.debug("[{}] Loading {} in a {} environment!", Constants.MOD_ID, Services.PLATFORM.getPlatformName(), Services.PLATFORM.getEnvironmentName());

        if (Services.PLATFORM.isModLoaded(Constants.MOD_ID)) {
            Constants.LOGGER.debug("Hi, {}! Great you have you with us! One of us, one of us, one of us, ...", Constants.MOD_ID);
        }
    }


}
