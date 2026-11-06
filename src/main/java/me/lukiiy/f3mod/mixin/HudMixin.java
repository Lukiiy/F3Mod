package me.lukiiy.f3mod.mixin;

import me.lukiiy.f3mod.F3Mod;
import me.lukiiy.f3mod.Padding;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.mob.player.ClientPlayerEntity;
import net.minecraft.client.gui.GameGui;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.render.TextRenderer;
import net.minecraft.client.render.Window;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Environment(EnvType.CLIENT)
@Mixin(GameGui.class)
public class HudMixin {
    @Shadow private Minecraft minecraft;

    @Unique private final int LINE_HEIGHT = 10;

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/TextRenderer;drawWithShadow(Ljava/lang/String;III)V", ordinal = 4, shift = At.Shift.AFTER))
    private void lukisF3$render(float tickDelta, boolean screenOpen, int mouseX, int mouseY, CallbackInfo ci) {
        GameOptions options = minecraft.options;
        if (!options.debugEnabled) return;

        Window window = new Window(options, minecraft.width, minecraft.height);
        int rightX = window.getWidth() - 2;

        lukisF3$renderSection(F3Mod.Section.LEFT, 2, 64, F3Mod.TextAlignment.LEFT);
        lukisF3$renderSection(F3Mod.Section.RIGHT, rightX, F3Mod.rightCenter ? window.getHeight() / 2 - LINE_HEIGHT : 44, F3Mod.TextAlignment.RIGHT);
        lukisF3$renderSection(F3Mod.Section.CONDITIONAL, rightX, window.getHeight(), F3Mod.TextAlignment.RIGHT);
    }

    @Unique
    private void lukisF3$renderSection(F3Mod.Section section, int startX, int startY, F3Mod.TextAlignment align) {
        ClientPlayerEntity player = minecraft.player;
        int color = section == F3Mod.Section.CONDITIONAL ? 0xFFFF55 : 0xE0E0E0;
        AtomicInteger y = new AtomicInteger(startY);

        if (section == F3Mod.Section.CONDITIONAL) {
            int lines = F3Mod.PROVIDERS.get(section).stream().mapToInt(p -> {
                List<String> data = p.provide(player, player.world);
                return data != null ? data.size() : 0;
            }).sum();

            y.set(startY - (lines * LINE_HEIGHT));
        }

        F3Mod.PROVIDERS.get(section).forEach(provider -> {
            try {
                List<String> data = provider.provide(minecraft.player, player.world);
                if (data == null || data.isEmpty()) return;

                Padding pad = provider.getPadding();
                int renderX = startX + (align == F3Mod.TextAlignment.RIGHT ? -pad.horizontal : pad.horizontal);

                lukisF3$renderText(data, renderX, y.get() + pad.vertical, align, color);

                y.addAndGet(data.size() * 10);
            } catch (Exception e) {
                F3Mod.LOGGER.error("Error in F3 Provider: {}", e.getMessage());
            }
        });
    }

    @Unique
    private void lukisF3$renderText(List<String> data, int startX, int startY, F3Mod.TextAlignment align, int color) {
        TextRenderer render = minecraft.textRenderer;
        int y = startY;

        for (String line : data) {
            int x = align == F3Mod.TextAlignment.RIGHT ? (startX - render.getWidth(line)) : startX;

            render.drawWithShadow(line, x, y, color);
            y += LINE_HEIGHT;
        }
    }

    // Surpress original F3 info

    @ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GameGui;drawString(Lnet/minecraft/client/render/TextRenderer;Ljava/lang/String;III)V", ordinal = 2), index = 1)
    private String lukisF3$suppressX(String text) {
        return "";
    }

    @ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GameGui;drawString(Lnet/minecraft/client/render/TextRenderer;Ljava/lang/String;III)V", ordinal = 3), index = 1)
    private String lukisF3$suppressY(String text) {
        return "";
    }

    @ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GameGui;drawString(Lnet/minecraft/client/render/TextRenderer;Ljava/lang/String;III)V", ordinal = 4), index = 1)
    private String lukisF3$suppressZ(String text) {
        return "";
    }

    @ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GameGui;drawString(Lnet/minecraft/client/render/TextRenderer;Ljava/lang/String;III)V", ordinal = 5), index = 1)
    private String lukisF3$suppressF(String text) {
        return "";
    }
}