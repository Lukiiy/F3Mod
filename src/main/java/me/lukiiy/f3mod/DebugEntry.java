package me.lukiiy.f3mod;

import net.minecraft.client.entity.mob.player.ClientPlayerEntity;
import net.minecraft.world.World;

import java.util.List;

public interface DebugEntry {
    F3Mod.Section getSection();

    List<String> provide(ClientPlayerEntity player, World world);

    default Padding getPadding() {
        return new Padding(0, 0);
    }
}
