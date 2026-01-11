package com.example.crosshairhider;

import net.fabricmc.loader.api.FabricLoader;
import java.io.*;
import java.util.Properties;

public class ModConfig {
    private static final File FILE = FabricLoader.getInstance().getConfigDir().resolve("crosshairhider.properties").toFile();
    private static final Properties props = new Properties();

    public static boolean enabled = true;
    public static boolean detectEntities = true;
    public static boolean detectBlocks = true;
    public static CrosshairHiderClient.Mode mode = CrosshairHiderClient.Mode.ALL_LIVING;
    public static boolean filterBlocks = true;
    public static boolean showCooldown = false;

    public static void load() {
        if (!FILE.exists()) { save(); return; }
        try (InputStream in = new FileInputStream(FILE)) {
            props.load(in);
            enabled = Boolean.parseBoolean(props.getProperty("enabled", "true"));
            detectEntities = Boolean.parseBoolean(props.getProperty("detectEntities", "true"));
            detectBlocks = Boolean.parseBoolean(props.getProperty("detectBlocks", "true"));
            mode = CrosshairHiderClient.Mode.valueOf(props.getProperty("mode", "ALL_LIVING"));
            filterBlocks = Boolean.parseBoolean(props.getProperty("filterBlocks", "true"));
            showCooldown = Boolean.parseBoolean(props.getProperty("showCooldown", "false"));
        } catch (Exception e) { e.printStackTrace(); }
    }

    public static void save() {
        props.setProperty("enabled", String.valueOf(enabled));
        props.setProperty("detectEntities", String.valueOf(detectEntities));
        props.setProperty("detectBlocks", String.valueOf(detectBlocks));
        props.setProperty("mode", mode.name());
        props.setProperty("filterBlocks", String.valueOf(filterBlocks));
        props.setProperty("showCooldown", String.valueOf(showCooldown));
        try (OutputStream out = new FileOutputStream(FILE)) {
            props.store(out, "Crosshair Hider Config");
        } catch (Exception e) { e.printStackTrace(); }
    }
}