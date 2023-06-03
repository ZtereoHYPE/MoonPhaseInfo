package codes.ztereohype.moonphaseinfoplus;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

import java.nio.file.Paths;

public class MoonPhaseInfoPlusMod implements ClientModInitializer {
    public static final String MOD_ID = "moonphaseinfoplus";
    public static final Config CONFIG = new Config(Paths.get("./config/moonphaseinfoplus.config"));

    PhaseIcon icon;

    @Override
    public void onInitializeClient() {
        icon = new PhaseIcon();
        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> icon.drawPhaseIcon(drawContext));
    }
}
