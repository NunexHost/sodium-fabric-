package me.jellysquid.mods.sodium.mixin.core.render.world;

import me.jellysquid.mods.sodium.client.render.chunk.NonStoringBuilderPool;
import net.minecraft.client.render.BufferBuilderStorage;
import net.minecraft.client.render.chunk.BlockBufferBuilderPool;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BufferBuilderStorage.class)
public class BufferBuilderStorageMixin {

private static NonStoringBuilderPool s_nonStoringBuilderPool;

@Inject(method = "<init>", at = @At("RETURN"))
private void sodium$initializeNonStoringBuilderPool(int i, CallbackInfo ci) {
    s_nonStoringBuilderPool = new NonStoringBuilderPool();
}

@Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/chunk/BlockBufferBuilderPool;allocate(I)Lnet/minecraft/client/render/chunk/BlockBufferBuilderPool;"))
private BlockBufferBuilderPool sodium$doNotAllocateChunks(int i) {
    return s_nonStoringBuilderPool;
   }
}
