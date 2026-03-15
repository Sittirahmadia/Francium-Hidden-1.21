package org.apache.core.u;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import org.apache.core.u.c.DC;
import org.apache.core.u.c.RP;

import java.time.Instant;

public class GUS {

    public static void startFranciumRPC(boolean showSmallImage) {
        if (!DC.start(1127687589940445266L)) {
            System.out.println("NULL");
            return;
        }

        RP presence = new RP();
        presence.setDetails("Playing Francium");
        presence.setLargeImage("francium", "Francium");

        if (showSmallImage) {
            presence.setSmallImage(getPlayerHeadImage(), getPlayerIGN());
        }

        presence.setStart(Instant.now().getEpochSecond());
        DC.setActivity(presence);
    }

    public static String getPlayerHeadImage() {
        MinecraftClient mc = MinecraftClient.getInstance();
        PlayerEntity player = mc.player;
        if (player != null) {
            GameProfile gameProfile = mc.player.getGameProfile();
            String uuid = gameProfile.getId().toString();
            return "https://crafthead.net/avatar/" + uuid;
        }
        return null;
    }

    private static String getPlayerIGN() {
        MinecraftClient mc = MinecraftClient.getInstance();
        PlayerEntity player = mc.player;
        if (player != null) {
            GameProfile gameProfile = player.getGameProfile();
            return gameProfile.getName();
        }
        return null;
    }

    public static void stopRPC() {
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        DC.stop();
    }
}
