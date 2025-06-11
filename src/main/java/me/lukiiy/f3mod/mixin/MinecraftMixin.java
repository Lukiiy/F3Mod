package me.lukiiy.f3mod.mixin;

import me.lukiiy.f3mod.F3Mod;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Inject(method = "method_2111", at = @At("HEAD"), cancellable = true)
    private void lukisF3$debugGraph(long par1, CallbackInfo ci) {
        if (!Keyboard.isKeyDown(F3Mod.keyShowGraph)) ci.cancel();
    }
}
