package me.jellysquid.mods.sodium.client.render.chunk.translucent_sorting;

import java.util.Arrays;

import org.joml.Vector3f;
import org.joml.Vector3fc;

import me.jellysquid.mods.sodium.client.model.quad.properties.ModelQuadFacing;

/**
 * Represents a quad for the purposes of translucency sorting. Called TQuad to
 * avoid confusion with other quad classes.
 * 
 * @implNote Autogenerated hashcode and equals methods to ensure the arrays are
 *           correctly compared..
 */
public record TQuad(ModelQuadFacing facing, Vector3fc normal, Vector3f center, float[] extents) {
    int getQuadHash() {
        // the hash code needs to be particularly collision resistant
        int result = 1;
        result = 31 * result + Arrays.hashCode(this.extents);
        if (facing == ModelQuadFacing.UNASSIGNED) {
            result = 31 * result + this.facing.hashCode();
        } else {
            result = 31 * result + this.normal.hashCode();
        }
        result = 31 * result + this.center.hashCode();
        return result;
    }
}
