package com.utilities;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

public class EntityCreator {

    /**
     *
     * @param entityType The type of entity that you want to create
     * @param location The location where you want the entity.
     * @return Entity
     */
    public static Entity create(EntityType entityType, Location location) {
        try {
            // We get the craftworld class with nms so it can be used in multiple versions
            Class<?> craftWorldClass = getNMSClass("org.bukkit.craftbukkit.", "CraftWorld");

            // Cast the bukkit world to the craftworld
            Object craftWorldObject = craftWorldClass.cast(location.getWorld());

            // Create variable with the method that creates the entity
            // https://hub.spigotmc.org/stash/projects/SPIGOT/repos/craftbukkit/browse/src/main/java/org/bukkit/craftbukkit/CraftWorld.java#896
            Method createEntityMethod = craftWorldObject.getClass().getMethod("createEntity", Location.class, Class.class);

            // Attempt to invoke the method that creates the entity itself. This returns a net.minecraft.server entity
            Object entity = createEntityMethod.invoke(craftWorldObject, location, entityType.getEntityClass());

            // finally we run the getBukkitEntity method in the entity class to get a usable object
            return (Entity) entity.getClass().getMethod("getBukkitEntity").invoke(entity);
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException exception) {
            exception.printStackTrace();
        }


        // If something went wrong we just return null
        return null;
    }

    /**
     *
     * @param prefix What comes before the version number
     * @param nmsClassString What comes after the version number
     * @return Class The class that you tried to access
     * @throws ClassNotFoundException throws an exception if the class it not found
     */
    private static Class<?> getNMSClass(String prefix, String nmsClassString) throws ClassNotFoundException {
        // Getting the version by splitting the package
       String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + ".";

        // Combining the prefix + version + nmsClassString for the full class path
        String name = prefix + version + nmsClassString;
        return Class.forName(name);
    }
}
