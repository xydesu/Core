package me.xydesu.core.Utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import me.xydesu.core.Core;
import org.bukkit.Material;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Translation {

    private static Map<String, String> translations = new HashMap<>();

    public static void load() {
        try (InputStream inputStream = Core.getPlugin().getResource("zh_tw.json")) {
            if (inputStream == null) {
                System.out.println("[Core] Could not find zh_tw.json in resources!");
                return;
            }
            try (Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
                Gson gson = new Gson();
                Type type = new TypeToken<Map<String, String>>(){}.getType();
                translations = gson.fromJson(reader, type);
                System.out.println("[Core] Loaded " + translations.size() + " translations.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String get(Material material) {
        if (translations.isEmpty()) {
            load();
        }
        String key = material.name().toLowerCase();
        
        // Try item key first
        String itemKey = "item.minecraft." + key;
        if (translations.containsKey(itemKey)) {
            return translations.get(itemKey);
        }

        // Try block key
        String blockKey = "block.minecraft." + key;
        if (translations.containsKey(blockKey)) {
            return translations.get(blockKey);
        }
        
        // Try entity key (for some items like item frames?)
        String entityKey = "entity.minecraft." + key;
        if (translations.containsKey(entityKey)) {
            return translations.get(entityKey);
        }

        return null;
    }
}
