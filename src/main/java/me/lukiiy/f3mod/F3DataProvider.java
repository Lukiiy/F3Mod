package me.lukiiy.f3mod;

import net.minecraft.entity.player.ClientPlayerEntity;
import net.minecraft.world.World;

import java.util.List;

@FunctionalInterface
public interface F3DataProvider {
    List<String> provide(ClientPlayerEntity player, World world);

    default Padding getPadding() {
        return new Padding(0, 0);
    }
}
