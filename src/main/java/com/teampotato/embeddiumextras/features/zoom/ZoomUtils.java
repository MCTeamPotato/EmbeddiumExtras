package com.teampotato.embeddiumextras.features.zoom;

import com.teampotato.embeddiumextras.config.EmbeddiumExtrasConfig;

//The class that contains most of the logic behind the zoom itself.
public class ZoomUtils {
    //The zoom signal, which is managed in an event and used by other mixins.
	public static boolean zoomState = false;

	//Used for post-zoom actions like updating the terrain.
	public static boolean lastZoomState = false;

	//The zoom divisor, managed by the zoom press and zoom scrolling. Used by other mixins.
	public static double zoomDivisor = EmbeddiumExtrasConfig.zoomValues.zoomDivisor;

	//The zoom FOV multipliers. Used by the GameRenderer mixin.
	public static float zoomFovMultiplier = 1.0F;
	public static float lastZoomFovMultiplier = 1.0F;

	//The zoom overlay's alpha. Used by the InGameHud mixin.
	public static float zoomOverlayAlpha = 0.0F;
	public static float lastZoomOverlayAlpha = 0.0F;

    //The method used for changing the zoom divisor, used by zoom scrolling and the keybinds.
	public static void changeZoomDivisor(boolean increase) {
		double changedZoomDivisor;
		double lesserChangedZoomDivisor;

		if (increase) {
			changedZoomDivisor = zoomDivisor + EmbeddiumExtrasConfig.zoomValues.scrollStep;
			lesserChangedZoomDivisor = zoomDivisor + EmbeddiumExtrasConfig.zoomValues.lesserScrollStep;
		} else {
			changedZoomDivisor = zoomDivisor - EmbeddiumExtrasConfig.zoomValues.scrollStep;
			lesserChangedZoomDivisor = zoomDivisor - EmbeddiumExtrasConfig.zoomValues.lesserScrollStep;
			lastZoomState = true;
		}

		if (lesserChangedZoomDivisor <= EmbeddiumExtrasConfig.zoomValues.zoomDivisor) {
			changedZoomDivisor = lesserChangedZoomDivisor;
		}

		if (changedZoomDivisor >= EmbeddiumExtrasConfig.zoomValues.minimumZoomDivisor) {
			if (changedZoomDivisor <= EmbeddiumExtrasConfig.zoomValues.maximumZoomDivisor) {
				zoomDivisor = changedZoomDivisor;
			}
		}
	}

	//The method used by both the "Reset Zoom" keybind and the "Reset Zoom With Mouse" tweak.
	public static void resetZoomDivisor() {
		zoomDivisor = EmbeddiumExtrasConfig.zoomValues.zoomDivisor;
		lastZoomState = true;
	}


	//The equivalent of GameRenderer's updateFovMultiplier but for zooming. Used by zoom transitions.
	public static void updateZoomFovMultiplier() {
		float zoomMultiplier = 1.0F;
		double dividedZoomMultiplier = 1.0 / ZoomUtils.zoomDivisor;

		if (ZoomUtils.zoomState) {
			zoomMultiplier = (float)dividedZoomMultiplier;
		}

		lastZoomFovMultiplier = zoomFovMultiplier;
		
		if (EmbeddiumExtrasConfig.zoomTransition.get().equals(EmbeddiumExtrasConfig.ZoomTransitionOptions.SMOOTH.toString())) {
			zoomFovMultiplier += (float) ((zoomMultiplier - zoomFovMultiplier) * EmbeddiumExtrasConfig.zoomValues.smoothMultiplier);
		}
	}

	//Handles the zoom overlay transparency with transitions. Used by zoom overlay.
	public static void updateZoomOverlayAlpha() {
		float zoomMultiplier = 0.0F;

		if (ZoomUtils.zoomState) {
			zoomMultiplier = 1.0F;
		}

		lastZoomOverlayAlpha = zoomOverlayAlpha;

		if (EmbeddiumExtrasConfig.zoomTransition.get().equals(EmbeddiumExtrasConfig.ZoomTransitionOptions.SMOOTH.toString())) {
			zoomOverlayAlpha += (float) ((zoomMultiplier - zoomOverlayAlpha) * EmbeddiumExtrasConfig.zoomValues.smoothMultiplier);
		}
	}
}