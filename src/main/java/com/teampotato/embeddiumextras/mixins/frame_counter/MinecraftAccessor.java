package com.teampotato.embeddiumextras.mixins.frame_counter;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Minecraft.class)
public interface MinecraftAccessor
{
    @Accessor("fps")
    static int ee$getFps()
    {
        throw new RuntimeException();
    }
}