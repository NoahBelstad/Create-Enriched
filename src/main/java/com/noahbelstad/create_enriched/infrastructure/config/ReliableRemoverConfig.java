package com.noahbelstad.create_enriched.infrastructure.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.neoforged.fml.loading.FMLPaths;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;

public class ReliableRemoverConfig {

    public void setupReliableRemoverConfig() {
        copyConfigFile("reliable_remover.json");
        copyConfigFile("reliable_recipes.json");
    }

    private void copyConfigFile(String fileName) {
        Path configPath = FMLPaths.CONFIGDIR.get().resolve(fileName);

        if (!Files.exists(configPath)) {
            try (InputStream in = this.getClass().getResourceAsStream("/" + fileName)) {
                if (in != null) {
                    Files.copy(in, configPath);
                } else {
                    System.err.println("Could not find /" + fileName + " in resources!");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try (InputStream in = this.getClass().getResourceAsStream("/" + fileName);
                 FileReader reader = new FileReader(configPath.toFile())) {

                if (in != null) {
                    JsonObject defaultConfig = JsonParser.parseReader(new InputStreamReader(in)).getAsJsonObject();
                    JsonObject existingConfig = JsonParser.parseReader(reader).getAsJsonObject();
                    boolean fileModified = false;

                    for (String key : defaultConfig.keySet()) {
                        if (defaultConfig.get(key).isJsonArray() && existingConfig.has(key) && existingConfig.get(key).isJsonArray()) {
                            JsonArray defaultArray = defaultConfig.getAsJsonArray(key);
                            JsonArray existingArray = existingConfig.getAsJsonArray(key);

                            for (JsonElement element : defaultArray) {
                                if (!existingArray.contains(element)) {
                                    existingArray.add(element);
                                    fileModified = true;
                                }
                            }
                        }
                    }

                    if (fileModified) {
                        Gson gson = new GsonBuilder().setPrettyPrinting().create();
                        try (FileWriter writer = new FileWriter(configPath.toFile())) {
                            gson.toJson(existingConfig, writer);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}