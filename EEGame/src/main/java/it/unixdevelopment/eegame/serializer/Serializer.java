package it.unixdevelopment.eegame.serializer;

import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class Serializer {

    public static <T> byte[] serialize(T object) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            BukkitObjectOutputStream boos = new BukkitObjectOutputStream(baos);

            boos.writeObject(object);

            boos.close();

            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static <T> T deserialize(byte[] bytes) {
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            BukkitObjectInputStream bois = new BukkitObjectInputStream(bais);

            T object = (T) bois.readObject();

            bois.close();

            return object;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}