package codes.ztereohype.moonphaseinfoplus;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public class PhaseIcon {
    private static final Identifier INVENTORY = new Identifier("minecraft:textures/gui/sprites/hud/effect_background.png");
    private static final Identifier ICONS = new Identifier(MoonPhaseInfoPlusMod.MOD_ID, "textures/environment/moon_phases_icons.png");

    private static final Formatting[] FULLNESS_COLOR = new Formatting[]
            {Formatting.RED, Formatting.GOLD, Formatting.YELLOW, Formatting.GREEN, Formatting.DARK_GREEN};

    void drawPhaseIcon(DrawContext drawContext) {
        MinecraftClient mc = MinecraftClient.getInstance();
        int windowWidth = mc.getWindow().getScaledWidth();
        int windowHeight = mc.getWindow().getScaledHeight();

        if (mc.getDebugHud().shouldShowDebugHud()) {
            RenderSystem.setShaderColor(1f, 1f, 1f, 0.25f);
        }

        int width = 24;
        int x = MoonPhaseInfoPlusMod.CONFIG.getX(windowWidth);
        int y = MoonPhaseInfoPlusMod.CONFIG.getY(windowHeight);
        drawContext.drawTexture(INVENTORY, x, y, 0, 0, 0, width, width, 24, 24);

        int moonWidth = 18;
        int d = width / 2 - moonWidth / 2;
        x += d;
        y += d;

        int phase = mc.world.getMoonPhase();
        int u = ((4 - phase % 4) % 4) * moonWidth; //{0, 3, 2, 1, 0, 3, 2, 1}[phase] * side length
        int v = phase >= 1 && phase <= 4 ? moonWidth : 0;

        RenderSystem.enableBlend();
        drawContext.drawTexture(ICONS, x, y, 400, (float)u, (float)v, moonWidth, moonWidth, 72, 36);
        RenderSystem.disableBlend();

        //fullness
        int transparencyMask = mc.getDebugHud().shouldShowDebugHud() ? 0xFEFFFFFF : 0xFFFFFFFF;
        int sizePercent = (int)(mc.world.getMoonSize() * 100f);
        drawContext.drawTextWithShadow(mc.textRenderer, String.format("%s%d%%%s",
                FULLNESS_COLOR[sizePercent / 25],
                sizePercent,
                phase <= 3 ? Formatting.RED + "↓" : Formatting.DARK_GREEN + "↑"),
                x + width + 1, y, transparencyMask);

        //time left
        long daySecondsLeft = (24000 - mc.world.getTimeOfDay() % 24000) / 20;
        long dayMinutesLeft = daySecondsLeft / 60;
        boolean atLeast60s = daySecondsLeft >= 60;

        drawContext.drawTextWithShadow(mc.textRenderer, Formatting.DARK_AQUA + (atLeast60s ? dayMinutesLeft + "min" : daySecondsLeft + "s"),
                x + width + 1, y + mc.textRenderer.fontHeight, transparencyMask);

        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
    }
}
