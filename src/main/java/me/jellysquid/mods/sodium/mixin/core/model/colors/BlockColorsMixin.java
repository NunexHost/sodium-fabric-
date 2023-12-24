package me.jellysquid.mods.sodium.mixin.core.model.colors;

import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.block.Block;
import me.jellysquid.mods.sodium.client.SodiumClientMod;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.registry.Registries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockColors.class)
public class BlockColorsMixin {

    @Unique
    private final Reference2ReferenceMap<Block, BlockColorProvider> blocksToColor = new Reference2ReferenceOpenHashMap<>();

    @Unique
    private final ReferenceSet<Block> overridenBlocks = new ReferenceOpenHashSet<>();

    @Inject(method = "registerColorProvider", at = @At("HEAD"))
    private void preRegisterColorProvider(BlockColorProvider provider, Block[] blocks, CallbackInfo ci) {
        for (Block block : blocks) {
            // There will be one provider already registered for vanilla blocks, if we are replacing it,
            // it means a mod is using custom logic and we need to disable per-vertex coloring
            if (blocksToColor.put(block, provider) != null) {
                overridenBlocks.add(block);
                SodiumClientMod.logger().info("Block {} had its color provider replaced with {} and will not use per-vertex coloring", Registries.BLOCK.getId(block), provider.toString());
            }
        }
    }

    // ... (other methods remain the same)

    @Inject(method = "getColorProvider", at = @At("HEAD"), cancellable = true)
    private BlockColorProvider getColorProvider(Block block, CallbackInfo ci) {
        BlockColorProvider provider = blocksToColor.get(block);
        if (provider != null) {
            ci.cancel();  // Cancel original method if a provider is found
            return provider;
        }
        return null;  // Let the original method proceed if no provider is found
    }

    // ... (other methods remain the same)
}

