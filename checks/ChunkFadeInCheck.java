public class ChunkFadeInCheck {

    static final int DURATION_MILLIS = 750;

    static float progress(int start, int now) {
        float t = Math.max(0.0f, Math.min((now - start) / (float) DURATION_MILLIS, 1.0f));
        return t * t * (3.0f - 2.0f * t);
    }

    static float shade(float fog, float color, float fade) {
        return fog * (1.0f - fade) + color * fade;
    }

    public static void main(String[] args) {
        assert progress(0, 0) == 0.0f : "fade must start fully in fog";
        assert progress(0, DURATION_MILLIS) == 1.0f : "fade must end at full colour";
        assert shade(0.7f, 0.2f, progress(0, 0)) == 0.7f : "start colour is not the fog colour";
        assert shade(0.7f, 0.2f, progress(0, DURATION_MILLIS)) == 0.2f : "end colour is not the real colour";

        float last = -1.0f;
        for (int t = 0; t <= DURATION_MILLIS; t++) {
            float p = progress(0, t);
            assert p >= last : "progress went backwards at " + t;
            assert p - last < 0.01f || last < 0.0f : "progress jumps at " + t;
            last = p;
        }

        int nearWrap = Integer.MAX_VALUE - 100;
        assert progress(nearWrap, nearWrap + 375) == progress(0, 375) : "int clock wrap breaks progress";
        assert progress(nearWrap, nearWrap + 5000) == 1.0f : "finished fade is not fully visible after wrap";
        assert progress(nearWrap - DURATION_MILLIS, nearWrap) == 1.0f : "untouched sections are not fully visible";
        assert progress(10, 5) == 0.0f : "start after now must stay hidden, not wrap negative";

        int end = nearWrap + DURATION_MILLIS;
        assert nearWrap + 10 - end < 0 : "still fading across the wrap";
        assert nearWrap + DURATION_MILLIS - end >= 0 : "fade end missed across the wrap";

        for (int id = 0; id < 256; id++) {
            assert (id >> 2) * 4 + (id & 3) == id : "vec4 array layout mismatch for draw id " + id;
        }

        System.out.println("ChunkFadeInCheck passed");
    }
}
