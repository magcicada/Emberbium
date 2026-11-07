package org.embeddedt.embeddium.render.chunk;

import me.jellysquid.mods.sodium.client.SodiumClientMod;
import org.embeddedt.embeddium.render.ShaderModBridge;

public final class ChunkFadeIn {
    public static final int DURATION_MILLIS = 750;

    private ChunkFadeIn() {
    }

    public static int now() {
        return (int) (System.nanoTime() / 1_000_000L);
    }

    public static boolean enabled() {
        return SodiumClientMod.options().quality.enableChunkFadeIn;
    }

    public static boolean active() {
        return enabled() && !ShaderModBridge.areShadersEnabled();
    }

    public static float progress(int start, int now) {
        float t = Math.max(0.0f, Math.min((now - start) / (float) DURATION_MILLIS, 1.0f));
        return t * t * (3.0f - 2.0f * t);
    }
}
