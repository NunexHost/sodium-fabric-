package me.jellysquid.mods.sodium.mixin.core.model.colors;

import it.unimi.dsi.fastutil.objects.*;
import me.jellysquid.mods.sodium.client.SodiumClientMod;
import me.jellysquid.mods.sodium.client.model.color.interop.BlockColorsExtended;
import net.minecraft.block.Block;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Collections;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.registry.Registries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockColors.class)
public class BlockColorsMixin implements BlockColorsExtended {

    // We're keeping a copy as we need to be able to iterate over the entry pairs, rather than just the values.
    private final Map<Block, BlockColorProvider> blocksToColor = new HashMap<>();

    private final Set<Block> overridenBlocks = new HashSet<>();

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

    @Override
    public Map<Block, BlockColorProvider> sodium$getProviders() {
        return blocksToColor;
    }

    @Override
    public Set<Block> sodium$getOverridenVanillaBlocks() {
        return overridenBlocks;
    }

    // Optimized code starts here

    @Inject(method = "<init>", at = @At("RETURN"))
    public BlockColorsMixin(CallbackInfo ci) {
        blocksToColor = new HashMap<>();
        overridenBlocks = new HashSet<>();
    }

    @Inject(method = "getColorProvider", at = @At("HEAD"))
    private BlockColorProvider getColorProvider(Block block, CallbackInfo ci) {
        return blocksToColor.get(block);
    }

    // Optimized code ends here
}
