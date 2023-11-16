package com.teampotato.embeddiumextras.mixins.embeddiumconfig;


import com.google.common.collect.ImmutableList;
import com.teampotato.embeddiumextras.config.EmbeddiumExtrasConfig;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import me.jellysquid.mods.sodium.client.gui.SodiumGameOptions;
import me.jellysquid.mods.sodium.client.gui.SodiumOptionsGUI;
import me.jellysquid.mods.sodium.client.gui.options.*;
import me.jellysquid.mods.sodium.client.gui.options.control.CyclingControl;
import me.jellysquid.mods.sodium.client.gui.options.control.TickBoxControl;
import me.jellysquid.mods.sodium.client.gui.options.storage.SodiumOptionsStorage;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TranslationTextComponent;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = SodiumOptionsGUI.class, remap = false)
public abstract class ZoomSettingsPage {

    @Shadow
    @Final
    private List<OptionPage> pages;
    @Unique
    private static final SodiumOptionsStorage ee$sodiumOpts = new SodiumOptionsStorage();


    @Inject(method = "<init>", at = @At("RETURN"))
    private void dynamicLights(Screen prevScreen, CallbackInfo ci) {
        List<OptionGroup> groups = new ObjectArrayList<>();

        OptionImpl<SodiumGameOptions, Boolean> lowerSensitivity = OptionImpl.createBuilder(Boolean.class, ee$sodiumOpts)
                .setName(I18n.get("extras.zoom.lower_sensitivity.name"))
                .setTooltip(I18n.get("extras.zoom.lower_sensitivity.tooltip"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> EmbeddiumExtrasConfig.lowerZoomSensitivity.set(value),
                        (options) -> EmbeddiumExtrasConfig.lowerZoomSensitivity.get())
                .setImpact(OptionImpact.LOW)
                .build();

        OptionImpl<SodiumGameOptions, Boolean> zoomScrolling = OptionImpl.createBuilder(Boolean.class, ee$sodiumOpts)
                .setName(I18n.get("extras.zoom.scrolling.name"))
                .setTooltip(I18n.get("extras.zoom.scrolling.tooltip"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> EmbeddiumExtrasConfig.zoomScrolling.set(value),
                        (options) -> EmbeddiumExtrasConfig.zoomScrolling.get())
                .setImpact(OptionImpact.LOW)
                .build();

        OptionImpl<SodiumGameOptions, Boolean> zoomOverlay = OptionImpl.createBuilder(Boolean.class, ee$sodiumOpts)
                .setName(I18n.get("extras.zoom.overlay.name"))
                .setTooltip(I18n.get("extras.zoom.overlay.tooltip"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> EmbeddiumExtrasConfig.zoomOverlay.set(value),
                        (options) -> EmbeddiumExtrasConfig.zoomOverlay.get())
                .setImpact(OptionImpact.LOW)
                .build();

        groups.add(OptionGroup
                .createBuilder()
                .add(lowerSensitivity)
                .add(zoomScrolling)
                .add(zoomOverlay)
                .build()
        );



        Option<EmbeddiumExtrasConfig.ZoomTransitionOptions> zoomTransition =  OptionImpl.createBuilder(EmbeddiumExtrasConfig.ZoomTransitionOptions.class, ee$sodiumOpts)
                .setName(I18n.get("extras.zoom.transition.name"))
                .setTooltip(I18n.get("extras.zoom.transition.tooltip"))
                .setControl(
                        (option) -> new CyclingControl<>(option, EmbeddiumExtrasConfig.ZoomTransitionOptions.class, new TranslationTextComponent[] {
                                new TranslationTextComponent("extras.option.off"),
                                new TranslationTextComponent("extras.option.smooth")
                        })
                )
                .setBinding(
                        (opts, value) -> EmbeddiumExtrasConfig.zoomTransition.set(value.toString()),
                        (opts) -> EmbeddiumExtrasConfig.ZoomTransitionOptions.valueOf(EmbeddiumExtrasConfig.zoomTransition.get()))
                .setImpact(OptionImpact.LOW)
                .build();

        Option<EmbeddiumExtrasConfig.ZoomModes> zoomMode =  OptionImpl.createBuilder(EmbeddiumExtrasConfig.ZoomModes.class, ee$sodiumOpts)
                .setName(I18n.get("extras.zoom.keybind.name"))
                .setTooltip(I18n.get("extras.zoom.keybind.tooltip"))
                .setControl(
                        (option) -> new CyclingControl<>(option, EmbeddiumExtrasConfig.ZoomModes.class, new TranslationTextComponent[] {
                                new TranslationTextComponent("extras.option.hold"),
                                new TranslationTextComponent("extras.option.toggle"),
                                new TranslationTextComponent("extras.option.persistent")
                        })
                )
                .setBinding(
                        (opts, value) -> EmbeddiumExtrasConfig.zoomMode.set(value.toString()),
                        (opts) -> EmbeddiumExtrasConfig.ZoomModes.valueOf(EmbeddiumExtrasConfig.zoomMode.get()))
                .setImpact(OptionImpact.LOW)
                .build();

        Option<EmbeddiumExtrasConfig.CinematicCameraOptions> cinematicCameraMode =  OptionImpl.createBuilder(EmbeddiumExtrasConfig.CinematicCameraOptions.class, ee$sodiumOpts)
                .setName(I18n.get("extras.zoom.cinematic_camera.name"))
                .setTooltip(I18n.get("extras.zoom.cinematic_camera.tooltip"))
                .setControl((option) -> new CyclingControl<>(option, EmbeddiumExtrasConfig.CinematicCameraOptions.class, new TranslationTextComponent[] {
                        new TranslationTextComponent("extras.option.off"),
                        new TranslationTextComponent("extras.option.vanilla"),
                        new TranslationTextComponent("extras.option.multiplied")
                }))
                .setBinding(
                        (opts, value) -> EmbeddiumExtrasConfig.cinematicCameraMode.set(value.toString()),
                        (opts) -> EmbeddiumExtrasConfig.CinematicCameraOptions.valueOf(EmbeddiumExtrasConfig.cinematicCameraMode.get()))
                .setImpact(OptionImpact.LOW)
                .build();

        groups.add(OptionGroup
                .createBuilder()
                .add(zoomTransition)
                .add(zoomMode)
                .add(cinematicCameraMode)
                .build()
        );


        pages.add(new OptionPage(I18n.get("extras.zoom.option.name"), ImmutableList.copyOf(groups)));
    }
}