package me.jellysquid.mods.sodium.mixin.core.render.frustum;

import me.jellysquid.mods.sodium.client.render.viewport.frustum.SimpleFrustum;
import me.jellysquid.mods.sodium.client.render.viewport.Viewport;
import me.jellysquid.mods.sodium.client.render.viewport.ViewportProvider;
import net.minecraft.client.render.Frustum;
import org.joml.FrustumIntersection;
import org.joml.Vector3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Frustum.class)
public class FrustumMixin implements ViewportProvider {

    @Shadow
    private double x;

    @Shadow
    private double y;

    @Shadow
    private double z;

    @Shadow
    @Final
    private FrustumIntersection frustumIntersection;

    @Final
    private Viewport viewport;

    @Override
    public Viewport sodium$createViewport() {

        // Direct FrustumIntersection Access

        FrustumIntersection frustum = this.frustumIntersection;

        // Lazy Viewport Creation

        return viewport != null ? viewport : new Viewport(new SimpleFrustum(frustum), new Vector3d(this.x, this.y, this.z));
    }
}

