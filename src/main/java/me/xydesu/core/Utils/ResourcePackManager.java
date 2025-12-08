    package me.xydesu.core.Utils;

import me.xydesu.core.Core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import java.util.stream.Stream;

public class ResourcePackManager {

    public static void packageResourcePack(Core plugin) {
        // 1. Define Source Directory
        // Priority 1: plugins/Core/ResourcePack
        File sourceDir = new File(plugin.getDataFolder(), "ResourcePack");
        
        // Priority 2: Hardcoded dev path (for user convenience during development)
        if (!sourceDir.exists()) {
            File devDir = new File("c:\\Users\\xy\\IdeaProjects\\Core\\ResourcePack");
            if (devDir.exists()) {
                sourceDir = devDir;
                plugin.getLogger().info("Found ResourcePack in dev path: " + sourceDir.getAbsolutePath());
            }
        }

        if (!sourceDir.exists()) {
            plugin.getLogger().warning("Could not find ResourcePack folder to package! Please create 'ResourcePack' folder in plugin directory.");
            return;
        }

        // 2. Define Target Zip
        // It will be saved in plugins/Core/resourcepack.zip
        File targetZip = new File(plugin.getDataFolder(), "resourcepack.zip");

        // 3. Zip it
        try {
            // Ensure data folder exists
            if (!plugin.getDataFolder().exists()) {
                plugin.getDataFolder().mkdirs();
            }
            
            zipDirectory(sourceDir, targetZip);
            plugin.getLogger().info("Successfully packaged resource pack to: " + targetZip.getAbsolutePath());
            
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to package resource pack!");
            e.printStackTrace();
        }
    }

    private static void zipDirectory(File sourceDir, File targetZip) throws IOException {
        Path sourcePath = sourceDir.toPath();
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(targetZip));
             Stream<Path> paths = Files.walk(sourcePath)) {
            
            paths.filter(path -> !Files.isDirectory(path))
                 .forEach(path -> {
                     // Create zip entry with relative path
                     String zipPath = sourcePath.relativize(path).toString().replace('\\', '/');
                     ZipEntry zipEntry = new ZipEntry(zipPath);
                     try {
                         zos.putNextEntry(zipEntry);
                         Files.copy(path, zos);
                         zos.closeEntry();
                     } catch (IOException e) {
                         e.printStackTrace();
                     }
                 });
        }
    }
}
