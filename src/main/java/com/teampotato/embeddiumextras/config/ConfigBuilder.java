package com.teampotato.embeddiumextras.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class ConfigBuilder {
    private static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();

    public ConfigBuilder(String comment) {
        COMMON_BUILDER.comment(comment).push("Embeddium Extras Settings");
    }

    public ForgeConfigSpec save() {
        COMMON_BUILDER.pop();
        return COMMON_BUILDER.build();
    }

    public void block(String name, @NotNull Consumer<ForgeConfigSpec.Builder> func) {
        COMMON_BUILDER.push(name);
        func.accept(COMMON_BUILDER);
        COMMON_BUILDER.pop();
    }
}