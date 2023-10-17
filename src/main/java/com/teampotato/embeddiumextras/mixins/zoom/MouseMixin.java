package com.teampotato.embeddiumextras.mixins.zoom;

import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHelper;
import net.minecraft.client.util.MouseSmoother;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import com.teampotato.embeddiumextras.EmbeddiumExtras;
import com.teampotato.embeddiumextras.config.EmbeddiumExtrasConfig;
import com.teampotato.embeddiumextras.features.zoom.ZoomUtils;


//This mixin is responsible for the mouse-behavior-changing part of the zoom.
@Mixin(MouseHelper.class)
public abstract class MouseMixin {
	@Shadow
	@Final
	private Minecraft minecraft;

	@Final
	@Shadow
	private final MouseSmoother smoothTurnX = new MouseSmoother();

	@Final
	@Shadow
	private final MouseSmoother smoothTurnY = new MouseSmoother();

	@Shadow
	private double accumulatedDX;

	@Shadow
	private double accumulatedDY;
	
	@Shadow
	private double accumulatedScroll;
	
	@Unique
	private final MouseSmoother ee$cursorXZoomSmoother = new MouseSmoother();

	@Unique
	private final MouseSmoother ee$cursorYZoomSmoother = new MouseSmoother();

	@Unique
	private double ee$extractedE;
	@Unique
	private double ee$adjustedG;
	
	//This mixin handles the "Reduce Sensitivity" option and extracts the g variable for the cinematic cameras.
	@ModifyVariable(
		at = @At(value = "FIELD", target = "Lnet/minecraft/client/MouseHelper;minecraft:Lnet/minecraft/client/Minecraft;", ordinal = 2),
		method = "turnPlayer",
		ordinal = 2
	)
	private double applyReduceSensitivity(double g) {
		double modifiedMouseSensitivity = this.minecraft.options.sensitivity;

		if (EmbeddiumExtrasConfig.lowerZoomSensitivity.get())
		{
			if (!EmbeddiumExtrasConfig.zoomTransition.get().equals(EmbeddiumExtrasConfig.ZoomTransitionOptions.OFF.toString())) {
				modifiedMouseSensitivity *= ZoomUtils.zoomFovMultiplier;
			} else if (ZoomUtils.zoomState) {
				modifiedMouseSensitivity /= ZoomUtils.zoomDivisor;
			}
		}

		double appliedMouseSensitivity = modifiedMouseSensitivity * 0.6 + 0.2;
		g = appliedMouseSensitivity * appliedMouseSensitivity * appliedMouseSensitivity * 8.0;
		this.ee$adjustedG = g;
		return g;
	}
	
	//Extracts the e variable for the cinematic cameras.
	@Inject(
		at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MouseHelper;isMouseGrabbed()Z"),
		method = "turnPlayer",
		locals = LocalCapture.CAPTURE_FAILHARD
	)
	private void obtainCinematicCameraValues(CallbackInfo info, double d, double e) {
		this.ee$extractedE = e;
	}

	//Applies the cinematic camera on the mouse's X.
	@ModifyVariable(
		at = @At(value = "FIELD", target = "Lnet/minecraft/client/MouseHelper;accumulatedDX:D", ordinal = 2, shift = At.Shift.BEFORE),
		method = "turnPlayer",
		ordinal = 2
	)
	private double applyCinematicModeX(double l) {
		if (!EmbeddiumExtrasConfig.cinematicCameraMode.get().equals(EmbeddiumExtrasConfig.CinematicCameraOptions.OFF.toString())) {
			if (ZoomUtils.zoomState) {
				if (this.minecraft.options.smoothCamera) {
					l = this.smoothTurnX.getNewDeltaValue(this.accumulatedDX * this.ee$adjustedG, (this.ee$extractedE * this.ee$adjustedG));
					this.ee$cursorXZoomSmoother.reset();
				} else {
					l = this.ee$cursorXZoomSmoother.getNewDeltaValue(this.accumulatedDX * this.ee$adjustedG, (this.ee$extractedE * this.ee$adjustedG));
				}
				if (EmbeddiumExtrasConfig.cinematicCameraMode.get().equals(EmbeddiumExtrasConfig.CinematicCameraOptions.MULTIPLIED.toString())) {
					l *= EmbeddiumExtrasConfig.zoomValues.cinematicMultiplier;
				}
			} else {
				this.ee$cursorXZoomSmoother.reset();
			}
		}
		return l;
	}
	
	//Applies the cinematic camera on the mouse's Y.
	@ModifyVariable(
		at = @At(value = "FIELD", target = "Lnet/minecraft/client/MouseHelper;accumulatedDY:D", ordinal = 2, shift = At.Shift.BEFORE),
		method = "turnPlayer",
		ordinal = 2
	)
	private double applyCinematicModeY(double m) {
		if (!EmbeddiumExtrasConfig.cinematicCameraMode.get().equals(EmbeddiumExtrasConfig.CinematicCameraOptions.OFF.toString())) {
			if (ZoomUtils.zoomState) {
				if (this.minecraft.options.smoothCamera) {
					m = this.smoothTurnY.getNewDeltaValue(this.accumulatedDY * this.ee$adjustedG, (this.ee$extractedE * this.ee$adjustedG));
					this.ee$cursorYZoomSmoother.reset();
				} else {
					m = this.ee$cursorYZoomSmoother.getNewDeltaValue(this.accumulatedDY * this.ee$adjustedG, (this.ee$extractedE * this.ee$adjustedG));
				}
				if (EmbeddiumExtrasConfig.cinematicCameraMode.get().equals(EmbeddiumExtrasConfig.CinematicCameraOptions.MULTIPLIED.toString())) {
					m *= EmbeddiumExtrasConfig.zoomValues.cinematicMultiplier;
				}
			} else {
				this.ee$cursorYZoomSmoother.reset();
			}
		}
		
		return m;
	}
	
	//Handles zoom scrolling.
	@Inject(
		at = @At(value = "FIELD", target = "Lnet/minecraft/client/MouseHelper;accumulatedScroll:D", ordinal = 7),
		method = "onScroll",
		cancellable = true
	)
	private void zoomerOnMouseScroll(CallbackInfo info) {
		if (this.accumulatedScroll != 0.0) {
			if (EmbeddiumExtrasConfig.zoomScrolling.get()) {
				if (EmbeddiumExtrasConfig.zoomMode.get().equals(EmbeddiumExtrasConfig.ZoomModes.PERSISTENT.toString())) {
					if (!EmbeddiumExtras.zoomKey.isDown())
					{
						return;
					}
				}
				
				if (ZoomUtils.zoomState) {
					if (this.accumulatedScroll > 0.0) {
						ZoomUtils.changeZoomDivisor(true);
					} else if (this.accumulatedScroll < 0.0) {
						ZoomUtils.changeZoomDivisor(false);
					}
					
					info.cancel();
				}
			}
		}
	}

	@Inject(
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/settings/KeyBinding;set(Lnet/minecraft/client/util/InputMappings$Input;Z)V"),
			method = "onPress(JIII)V",
			cancellable = true
	)
	private void zoomerOnMouseButton(long window, int button, int action, int mods, CallbackInfo info) {
		if (EmbeddiumExtrasConfig.zoomScrolling.get()) {
			if (!EmbeddiumExtras.zoomKey.isDown() && EmbeddiumExtrasConfig.zoomMode.get().equals(EmbeddiumExtrasConfig.ZoomModes.PERSISTENT.toString())) {
				return;
			}

			if (button == 2 && action == 1 && EmbeddiumExtras.zoomKey.isDown()) {
				ZoomUtils.resetZoomDivisor();
				info.cancel();
			}
		}
	}
}