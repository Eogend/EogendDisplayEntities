package fr.lavapower.eogenddisplayentities.storage;

import fr.lavapower.eogenddisplayentities.EogendDisplayEntities;
import fr.lavapower.eogenddisplayentities.entity.DisplayWrapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class EntityStorage {
    private final String dataFolderPath;

    public EntityStorage(EogendDisplayEntities plugin) {
        dataFolderPath = plugin.getDataFolder().getPath();
    }

    public String getNameForEntity(DisplayWrapper wrapper) {
        Path filePath = Path.of(dataFolderPath, "entities", entityToFileName(wrapper));
        if(!Files.exists(filePath))
            return null;
        else {
            try {
                return Files.readString(filePath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void removeEntity(DisplayWrapper wrapper) {
        try {
            Files.createDirectories(Path.of(dataFolderPath, "entities"));
            Files.deleteIfExists(Path.of(dataFolderPath, "entities", entityToFileName(wrapper)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveNameForEntity(String name, DisplayWrapper wrapper) {
        try {
            Files.createDirectories(Path.of(dataFolderPath, "entities"));
            Files.writeString(Path.of(dataFolderPath, "entities", entityToFileName(wrapper)), name);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String entityToFileName(DisplayWrapper wrapper) {
        var location = wrapper.getLocation();
        return wrapper.getType() + "-" + location.getWorld().getName() + "-" + location.getBlockX() + "-" + location.getBlockY() + "-" + location.getBlockZ() + ".txt";
    }
}
