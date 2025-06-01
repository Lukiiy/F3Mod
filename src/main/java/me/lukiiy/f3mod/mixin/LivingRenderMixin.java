package me.lukiiy.f3mod.mixin;

import me.lukiiy.f3mod.F3Mod;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.entity.LivingEntity;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(LivingEntityRenderer.class)
public class LivingRenderMixin {
    @Inject(method = "method_821", at = @At("HEAD"), cancellable = true)
    private void lukisF3_toggleIDs(LivingEntity d, double e, double f, double par4, CallbackInfo ci) {
        if (!Minecraft.method_2149() || !Keyboard.isKeyDown(F3Mod.keyEntityIDs)) ci.cancel();
    }
}
