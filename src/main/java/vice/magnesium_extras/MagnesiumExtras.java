package vice.magnesium_extras;

import me.jellysquid.mods.sodium.client.gui.SodiumGameOptionPages;
import me.jellysquid.mods.sodium.client.gui.options.storage.SodiumOptionsStorage;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.network.FMLNetworkConstants;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import vice.magnesium_extras.config.EntityListConfig;
import vice.magnesium_extras.config.MagnesiumExtrasConfig;

import java.lang.reflect.Field;

@Mod(MagnesiumExtras.MODID)
public class MagnesiumExtras
{
    public static final String MODID = "magnesium_extras";
    public static final Logger LOGGER = LogManager.getLogger();

    public MagnesiumExtras() {
        MinecraftForge.EVENT_BUS.register(this);

        MagnesiumExtrasConfig.loadConfig(FMLPaths.CONFIGDIR.get().resolve("mgrb_extras.toml"));
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, EntityListConfig.ENTITY_LIST_CONFIG, "mgrb_extras_entitylist.toml");

        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST, () -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));

        try {
            final Field sodiumOptsField = SodiumGameOptionPages.class.getDeclaredField("sodiumOpts");
            sodiumOptsField.setAccessible(true);
            SodiumOptionsStorage sodiumOpts = (SodiumOptionsStorage) sodiumOptsField.get(null);
            sodiumOpts.save();
        } catch (Throwable t) {
            LOGGER.error("Could not retrieve sodiumOptsField");
        }
    }
}