package com.teampotato.embeddiumextras.mixins.embeddiumconfig;

import com.teampotato.embeddiumextras.config.EmbeddiumExtrasConfig;
import com.teampotato.embeddiumextras.features.gpumemleakfix.MemoryCleaner;
import me.jellysquid.mods.sodium.client.gui.SodiumGameOptionPages;
import me.jellysquid.mods.sodium.client.gui.SodiumGameOptions;
import me.jellysquid.mods.sodium.client.gui.options.*;
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
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(value = SodiumGameOptionPages.class, remap = false)
public abstract class MixinSodiumGameOptionPages {
    @Shadow @Final private static SodiumOptionsStorage sodiumOpts;

    @Inject(method = "general", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", remap = false, ordinal = 2, shift = At.Shift.BEFORE), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private static void insertSetting1(CallbackInfoReturnable<OptionPage> cir, List<OptionGroup> groups) {
        Option<EmbeddiumExtrasConfig.Complexity> displayFps =  OptionImpl.createBuilder(EmbeddiumExtrasConfig.Complexity.class, sodiumOpts)
                .setName(I18n.get("extras.display_fps.display.name"))
                .setTooltip(I18n.get("extras.display_fps.display.tooltip"))
                .setControl((option) -> new CyclingControl<>(option, EmbeddiumExtrasConfig.Complexity.class, new TranslationTextComponent[] {new TranslationTextComponent("extras.option.off"), new TranslationTextComponent("extras.option.simple"), new TranslationTextComponent("extras.option.advanced")}))
                .setBinding(
                        (opts, value) -> EmbeddiumExtrasConfig.fpsCounterMode.set(value.toString()),
                        (opts) -> EmbeddiumExtrasConfig.Complexity.valueOf(EmbeddiumExtrasConfig.fpsCounterMode.get()))
                .setImpact(OptionImpact.LOW)
                .build();


        Option<Integer> displayFpsPos = OptionImpl.createBuilder(Integer.TYPE, sodiumOpts)
                .setName(I18n.get("extras.display_fps.position.name"))
                .setTooltip(I18n.get("extras.display_fps.position.tooltip"))
                .setControl((option) -> new SliderControl(option, 4, 64, 2, ControlValueFormatter.number()))
                .setImpact(OptionImpact.LOW)
                .setBinding(
                        (opts, value) -> EmbeddiumExtrasConfig.fpsCounterPosition.set(value),
                        (opts) -> EmbeddiumExtrasConfig.fpsCounterPosition.get())
                .build();

        OptionImpl<SodiumGameOptions, Boolean> displayFpsAlignRight = OptionImpl.createBuilder(Boolean.class, sodiumOpts)
                .setName(I18n.get("extras.display_fps.right_align.name"))
                .setTooltip(I18n.get("extras.display_fps.right_align.tooltip"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> EmbeddiumExtrasConfig.fpsCounterAlignRight.set(value),
                        (options) -> EmbeddiumExtrasConfig.fpsCounterAlignRight.get())
                .setImpact(OptionImpact.LOW)
                .build();

        groups.add(OptionGroup.createBuilder().add(displayFps).add(displayFpsAlignRight).add(displayFpsPos).build());
    }


    @Inject(method = "quality", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", remap = false, ordinal = 2, shift = At.Shift.BEFORE), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private static void insertSetting2(CallbackInfoReturnable<OptionPage> cir, List<OptionGroup> groups) {
        OptionImpl<SodiumGameOptions, EmbeddiumExtrasConfig.Quality> chunkFadeIn = OptionImpl.createBuilder(EmbeddiumExtrasConfig.Quality.class, sodiumOpts)
                .setName(I18n.get("extras.fadeinchunks.name"))
                .setTooltip(I18n.get("extras.fadeinchunks.tooltip"))
                .setControl(option -> new CyclingControl<>(option, EmbeddiumExtrasConfig.Quality.class, new TranslationTextComponent[] {
                        new TranslationTextComponent("extras.fadeinchunks.ctrl.off"), new TranslationTextComponent("extras.fadeinchunks.ctrl.fast"), new TranslationTextComponent("extras.fadeinchunks.ctrl.fancy") }))
                .setBinding(
                        (opts, value) -> EmbeddiumExtrasConfig.fadeInQuality.set(value.toString()),
                        opts -> EmbeddiumExtrasConfig.Quality.valueOf(EmbeddiumExtrasConfig.fadeInQuality.get()))
                .setImpact(OptionImpact.LOW).build();


        groups.add(OptionGroup.createBuilder()
                .add(chunkFadeIn)
                .build());
    }

    @Inject(method = "performance", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", remap = false, ordinal = 0, shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private static void insertSetting3(CallbackInfoReturnable<OptionPage> cir, @NotNull List<OptionGroup> groups) {
        OptionImpl<SodiumGameOptions, Boolean> hideJei = OptionImpl.createBuilder(Boolean.class, sodiumOpts)
                .setName(I18n.get("extras.jei.name"))
                .setTooltip(I18n.get("extras.jei.tooltip"))
                .setControl(TickBoxControl::new)
                .setBinding((sodiumGameOptions, aBoolean) -> EmbeddiumExtrasConfig.hideJeiItems.set(aBoolean), sodiumGameOptions -> EmbeddiumExtrasConfig.hideJeiItems.get())
                .setImpact(OptionImpact.MEDIUM)
                .setFlags(new OptionFlag[]{OptionFlag.REQUIRES_RENDERER_RELOAD})
                .build();

        groups.add(OptionGroup.createBuilder().add(hideJei).build());

        OptionImpl<SodiumGameOptions, Boolean> fastchest = OptionImpl.createBuilder(Boolean.class, sodiumOpts)
                .setName(I18n.get("extras.fastchest.name"))
                .setTooltip(I18n.get("extras.fastchest.tooltip"))
                .setControl(TickBoxControl::new)
                .setBinding((sodiumGameOptions, aBoolean) -> EmbeddiumExtrasConfig.enableFastChest.set(aBoolean), sodiumGameOptions -> EmbeddiumExtrasConfig.enableFastChest.get())
                .setImpact(OptionImpact.MEDIUM)
                .setFlags(new OptionFlag[]{OptionFlag.REQUIRES_RENDERER_RELOAD})
                .build();

        groups.add(OptionGroup.createBuilder().add(fastchest).build());

        OptionImpl<SodiumGameOptions, Boolean> fixGPUMemoryLeak = OptionImpl.createBuilder(Boolean.class, sodiumOpts)
                .setName(I18n.get("extras.fix_gpu_memory_leak.name"))
                .setTooltip(I18n.get("extras.fix_gpu_memory_leak.tooltip"))
                .setControl(TickBoxControl::new)
                .setBinding((sodiumGameOptions, aBoolean) -> {
                    EmbeddiumExtrasConfig.fixGPUMemoryLeak.set(aBoolean);
                    MemoryCleaner.shouldFixGPUMemoryLeak = aBoolean;
                    MemoryCleaner.IDS_CONTAINERS.clear();
                }, sodiumGameOptions -> EmbeddiumExtrasConfig.fixGPUMemoryLeak.get())
                .setImpact(OptionImpact.VARIES)
                .build();

        MemoryCleaner.shouldFixGPUMemoryLeak = EmbeddiumExtrasConfig.fixGPUMemoryLeak.get();
        groups.add(OptionGroup.createBuilder().add(fixGPUMemoryLeak).build());

        OptionImpl<SodiumGameOptions, Boolean> enableDistanceChecks = OptionImpl.createBuilder(Boolean.class, sodiumOpts)
                .setName(I18n.get("extras.enable_max_entity_distance.name"))
                .setTooltip(I18n.get("extras.enable_max_entity_distance.tooltip"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> EmbeddiumExtrasConfig.enableDistanceChecks.set(value),
                        (options) -> EmbeddiumExtrasConfig.enableDistanceChecks.get())
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
                        (options, value) -> EmbeddiumExtrasConfig.maxEntityRenderDistanceSquare.set(value * value),
                        (options) ->  Math.toIntExact(Math.round(Math.sqrt(EmbeddiumExtrasConfig.maxEntityRenderDistanceSquare.get()))))
                .setImpact(OptionImpact.EXTREME)
                .build();

        OptionImpl<SodiumGameOptions, Integer> maxEntityDistanceVertical = OptionImpl.createBuilder(Integer.TYPE, sodiumOpts)
                .setName(I18n.get("extras.vertical_entity_distance.name"))
                .setTooltip(I18n.get("extras.vertical_entity_distance.tooltip"))
                .setControl((option) -> new SliderControl(option, 16, 64, 4, ControlValueFormatter.number()))
                .setBinding(
                        (options, value) -> EmbeddiumExtrasConfig.maxEntityRenderDistanceY.set(value ),
                        (options) -> EmbeddiumExtrasConfig.maxEntityRenderDistanceY.get())
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
                        (options, value) -> EmbeddiumExtrasConfig.maxTileEntityRenderDistanceSquare.set(value * value),
                        (options) -> Math.toIntExact(Math.round(Math.sqrt(EmbeddiumExtrasConfig.maxTileEntityRenderDistanceSquare.get()))))
                .setImpact(OptionImpact.HIGH)
                .build();

        OptionImpl<SodiumGameOptions, Integer> maxTileEntityDistanceVertical = OptionImpl.createBuilder(Integer.TYPE, sodiumOpts)
                .setName(I18n.get("extras.vertical_tile_distance.name"))
                .setTooltip(I18n.get("extras.vertical_tile_distance.tooltip"))
                .setControl((option) -> new SliderControl(option, 16, 64, 4, ControlValueFormatter.number()))
                .setBinding(
                        (options, value) -> EmbeddiumExtrasConfig.maxTileEntityRenderDistanceY.set(value),
                        (options) -> EmbeddiumExtrasConfig.maxTileEntityRenderDistanceY.get())
                .setImpact(OptionImpact.HIGH)
                .build();

        groups.add(
                OptionGroup.createBuilder()
                        .add(maxTileEntityDistance)
                        .add(maxTileEntityDistanceVertical)
                        .build()
        );
    }
}
