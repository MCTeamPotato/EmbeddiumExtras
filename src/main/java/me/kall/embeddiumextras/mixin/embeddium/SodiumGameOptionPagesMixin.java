package me.kall.embeddiumextras.mixin.embeddium;

import com.llamalad7.mixinextras.sugar.Local;
import me.jellysquid.mods.sodium.client.gui.SodiumGameOptionPages;
import me.jellysquid.mods.sodium.client.gui.SodiumGameOptions;
import me.jellysquid.mods.sodium.client.gui.options.*;
import me.jellysquid.mods.sodium.client.gui.options.control.ControlValueFormatter;
import me.jellysquid.mods.sodium.client.gui.options.control.SliderControl;
import me.jellysquid.mods.sodium.client.gui.options.control.TickBoxControl;
import me.jellysquid.mods.sodium.client.gui.options.storage.SodiumOptionsStorage;
import me.kall.embeddiumextras.features.fastchest.FastChestInformer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.language.I18n;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

import static me.kall.embeddiumextras.config.ExtrasConfig.*;

@Mixin(value = SodiumGameOptionPages.class, remap = false)
public abstract class SodiumGameOptionPagesMixin {
    @Shadow @Final private static SodiumOptionsStorage sodiumOpts;

    @Inject(method = "general", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", remap = false, ordinal = 2))
    private static void injectGeneral(CallbackInfoReturnable<OptionPage> cir, @Local(ordinal = 0) @NotNull List<OptionGroup> groups) {
        OptionImpl<SodiumGameOptions, Boolean> zoom = OptionImpl.createBuilder(Boolean.class, sodiumOpts)
                .setName(I18n.get("extras.zoom"))
                .setTooltip(I18n.get("extras.none"))
                .setControl(TickBoxControl::new)
                .setBinding((sodiumGameOptions, value) -> ZOOM.set(value), sodiumGameOptions -> ZOOM.get())
                .setImpact(OptionImpact.LOW)
                .build();

        Option<Integer> maxZoomScale = OptionImpl.createBuilder(Integer.TYPE, sodiumOpts)
                .setName(I18n.get("extras.zoom.max_zoom_scale"))
                .setTooltip(I18n.get("extras.zoom.max_zoom_scale.tooltip"))
                .setControl(option -> new SliderControl(option, 4, 64, 2, ControlValueFormatter.number()))
                .setBinding((sodiumGameOptions, integer) -> {}, sodiumGameOptions -> 60)
                .setImpact(OptionImpact.LOW)
                .build();

        groups.add(OptionGroup.createBuilder().add(zoom).add(maxZoomScale).build());

        OptionImpl<SodiumGameOptions, Boolean> note = OptionImpl.createBuilder(Boolean.class, sodiumOpts)
                .setName(I18n.get("extras.note.name"))
                .setTooltip(I18n.get("extras.none"))
                .setControl(TickBoxControl::new)
                .setBinding((sodiumGameOptions, value) -> NOTE.set(value), sodiumGameOptions -> NOTE.get())
                .setImpact(OptionImpact.LOW)
                .build();

        groups.add(OptionGroup.createBuilder().add(note).build());
    }

    @Inject(method = "performance", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", shift = At.Shift.AFTER, remap = false))
    private static void injectPerformance(CallbackInfoReturnable<OptionPage> cir, @Local(ordinal = 0) @NotNull List<OptionGroup> groups) {
        OptionImpl<SodiumGameOptions, Boolean> fastChest = OptionImpl.createBuilder(Boolean.class, sodiumOpts)
                .setName(I18n.get("extras.fast_chest"))
                .setTooltip(I18n.get("extras.fast_chest.tooltip"))
                .setControl(TickBoxControl::new)
                .setBinding((sodiumGameOptions, value) -> {
                    FAST_CHEST.set(value);
                    LocalPlayer player = Minecraft.getInstance().player;
                    if (player != null) {
                        player.displayClientMessage(value ? FastChestInformer.ENABLE : FastChestInformer.DISABLE, false);
                    } else {
                        FastChestInformer.shouldInform = true;
                        FastChestInformer.informType = value ? "true" : "false";
                    }
                }, sodiumGameOptions -> FAST_CHEST.get())
                .setImpact(OptionImpact.VARIES)
                .build();

        groups.add(OptionGroup.createBuilder().add(fastChest).build());

        OptionImpl<SodiumGameOptions, Boolean> entityCheck = OptionImpl.createBuilder(Boolean.class, sodiumOpts)
                .setName(I18n.get("extras.dist.entity.check"))
                .setTooltip(I18n.get("extras.none"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> ENABLE_ENTITY_DIST_CHECK.set(value),
                        (options) -> ENABLE_ENTITY_DIST_CHECK.get())
                .setImpact(OptionImpact.EXTREME)
                .build();

        groups.add(OptionGroup.createBuilder().add(entityCheck).build());

        OptionImpl<SodiumGameOptions, Integer> maxEntityDistSqr = OptionImpl.createBuilder(Integer.TYPE, sodiumOpts)
                .setName(I18n.get("extras.dist.entity.dist_sqr"))
                .setTooltip(I18n.get("extras.none"))
                .setControl((option) -> new SliderControl(option, 16, 128, 8, ControlValueFormatter.number()))
                .setBinding(
                        (options, value) -> ENTITY_MAX_RENDERABLE_DIST_SQR.set(value * value),
                        (options) ->  Math.toIntExact(Math.round(Math.sqrt(ENTITY_MAX_RENDERABLE_DIST_SQR.get()))))
                .setImpact(OptionImpact.EXTREME)
                .build();

        OptionImpl<SodiumGameOptions, Integer> maxEntityHeight = OptionImpl.createBuilder(Integer.TYPE, sodiumOpts)
                .setName(I18n.get("extras.dist.entity.height"))
                .setTooltip(I18n.get("extras.none"))
                .setControl((option) -> new SliderControl(option, 16, 64, 4, ControlValueFormatter.number()))
                .setBinding(
                        (options, value) -> ENTITY_MAX_RENDERABLE_HEIGHT.set(value),
                        (options) -> ENTITY_MAX_RENDERABLE_HEIGHT.get())
                .setImpact(OptionImpact.EXTREME)
                .build();

        groups.add(OptionGroup.createBuilder().add(maxEntityHeight).add(maxEntityDistSqr).build());

        OptionImpl<SodiumGameOptions, Boolean> tileCheck = OptionImpl.createBuilder(Boolean.class, sodiumOpts)
                .setName(I18n.get("extras.dist.tile.check"))
                .setTooltip(I18n.get("extras.none"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> ENABLE_TILE_ENTITY_DIST_CHECK.set(value),
                        (options) -> ENABLE_TILE_ENTITY_DIST_CHECK.get())
                .setImpact(OptionImpact.EXTREME)
                .build();

        groups.add(OptionGroup.createBuilder().add(tileCheck).build());

        OptionImpl<SodiumGameOptions, Integer> maxTileDistSqr = OptionImpl.createBuilder(Integer.TYPE, sodiumOpts)
                .setName(I18n.get("extras.dist.tile.dist_sqr"))
                .setTooltip(I18n.get("extras.none"))
                .setControl((option) -> new SliderControl(option, 16, 256, 8, ControlValueFormatter.number()))
                .setBinding(
                        (options, value) -> TILE_MAX_RENDERABLE_DIST_SQR.set(value * value),
                        (options) -> Math.toIntExact(Math.round(Math.sqrt(TILE_MAX_RENDERABLE_DIST_SQR.get()))))
                .setImpact(OptionImpact.HIGH)
                .build();

        OptionImpl<SodiumGameOptions, Integer> maxTileHeight = OptionImpl.createBuilder(Integer.TYPE, sodiumOpts)
                .setName(I18n.get("extras.dist.tile.height"))
                .setTooltip(I18n.get("extras.none"))
                .setControl((option) -> new SliderControl(option, 16, 64, 4, ControlValueFormatter.number()))
                .setBinding(
                        (options, value) -> TILE_MAX_RENDERABLE_HEIGHT.set(value),
                        (options) -> TILE_MAX_RENDERABLE_HEIGHT.get())
                .setImpact(OptionImpact.HIGH)
                .build();

        groups.add(OptionGroup.createBuilder().add(maxTileDistSqr).add(maxTileHeight).build());

        OptionImpl<SodiumGameOptions, Boolean> hideJeiItems = OptionImpl.createBuilder(Boolean.class, sodiumOpts)
                .setName(I18n.get("extras.hide_jei_items"))
                .setTooltip(I18n.get("extras.hide_jei_items.tooltip"))
                .setControl(TickBoxControl::new)
                .setBinding((sodiumGameOptions, value) -> HIDE_JEI_ITEMS.set(value), options -> HIDE_JEI_ITEMS.get())
                .setImpact(OptionImpact.MEDIUM)
                .build();

        groups.add(OptionGroup.createBuilder().add(hideJeiItems).build());
    }

    @Inject(method = "advanced", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", remap = false, shift = At.Shift.AFTER, ordinal = 3))
    private static void injectAdvanced(CallbackInfoReturnable<OptionPage> cir, @Local(ordinal = 0) List<OptionGroup> groups) {
        OptionImpl<SodiumGameOptions, Boolean> videoTape = OptionImpl.createBuilder(Boolean.class, sodiumOpts)
                .setName(I18n.get("extras.video_tape"))
                .setTooltip(I18n.get("extras.video_tape.tooltip"))
                .setControl(TickBoxControl::new)
                .setBinding((sodiumGameOptions, value) -> MEMORY_LEAK_FIX.set(value), sodiumGameOptions -> MEMORY_LEAK_FIX.get())
                .setImpact(OptionImpact.VARIES)
                .build();

        groups.add(OptionGroup.createBuilder().add(videoTape).build());
    }

    @Inject(method = "quality", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", remap = false, shift = At.Shift.AFTER, ordinal = 2))
    private static void injectQuality(CallbackInfoReturnable<OptionPage> cir, @Local(ordinal = 0) List<OptionGroup> groups) {
        OptionImpl<SodiumGameOptions, Boolean> clearSkies = OptionImpl.createBuilder(Boolean.class, sodiumOpts)
                .setName(I18n.get("extras.clear_skies"))
                .setTooltip(I18n.get("extras.clear_skies.tooltip"))
                .setControl(TickBoxControl::new)
                .setBinding((sodiumGameOptions, value) -> CLEAR_SKIES.set(value), options -> CLEAR_SKIES.get())
                .setImpact(OptionImpact.LOW)
                .build();

        groups.add(OptionGroup.createBuilder().add(clearSkies).build());

        OptionImpl<SodiumGameOptions, Boolean> shutUpGlError = OptionImpl.createBuilder(Boolean.class, sodiumOpts)
                .setName(I18n.get("extras.gl_error"))
                .setTooltip(I18n.get("extras.gl_error.tooltip"))
                .setControl(TickBoxControl::new)
                .setBinding((sodiumGameOptions, value) -> SHUT_UP_GL_ERROR.set(value), options -> SHUT_UP_GL_ERROR.get())
                .setImpact(OptionImpact.LOW)
                .build();

        groups.add(OptionGroup.createBuilder().add(shutUpGlError).build());

        OptionImpl<SodiumGameOptions, Boolean> fadeInChunks = OptionImpl.createBuilder(Boolean.class, sodiumOpts)
                .setName(I18n.get("extras.fade_in_chunks"))
                .setTooltip(I18n.get("extras.fade_in_chunks.tooltip"))
                .setControl(TickBoxControl::new)
                .setBinding((sodiumGameOptions, value) -> FADE_IN_CHUNKS.set(value), options -> FADE_IN_CHUNKS.get())
                .setImpact(OptionImpact.LOW)
                .setFlags(new OptionFlag[]{OptionFlag.REQUIRES_RENDERER_RELOAD})
                .build();

        OptionImpl<SodiumGameOptions, Integer> fadeInTime = OptionImpl.createBuilder(Integer.TYPE, sodiumOpts)
                .setName(I18n.get("extras.fade_in_chunks.time"))
                .setTooltip(I18n.get("extras.fade_in_chunks.time.tooltip"))
                .setControl((option) -> new SliderControl(option, 5, 30, 5, ControlValueFormatter.number()))
                .setBinding(
                        (options, value) -> FADE_IN_TIME.set(value),
                        (options) -> FADE_IN_TIME.get())
                .setImpact(OptionImpact.LOW)
                .build();

        groups.add(OptionGroup.createBuilder().add(fadeInChunks).add(fadeInTime).build());
    }
}
