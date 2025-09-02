package me.kall.embeddiumextras.mixin;

import com.llamalad7.mixinextras.MixinExtrasBootstrap;
import net.minecraftforge.fml.loading.FMLLoader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class MixinConfigPlugin implements IMixinConfigPlugin {
    public MixinConfigPlugin() {
        Path jarFilePath = FMLLoader.getLoadingModList().getModFileById("embeddiumextras").getFile().getFilePath();
        Path resourcePacks = FMLLoader.getGamePath().resolve("resourcepacks");
        try {
            extractAndZipResourcePack(jarFilePath, resourcePacks, "EmbeddiumExtrasFastChestResourcepack");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void extractAndZipResourcePack(Path jarFilePath, Path resourcePacksDir, String zipName) throws IOException {
        Path tempDir = Files.createTempDirectory("resourcepack_extract_");

        try (ZipFile zipFile = new ZipFile(jarFilePath.toFile())) {
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            String prefix = "assets/embeddiumextras/resourcepack/";

            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                String name = entry.getName();

                if (!name.startsWith(prefix)) continue;

                Path relativePath = Paths.get(name.substring(prefix.length()));
                Path targetPath = tempDir.resolve(relativePath);

                if (entry.isDirectory()) {
                    Files.createDirectories(targetPath);
                } else {
                    Files.createDirectories(targetPath.getParent());
                    try (InputStream in = zipFile.getInputStream(entry)) {
                        Files.copy(in, targetPath, StandardCopyOption.REPLACE_EXISTING);
                    }
                }
            }
        }

        Path zipPath = resourcePacksDir.resolve(zipName + ".zip");
        Files.createDirectories(resourcePacksDir);

        try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(zipPath))) {
            try (Stream<Path> pathStream = Files.walk(tempDir)) {
                pathStream.forEach(path -> {
                    if (Files.isDirectory(path)) return;
                    Path relative = tempDir.relativize(path);
                    try {
                        zos.putNextEntry(new ZipEntry(relative.toString().replace("\\", "/")));
                        Files.copy(path, zos);
                        zos.closeEntry();
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                });
            }
        }

        deleteDirectory(tempDir);
    }

    private static void deleteDirectory(Path dir) throws IOException {
        if (!Files.exists(dir)) return;
        try (Stream<Path> pathStream = Files.walk(dir)){
            pathStream.sorted(Comparator.reverseOrder())
                    .forEach(path -> {
                        try { Files.delete(path); } catch (IOException ignored) {}
                    });
        }
    }

    @Override
    public void onLoad(String mixinPackage) {
        MixinExtrasBootstrap.init();
    }

    @Override public String getRefMapperConfig() {return "";}
    @Override public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {return true;}
    @Override public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {}
    @Override public List<String> getMixins() {return Collections.emptyList();}
    @Override public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}
    @Override public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}
}