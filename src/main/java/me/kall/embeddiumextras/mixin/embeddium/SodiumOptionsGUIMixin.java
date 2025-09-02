package me.kall.embeddiumextras.mixin.embeddium;

import com.google.common.collect.ImmutableList;
import me.jellysquid.mods.sodium.client.gui.SodiumGameOptions;
import me.jellysquid.mods.sodium.client.gui.SodiumOptionsGUI;
import me.jellysquid.mods.sodium.client.gui.options.*;
import me.jellysquid.mods.sodium.client.gui.options.control.ControlValueFormatter;
import me.jellysquid.mods.sodium.client.gui.options.control.CyclingControl;
import me.jellysquid.mods.sodium.client.gui.options.control.SliderControl;
import me.jellysquid.mods.sodium.client.gui.options.control.TickBoxControl;
import me.jellysquid.mods.sodium.client.gui.options.storage.SodiumOptionsStorage;
import me.kall.embeddiumextras.config.ExtrasConfig;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.TranslatableComponent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

import static me.kall.embeddiumextras.config.ExtrasConfig.*;
import static me.kall.embeddiumextras.config.ExtrasConfig.FPS_ALIGN_RIGHT;
import static me.kall.embeddiumextras.config.ExtrasConfig.FPS_POS_Y_OFFSET;
import static me.kall.embeddiumextras.config.ExtrasConfig.SHOW_GAME_TIME;
import static me.kall.embeddiumextras.config.ExtrasConfig.SHOW_GPU_INFO;
import static me.kall.embeddiumextras.config.ExtrasConfig.SHOW_MEMORY_INFO;

@Mixin(value = SodiumOptionsGUI.class, remap = false)
public abstract class SodiumOptionsGUIMixin {
    @Shadow @Final private List<OptionPage> pages;
    @Unique private static final SodiumOptionsStorage extras$sodiumOpts = new SodiumOptionsStorage();

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onInit(Screen prevScreen, CallbackInfo ci) {
        List<OptionGroup> groups = new ArrayList<>();

        Option<ExtrasConfig.FpsMode> displayFps = OptionImpl.createBuilder(ExtrasConfig.FpsMode.class, extras$sodiumOpts)
                .setName(I18n.get("extras.display_fps.display.name"))
                .setTooltip(I18n.get("extras.none"))
                .setControl(option -> new CyclingControl<>(option, ExtrasConfig.FpsMode.class, new TranslatableComponent[]{new TranslatableComponent("extras.option.off"), new TranslatableComponent("extras.option.simple"), new TranslatableComponent("extras.option.advanced")}))
                .setBinding((sodiumGameOptions, fpsMode) -> FPS_DISPLAY_MODE.set(fpsMode), sodiumGameOptions -> FPS_DISPLAY_MODE.get())
                .setImpact(OptionImpact.LOW).build();

        Option<Integer> fpsPosX = OptionImpl.createBuilder(Integer.TYPE, extras$sodiumOpts)
                .setName(I18n.get("extras.display_fps.position.x.name"))
                .setTooltip(I18n.get("extras.none"))
                .setControl((option) -> new SliderControl(option, 4, 64, 2, ControlValueFormatter.number()))
                .setImpact(OptionImpact.LOW)
                .setBinding((sodiumGameOptions, integer) -> FPS_POS_X_OFFSET.set(integer), sodiumGameOptions -> FPS_POS_X_OFFSET.get())
                .build();

        Option<Integer> fpsPosY = OptionImpl.createBuilder(Integer.TYPE, extras$sodiumOpts)
                .setName(I18n.get("extras.display_fps.position.y.name"))
                .setTooltip(I18n.get("extras.none"))
                .setControl((option) -> new SliderControl(option, 4, 64, 2, ControlValueFormatter.number()))
                .setImpact(OptionImpact.LOW)
                .setBinding((sodiumGameOptions, integer) -> FPS_POS_Y_OFFSET.set(integer), sodiumGameOptions -> FPS_POS_Y_OFFSET.get())
                .build();

        OptionImpl<SodiumGameOptions, Boolean> right = OptionImpl.createBuilder(Boolean.class, extras$sodiumOpts)
                .setName(I18n.get("extras.display_fps.right_align.name"))
                .setTooltip(I18n.get("extras.none"))
                .setControl(TickBoxControl::new)
                .setBinding((options, value) -> FPS_ALIGN_RIGHT.set(value), (options) -> FPS_ALIGN_RIGHT.get())
                .setImpact(OptionImpact.LOW)
                .build();


        groups.add(OptionGroup.createBuilder().add(displayFps).add(right).add(fpsPosX).add(fpsPosY).build());

        OptionImpl<SodiumGameOptions, Boolean> showPlayTime = OptionImpl.createBuilder(Boolean.class, extras$sodiumOpts)
                .setName(I18n.get("extras.display_fps.show_play_time.name"))
                .setTooltip(I18n.get("extras.none"))
                .setControl(TickBoxControl::new)
                .setBinding((options, value) -> SHOW_GAME_TIME.set(value), (options) -> SHOW_GAME_TIME.get())
                .setImpact(OptionImpact.LOW)
                .build();

        OptionImpl<SodiumGameOptions, Boolean> showMemory = OptionImpl.createBuilder(Boolean.class, extras$sodiumOpts)
                .setName(I18n.get("extras.display_fps.show_memory.name"))
                .setTooltip(I18n.get("extras.none"))
                .setControl(TickBoxControl::new)
                .setBinding((options, value) -> SHOW_MEMORY_INFO.set(value), (options) -> SHOW_MEMORY_INFO.get())
                .setImpact(OptionImpact.LOW)
                .build();

        OptionImpl<SodiumGameOptions, Boolean> showGpu = OptionImpl.createBuilder(Boolean.class, extras$sodiumOpts)
                .setName(I18n.get("extras.display_fps.show_gpu.name"))
                .setTooltip(I18n.get("extras.none"))
                .setControl(TickBoxControl::new)
                .setBinding((options, value) -> SHOW_GPU_INFO.set(value), (options) -> SHOW_GPU_INFO.get())
                .setImpact(OptionImpact.LOW)
                .build();

        groups.add(OptionGroup.createBuilder().add(showGpu).add(showMemory).add(showPlayTime).build());

        pages.add(new OptionPage(I18n.get("extras.display_fps.page"), ImmutableList.copyOf(groups)));
    }
}
