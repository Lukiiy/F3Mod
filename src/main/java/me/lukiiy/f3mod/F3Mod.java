package me.lukiiy.f3mod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.lwjgl.input.Keyboard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class F3Mod implements ClientModInitializer {
    public static final String MOD_ID = "f3mod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final Map<Section, List<DebugEntry>> PROVIDERS = new EnumMap<>(Section.class);

    static {
        for (Section side : Section.values()) PROVIDERS.put(side, new ArrayList<>());

        registerEntry(new ProvidedF3.Left());
        registerEntry(new ProvidedF3.Conditional());
    }

    public static int keyShowGraph;
    public static int keyEntityIDs;
    public static boolean displaySeed;
    public static boolean multiplayerSeed;
    public static boolean showTime;
    public static boolean slimeChunk;
    public static boolean showDay;
    public static boolean rightCenter;

    public static boolean debugGraph = false;

    @Override
    public void onInitializeClient() {
        Config config = new Config(MOD_ID, "F3");

        config.setIfAbsent("keybind.graph", "LMENU");
        config.setIfAbsent("keybind.entityIDs", "RSHIFT");
        config.setIfAbsent("seed.show", "true");
        config.setIfAbsent("seed.showOnMultiplayer", "false");
        config.setIfAbsent("time.show", "true");
        config.setIfAbsent("slimechunk.show", "true");
        config.setIfAbsent("day.show", "true");
        config.setIfAbsent("right.center", "false");

        keyShowGraph = key("Debug Graph", config.getOrDefault("keybind.graph", "LMENU"));
        keyEntityIDs = key("Show Entity IDs", config.getOrDefault("keybind.entityIDs", "RSHIFT"));
        displaySeed = config.getBoolean("seed.show");
        multiplayerSeed = config.getBoolean("seed.showOnMultiplayer");
        showTime = config.getBoolean("time.show");
        slimeChunk = config.getBoolean("slimechunk.show");
        showDay = config.getBoolean("day.show");
        rightCenter = config.getBoolean("right.center");

        FabricLoader.getInstance().getEntrypoints("f3mod", F3ModAPI.class).forEach(entryPoint -> entryPoint.getEntries().forEach(F3Mod::registerEntry));
    }

    private int key(String action, String key) {
        int code = Keyboard.getKeyIndex(key);
        LOGGER.info("The F3 combination for '{}' has been set to {}!", action, Keyboard.getKeyName(code));

        return code;
    }

    public enum Section {
        LEFT,
        RIGHT,
        CONDITIONAL
    }

    public enum TextAlignment {
        LEFT,
        RIGHT
    }

    public static void registerEntry(DebugEntry entry) {
        PROVIDERS.get(entry.getSection()).add(entry);
    }
}
