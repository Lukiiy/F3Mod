package me.lukiiy.f3mod.mixin;

import me.lukiiy.f3mod.F3Mod;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Shadow public Screen currentScreen;

    @Inject(method = "method_2111", at = @At("HEAD"), cancellable = true)
    private void lukisF3$debugGraph(long par1, CallbackInfo ci) {
        boolean show = currentScreen == null && Keyboard.isKeyDown(F3Mod.keyShowGraph);

        F3Mod.debugGraph = show;
        if (!show) ci.cancel();
    }
}
