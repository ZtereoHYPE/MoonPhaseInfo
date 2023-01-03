package codes.ztereohype.moonphaseinfoplus;

import java.util.function.Function;
import lombok.AllArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class Config {
    private static final Location DEFAULT = Location.BOTTOM_RIGHT;
    private final String header = "MoonPhaseInfoPlus config - You can pick between 'top' and 'bottom_right' for the location of the indicator.";

    private Location currentLocation;

    Path configPath;

    public Config(Path configPath) {
        this.configPath = configPath;
        initialise();
    }

    public void initialise() {
        Properties properties = new Properties();

        if (Files.exists(configPath)) {
            try {
                properties.load(Files.newInputStream(configPath));
            } catch (IOException ignored) {}
        }

        if (!properties.containsKey("location")) {
            properties.setProperty("location", String.valueOf(DEFAULT).toLowerCase());
            save(properties);
        }

        this.currentLocation = Location.fromString(properties.getProperty("location"));
    }

    private void save(Properties properties) {
        File configDir = new File(FilenameUtils.getPath(configPath.toString()));
        boolean createdDirs = configDir.mkdirs();
        if (createdDirs)
            LogManager.getLogger(MoonPhaseInfoPlusMod.MOD_ID).info("Created config directories.");

        try {
            properties.store(Files.newOutputStream(configPath), header);
        } catch (IOException e) {
            e.printStackTrace();
            LogManager.getLogger(MoonPhaseInfoPlusMod.MOD_ID).error("There was an error trying to save the config. Please contact the developer and send them the log.");
        }
    }

    public int getX(int width) {
        return currentLocation.getX(width);
    }

    public int getY(int height) {
        return currentLocation.getY(height);
    }

    @AllArgsConstructor
    enum Location {
        TOP((width) -> width / 2 - 24 - 1, (height) -> 1),
        BOTTOM_RIGHT((width) -> width - 60, (height) -> height - 25);

        private final Function<Integer, Integer> xFunc;
        private final Function<Integer, Integer> yFunc;

        public int getX(int width) {
            return xFunc.apply(width);
        }

        public int getY(int height) {
            return yFunc.apply(height);
        }

        public static Location fromString(String name) {
            for (Location location : Location.values()) {
                if (name.equals(location.name().toLowerCase())) {
                    return location;
                }
            }
            return Location.BOTTOM_RIGHT;
        }
    }
}
