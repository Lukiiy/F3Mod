package me.lukiiy.f3mod;

import net.minecraft.class_43;
import net.minecraft.class_519;
import net.minecraft.entity.player.ClientPlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldProperties;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LukisF3 {
    public static class Left implements F3DataProvider {
        @Override
        public List<String> provide(ClientPlayerEntity player, World world) {
            List<String> display = new ArrayList<>();
            WorldProperties props = world.method_262();
            class_519 chunkManager = world.method_1781();

            int blockX = (int) Math.floor(player.x);
            int blockY = (int) Math.floor(player.y);
            int blockZ = (int) Math.floor(player.z);

            class_43 chunk = world.method_199(blockX, blockZ);
            String isSlimeChunk = F3Mod.slimeChunk ? "Slime: " + (chunk.method_863(987234911L).nextInt(10) == 0 ? "Yes" : "No") : "";
            int facing = MathHelper.floot(player.yaw * 4f / 360f + .5) & 3;
            long worldTime = props.getTime();

            if (F3Mod.displaySeed && (!player.world.isRemote || F3Mod.multiplayerSeed)) display.add("Seed: " + world.getSeed());
            display.add("XYZ: " + String.format(Locale.US, "%.3f %.3f %.3f", player.x, player.y, player.z));
            display.add("Chunk: " + String.format("%s %s %s", blockX >> 4, blockY >> 4, blockZ >> 4) + " " + isSlimeChunk);
            display.add("Facing: " + getDirection(facing) + " (" + facing + ")");
            display.add("Biome: " + chunkManager.method_1787(blockX, blockZ).field_888);
            display.add("Light: " + world.method_252(blockX, blockY, blockZ) + " (sky)");
            if (F3Mod.showTime) display.add("Time: " + coolFormattedTime(worldTime));
            display.add("Day: " + (worldTime / 24000L + 1));

            return display;
        }
    }

    public static class Conditional implements F3DataProvider {
        @Override
        public List<String> provide(ClientPlayerEntity player, World world) {
            List<String> display = new ArrayList<>();
            if (Keyboard.isKeyDown(F3Mod.keyShowGraph)) display.add("Showing Debug Graph");
            if (Keyboard.isKeyDown(F3Mod.keyEntityIDs)) display.add("Showing Entity IDs");
            return display;
        }
    }

    private static String getDirection(int face) {
        String[] directions = {"south [+Z]", "west [-X]", "north [-Z]", "east [+X]"};
        return face >= 0 && face < directions.length ? directions[face] : "???";
    }

    private static String coolFormattedTime(long time) {
        long dayTime = time % 24000L;
        long timeOfDay = (dayTime + 6000) % 24000;

        return String.format("%02d:%02d", (int) (timeOfDay / 1000), (int) ((timeOfDay % 1000) * 60 / 1000));
    }
}
