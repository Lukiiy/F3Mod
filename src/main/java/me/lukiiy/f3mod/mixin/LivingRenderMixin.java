package me.lukiiy.f3mod.mixin;

import me.lukiiy.f3mod.F3Mod;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.entity.MobRenderer;
import net.minecraft.entity.mob.MobEntity;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(MobRenderer.class)
public class LivingRenderMixin<T extends MobEntity> {
    @Inject(method = "renderNameTag(Lnet/minecraft/entity/mob/MobEntity;DDD)V", at = @At("HEAD"), cancellable = true)
    private void lukisF3$toggleIDs(T entity, double dx, double dy, double dz, CallbackInfo ci) {
        if (!Minecraft.isDebugProfilerEnabled() || !Keyboard.isKeyDown(F3Mod.keyEntityIDs)) ci.cancel();
    }
}
