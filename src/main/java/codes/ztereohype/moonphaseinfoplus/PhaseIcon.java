package codes.ztereohype.moonphaseinfoplus;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public class PhaseIcon {
    private static final Identifier INVENTORY = new Identifier("textures/gui/container/inventory.png");
    private static final Identifier ICONS = new Identifier(MoonPhaseInfoPlusMod.MOD_ID, "textures/environment/moon_phases_icons.png");

    private static final Formatting[] FULLNESS_COLOR = new Formatting[]
            {Formatting.RED, Formatting.GOLD, Formatting.YELLOW, Formatting.GREEN, Formatting.DARK_GREEN};

    void drawPhaseIcon(DrawContext drawContext) {
        MinecraftClient mc = MinecraftClient.getInstance();
        int windowWidth = mc.getWindow().getScaledWidth();
        int windowHeight = mc.getWindow().getScaledHeight();

        if (mc.options.debugEnabled) {
            RenderSystem.setShaderColor(1f, 1f, 1f, 0.25f);
        }

        int a = 24;
//        int x = windowWidth / 2 - a - 1;
//        int y = 1;
        int x = MoonPhaseInfoPlusMod.CONFIG.getX(windowWidth);
        int y = MoonPhaseInfoPlusMod.CONFIG.getY(windowHeight);
        drawContext.drawTexture(INVENTORY, x, y, 400, 141, 166, a, a, 256, 256);

        int a2 = 18;
        int d = a / 2 - a2 / 2;
        x += d;
        y += d;

        int phase = mc.world.getMoonPhase();
        int u = ((4 - phase % 4) % 4) * a2; //{0, 3, 2, 1, 0, 3, 2, 1}[phase] * side length
        int v = phase >= 1 && phase <= 4 ? a2 : 0;

        RenderSystem.enableBlend();
        drawContext.drawTexture(ICONS, x, y, 400, (float)u, (float)v, a2, a2, 72, 36);
        RenderSystem.disableBlend();

        //fullness
        int transparencyMask = mc.options.debugEnabled ? 0x50FFFFFF : 0xFFFFFFFF;
        int sizePercent = (int)(mc.world.getMoonSize() * 100f);
        drawContext.drawTextWithShadow(mc.textRenderer, String.format("%s%d%%%s",
                FULLNESS_COLOR[sizePercent / 25],
                sizePercent,
                phase <= 3 ? Formatting.RED + "↓" : Formatting.DARK_GREEN + "↑"),
                x + a + 1, y, transparencyMask);

        //time left
        long daySecondsLeft = (24000 - mc.world.getTimeOfDay() % 24000) / 20;
        long dayMinutesLeft = daySecondsLeft / 60;
        boolean atLeast60s = daySecondsLeft >= 60;
        //todo: figure out why translucent text is not working
        drawContext.drawTextWithShadow(mc.textRenderer, Formatting.DARK_AQUA + (atLeast60s ? dayMinutesLeft + "min" : daySecondsLeft + "s"),
                x + width + 1, y + mc.textRenderer.fontHeight, transparencyMask);

        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
    }
}
