package vice.magnesium_extras.mixins.sodiumconfig;


import com.google.common.collect.ImmutableList;
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
import vice.magnesium_extras.config.MagnesiumExtrasConfig;

import java.util.ArrayList;
import java.util.List;

@Pseudo
@Mixin(value = SodiumOptionsGUI.class, remap = false)
public abstract class ZoomSettingsPage {

    @Shadow
    @Final
    private List<OptionPage> pages;
    @Unique
    private static final SodiumOptionsStorage rbextras$sodiumOpts = new SodiumOptionsStorage();


    @Inject(method = "<init>", at = @At("RETURN"))
    private void dynamicLights(Screen prevScreen, CallbackInfo ci) {
        List<OptionGroup> groups = new ArrayList<>();

        OptionImpl<SodiumGameOptions, Boolean> lowerSensitivity = OptionImpl.createBuilder(Boolean.class, rbextras$sodiumOpts)
                .setName(I18n.get("extras.zoom.lower_sensitivity.name"))
                .setTooltip(I18n.get("extras.zoom.lower_sensitivity.tooltip"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> MagnesiumExtrasConfig.lowerZoomSensitivity.set(value),
                        (options) -> MagnesiumExtrasConfig.lowerZoomSensitivity.get())
                .setImpact(OptionImpact.LOW)
                .build();

        OptionImpl<SodiumGameOptions, Boolean> zoomScrolling = OptionImpl.createBuilder(Boolean.class, rbextras$sodiumOpts)
                .setName(I18n.get("extras.zoom.scrolling.name"))
                .setTooltip(I18n.get("extras.zoom.scrolling.tooltip"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> MagnesiumExtrasConfig.zoomScrolling.set(value),
                        (options) -> MagnesiumExtrasConfig.zoomScrolling.get())
                .setImpact(OptionImpact.LOW)
                .build();

        OptionImpl<SodiumGameOptions, Boolean> zoomOverlay = OptionImpl.createBuilder(Boolean.class, rbextras$sodiumOpts)
                .setName(I18n.get("extras.zoom.overlay.name"))
                .setTooltip(I18n.get("extras.zoom.overlay.tooltip"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> MagnesiumExtrasConfig.zoomOverlay.set(value),
                        (options) -> MagnesiumExtrasConfig.zoomOverlay.get())
                .setImpact(OptionImpact.LOW)
                .build();

        groups.add(OptionGroup
                .createBuilder()
                .add(lowerSensitivity)
                .add(zoomScrolling)
                .add(zoomOverlay)
                .build()
        );



        Option<MagnesiumExtrasConfig.ZoomTransitionOptions> zoomTransition =  OptionImpl.createBuilder(MagnesiumExtrasConfig.ZoomTransitionOptions.class, rbextras$sodiumOpts)
                .setName(I18n.get("extras.zoom.transition.name"))
                .setTooltip(I18n.get("extras.zoom.transition.tooltip"))
                .setControl(
                        (option) -> new CyclingControl<>(option, MagnesiumExtrasConfig.ZoomTransitionOptions.class, new TranslationTextComponent[] {
                                new TranslationTextComponent("extras.option.off"),
                                new TranslationTextComponent("extras.option.smooth")
                        })
                )
                .setBinding(
                        (opts, value) -> MagnesiumExtrasConfig.zoomTransition.set(value.toString()),
                        (opts) -> MagnesiumExtrasConfig.ZoomTransitionOptions.valueOf(MagnesiumExtrasConfig.zoomTransition.get()))
                .setImpact(OptionImpact.LOW)
                .build();

        Option<MagnesiumExtrasConfig.ZoomModes> zoomMode =  OptionImpl.createBuilder(MagnesiumExtrasConfig.ZoomModes.class, rbextras$sodiumOpts)
                .setName(I18n.get("extras.zoom.keybind.name"))
                .setTooltip(I18n.get("extras.zoom.keybind.tooltip"))
                .setControl(
                        (option) -> new CyclingControl<>(option, MagnesiumExtrasConfig.ZoomModes.class, new TranslationTextComponent[] {
                                new TranslationTextComponent("extras.option.hold"),
                                new TranslationTextComponent("extras.option.toggle"),
                                new TranslationTextComponent("extras.option.persistent")
                        })
                )
                .setBinding(
                        (opts, value) -> MagnesiumExtrasConfig.zoomMode.set(value.toString()),
                        (opts) -> MagnesiumExtrasConfig.ZoomModes.valueOf(MagnesiumExtrasConfig.zoomMode.get()))
                .setImpact(OptionImpact.LOW)
                .build();

        Option<MagnesiumExtrasConfig.CinematicCameraOptions> cinematicCameraMode =  OptionImpl.createBuilder(MagnesiumExtrasConfig.CinematicCameraOptions.class, rbextras$sodiumOpts)
                .setName(I18n.get("extras.zoom.cinematic_camera.name"))
                .setTooltip(I18n.get("extras.zoom.cinematic_camera.tooltip"))
                .setControl((option) -> new CyclingControl<>(option, MagnesiumExtrasConfig.CinematicCameraOptions.class, new TranslationTextComponent[] {
                        new TranslationTextComponent("extras.option.off"),
                        new TranslationTextComponent("extras.option.vanilla"),
                        new TranslationTextComponent("extras.option.multiplied")
                }))
                .setBinding(
                        (opts, value) -> MagnesiumExtrasConfig.cinematicCameraMode.set(value.toString()),
                        (opts) -> MagnesiumExtrasConfig.CinematicCameraOptions.valueOf(MagnesiumExtrasConfig.cinematicCameraMode.get()))
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