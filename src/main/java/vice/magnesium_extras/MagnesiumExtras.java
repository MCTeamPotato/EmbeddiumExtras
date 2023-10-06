package vice.magnesium_extras;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.network.FMLNetworkConstants;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import vice.magnesium_extras.config.EntityListConfig;
import vice.magnesium_extras.config.MagnesiumExtrasConfig;

@Mod(MagnesiumExtras.MODID)
public class MagnesiumExtras
{
    public static final String MODID = "magnesium_extras";
    public static final KeyBinding zoomKey = new KeyBinding("extras.key.zoom", KeyConflictContext.IN_GAME, KeyModifier.NONE, InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_C, "extras.key.category");

    public MagnesiumExtras() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);
        MagnesiumExtrasConfig.loadConfig(FMLPaths.CONFIGDIR.get().resolve("mgrb_extras.toml"));
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, EntityListConfig.ENTITY_LIST_CONFIG, "mgrb_extras_entitylist.toml");
        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST, () -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));
    }

    public void onClientSetup(@NotNull FMLClientSetupEvent event) {
        event.enqueueWork(() -> ClientRegistry.registerKeyBinding(zoomKey));
    }

    @SuppressWarnings("deprecation")
    public static void disableFog() {
        if (!MagnesiumExtrasConfig.enableFog.get() && GL11.glGetInteger(GL11.GL_FOG_MODE) == GL11.GL_LINEAR) RenderSystem.disableFog();
    }
}