package me.lukiiy.f3mod.mixin;

import me.lukiiy.f3mod.F3Mod;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_564;
import net.minecraft.client.Minecraft;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.option.GameOptions;
import net.minecraft.entity.player.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
@Mixin(InGameHud.class)
public class HudMixin {
    @Shadow private Minecraft minecraft;
    @Unique private class_564 scaledRes;

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/font/TextRenderer;drawWithShadow(Ljava/lang/String;III)V", shift = At.Shift.AFTER))
    private void lukisF3$render(float bl, boolean i, int j, int par4, CallbackInfo ci) {
        GameOptions options = minecraft.options;
        if (!options.debugHud) return;

        scaledRes = new class_564(options, minecraft.displayWidth, minecraft.displayHeight);

        lukisF3$renderSection(F3Mod.Section.LEFT, 2, 64, F3Mod.TextAlignment.LEFT);
        lukisF3$renderSection(F3Mod.Section.CONDITIONAL, -1, scaledRes.method_1858(), F3Mod.TextAlignment.RIGHT);
    }

    @Unique
    private void lukisF3$renderSection(F3Mod.Section section, int startX, int startY, F3Mod.TextAlignment align) {
        List<String> data = new ArrayList<>();
        ClientPlayerEntity player = minecraft.player;
        int color = 0xE0E0E0;

        F3Mod.PROVIDERS.get(section).forEach(provider -> {
            try {
                List<String> extra = provider.provide(minecraft.player, player.world);
                if (extra != null) data.addAll(extra);
            } catch (Exception e) {
                F3Mod.LOGGER.error("Error in F3 Provider: {}", e.getMessage());
            }
        });

        if (data.isEmpty()) return;

        if (section == F3Mod.Section.CONDITIONAL) {
            startY = startY - (data.size() * 10);
            color = 0xFFFF55;
        }

        lukisF3$renderText(data, startX, startY, align, color);
    }

    @Unique
    private void lukisF3$renderText(List<String> data, int startX, int startY, F3Mod.TextAlignment align, int color) {
        TextRenderer textRenderer = minecraft.textRenderer;
        int lineHeight = 10;
        int y = startY;

        for (String line : data) {
            int x = align == F3Mod.TextAlignment.RIGHT ? (scaledRes.method_1857() - textRenderer.getWidth(line) - 2) : startX;

            textRenderer.drawWithShadow(line, x, y, color);
            y += lineHeight;
        }
    }

    // Surpress original F3 info

    @ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;drawStringWithShadow(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V", ordinal = 2), index = 1)
    private String lukisF3$suppressX(String par2) {
        return "";
    }

    @ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;drawStringWithShadow(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V", ordinal = 3), index = 1)
    private String lukisF3$suppressY(String par2) {
        return "";
    }

    @ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;drawStringWithShadow(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V", ordinal = 4), index = 1)
    private String lukisF3$suppressZ(String par2) {
        return "";
    }

    @ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;drawStringWithShadow(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V", ordinal = 5), index = 1)
    private String lukisF3$surpressF(String par2) {
        return "";
    }
}
