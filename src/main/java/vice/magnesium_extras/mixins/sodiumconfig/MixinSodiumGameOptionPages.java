package vice.magnesium_extras.mixins.sodiumconfig;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import me.jellysquid.mods.sodium.client.gui.SodiumGameOptionPages;
import me.jellysquid.mods.sodium.client.gui.SodiumGameOptions;
import me.jellysquid.mods.sodium.client.gui.options.Option;
import me.jellysquid.mods.sodium.client.gui.options.OptionGroup;
import me.jellysquid.mods.sodium.client.gui.options.OptionImpact;
import me.jellysquid.mods.sodium.client.gui.options.OptionImpl;
import me.jellysquid.mods.sodium.client.gui.options.control.ControlValueFormatter;
import me.jellysquid.mods.sodium.client.gui.options.control.CyclingControl;
import me.jellysquid.mods.sodium.client.gui.options.control.SliderControl;
import me.jellysquid.mods.sodium.client.gui.options.control.TickBoxControl;
import me.jellysquid.mods.sodium.client.gui.options.storage.SodiumOptionsStorage;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TranslationTextComponent;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import vice.magnesium_extras.config.MagnesiumExtrasConfig;

import java.util.Collection;
import java.util.List;

@Mixin(value = SodiumGameOptionPages.class, remap = false, priority = 10)
public abstract class MixinSodiumGameOptionPages {
    @Shadow @Final private static SodiumOptionsStorage sodiumOpts;


    @ModifyArg(method = "general", at = @At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableList;copyOf(Ljava/util/Collection;)Lcom/google/common/collect/ImmutableList;"))
    private static @NotNull Collection<OptionGroup> insertSetting1(Collection<OptionGroup> elements) {
        List<OptionGroup> groups = new ObjectArrayList<>(elements);
        Option<MagnesiumExtrasConfig.Complexity> displayFps =  OptionImpl.createBuilder(MagnesiumExtrasConfig.Complexity.class, sodiumOpts)
                .setName(I18n.get("extras.display_fps.display.name"))
                .setTooltip(I18n.get("extras.display_fps.display.tooltip"))
                .setControl((option) -> new CyclingControl<>(option, MagnesiumExtrasConfig.Complexity.class, new TranslationTextComponent[] {new TranslationTextComponent("extras.option.off"), new TranslationTextComponent("extras.option.simple"), new TranslationTextComponent("extras.option.advanced")}))
                .setBinding(
                        (opts, value) -> MagnesiumExtrasConfig.fpsCounterMode.set(value.toString()),
                        (opts) -> MagnesiumExtrasConfig.Complexity.valueOf(MagnesiumExtrasConfig.fpsCounterMode.get()))
                .setImpact(OptionImpact.LOW)
                .build();


        Option<Integer> displayFpsPos = OptionImpl.createBuilder(Integer.TYPE, sodiumOpts)
                .setName(I18n.get("extras.display_fps.position.name"))
                .setTooltip(I18n.get("extras.display_fps.position.tooltip"))
                .setControl((option) -> new SliderControl(option, 4, 64, 2, ControlValueFormatter.number()))
                .setImpact(OptionImpact.LOW)
                .setBinding(
                        (opts, value) -> MagnesiumExtrasConfig.fpsCounterPosition.set(value),
                        (opts) -> MagnesiumExtrasConfig.fpsCounterPosition.get())
                .build();

        OptionImpl<SodiumGameOptions, Boolean> displayFpsAlignRight = OptionImpl.createBuilder(Boolean.class, sodiumOpts)
                .setName(I18n.get("extras.display_fps.right_align.name"))
                .setTooltip(I18n.get("extras.display_fps.right_align.tooltip"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> MagnesiumExtrasConfig.fpsCounterAlignRight.set(value),
                        (options) -> MagnesiumExtrasConfig.fpsCounterAlignRight.get())
                .setImpact(OptionImpact.LOW)
                .build();

        groups.add(OptionGroup.createBuilder()
                .add(displayFps)
                .add(displayFpsAlignRight)
                .add(displayFpsPos)
                .build());
        return groups;
    }


    @ModifyArg(method = "quality", at = @At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableList;copyOf(Ljava/util/Collection;)Lcom/google/common/collect/ImmutableList;"))
    private static @NotNull Collection<OptionGroup> insertSetting2(Collection<OptionGroup> elements) {
        List<OptionGroup> groups = new ObjectArrayList<>(elements);
        OptionImpl<SodiumGameOptions, MagnesiumExtrasConfig.Quality> chunkFadeIn = OptionImpl.createBuilder(MagnesiumExtrasConfig.Quality.class, sodiumOpts)
                .setName(I18n.get("extras.fadeinchunks.name"))
                .setTooltip(I18n.get("extras.fadeinchunks.tooltip"))
                .setControl(option -> new CyclingControl<>(option, MagnesiumExtrasConfig.Quality.class, new TranslationTextComponent[] {
                        new TranslationTextComponent("extras.fadeinchunks.ctrl.off"), new TranslationTextComponent("extras.fadeinchunks.ctrl.fast"), new TranslationTextComponent("extras.fadeinchunks.ctrl.fancy") }))
                .setBinding(
                        (opts, value) -> MagnesiumExtrasConfig.fadeInQuality.set(value.toString()),
                        opts -> MagnesiumExtrasConfig.Quality.valueOf(MagnesiumExtrasConfig.fadeInQuality.get()))
                .setImpact(OptionImpact.LOW).build();


        groups.add(OptionGroup.createBuilder()
                .add(chunkFadeIn)
                .build());
        return groups;
    }

    @ModifyArg(method = "performance", at = @At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableList;copyOf(Ljava/util/Collection;)Lcom/google/common/collect/ImmutableList;"))
    private static @NotNull Collection<OptionGroup> insertSetting3(Collection<OptionGroup> elements) {
        List<OptionGroup> groups = new ObjectArrayList<>(elements);

        OptionImpl<SodiumGameOptions, Boolean> enableFog = OptionImpl.createBuilder(Boolean.class, sodiumOpts)
                .setName(I18n.get("extra.enable_fog.name"))
                .setTooltip(I18n.get("extra.enable_fog.tooltip"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> MagnesiumExtrasConfig.enableFog.set(value),
                        (option) -> MagnesiumExtrasConfig.enableFog.get())
                .setImpact(OptionImpact.MEDIUM)
                .build();

        groups.add(OptionGroup.createBuilder().add(enableFog).build());

        OptionImpl<SodiumGameOptions, Boolean> enableDistanceChecks = OptionImpl.createBuilder(Boolean.class, sodiumOpts)
                .setName(I18n.get("extras.enable_max_entity_distance.name"))
                .setTooltip(I18n.get("extras.enable_max_entity_distance.tooltip"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> MagnesiumExtrasConfig.enableDistanceChecks.set(value),
                        (options) -> MagnesiumExtrasConfig.enableDistanceChecks.get())
                .setImpact(OptionImpact.EXTREME)
                .build();

        groups.add(OptionGroup
                .createBuilder()
                .add(enableDistanceChecks)
                .build()
        );

        OptionImpl<SodiumGameOptions, Integer> maxEntityDistance = OptionImpl.createBuilder(Integer.TYPE, sodiumOpts)
                .setName(I18n.get("extras.max_entity_distance.name"))
                .setTooltip(I18n.get("extras.max_entity_distance.tooltip"))
                .setControl((option) -> new SliderControl(option, 16, 192, 8, ControlValueFormatter.number()))
                .setBinding(
                        (options, value) -> MagnesiumExtrasConfig.maxEntityRenderDistanceSquare.set(value * value),
                        (options) ->  Math.toIntExact(Math.round(Math.sqrt(MagnesiumExtrasConfig.maxEntityRenderDistanceSquare.get()))))
                .setImpact(OptionImpact.EXTREME)
                .build();

        OptionImpl<SodiumGameOptions, Integer> maxEntityDistanceVertical = OptionImpl.createBuilder(Integer.TYPE, sodiumOpts)
                .setName(I18n.get("extras.vertical_entity_distance.name"))
                .setTooltip(I18n.get("extras.vertical_entity_distance.tooltip"))
                .setControl((option) -> new SliderControl(option, 16, 64, 4, ControlValueFormatter.number()))
                .setBinding(
                        (options, value) -> MagnesiumExtrasConfig.maxEntityRenderDistanceY.set(value ),
                        (options) -> MagnesiumExtrasConfig.maxEntityRenderDistanceY.get())
                .setImpact(OptionImpact.EXTREME)
                .build();


        groups.add(OptionGroup
                .createBuilder()
                .add(maxEntityDistance)
                .add(maxEntityDistanceVertical)
                .build()
        );

        OptionImpl<SodiumGameOptions, Integer> maxTileEntityDistance = OptionImpl.createBuilder(Integer.TYPE, sodiumOpts)
                .setName(I18n.get("extras.max_tile_distance.name"))
                .setTooltip(I18n.get("extras.max_tile_distance.tooltip"))
                .setControl((option) -> new SliderControl(option, 16, 256, 8, ControlValueFormatter.number()))
                .setBinding(
                        (options, value) -> MagnesiumExtrasConfig.maxTileEntityRenderDistanceSquare.set(value * value),
                        (options) -> Math.toIntExact(Math.round(Math.sqrt(MagnesiumExtrasConfig.maxTileEntityRenderDistanceSquare.get()))))
                .setImpact(OptionImpact.HIGH)
                .build();

        OptionImpl<SodiumGameOptions, Integer> maxTileEntityDistanceVertical = OptionImpl.createBuilder(Integer.TYPE, sodiumOpts)
                .setName(I18n.get("extras.vertical_tile_distance.name"))
                .setTooltip(I18n.get("extras.vertical_tile_distance.tooltip"))
                .setControl((option) -> new SliderControl(option, 16, 64, 4, ControlValueFormatter.number()))
                .setBinding(
                        (options, value) -> MagnesiumExtrasConfig.maxTileEntityRenderDistanceY.set(value),
                        (options) -> MagnesiumExtrasConfig.maxTileEntityRenderDistanceY.get())
                .setImpact(OptionImpact.HIGH)
                .build();

        groups.add(
                OptionGroup.createBuilder()
                        .add(maxTileEntityDistance)
                        .add(maxTileEntityDistanceVertical)
                        .build()
        );

        return groups;
    }
}
