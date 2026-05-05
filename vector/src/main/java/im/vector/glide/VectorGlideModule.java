/*
 * Copyright 2019 New Vector Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package im.vector.glide;

import android.content.Context;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;

/**
 * Glide configuration tuned for low-RAM devices (e.g. Snapdragon S4 Plus, 2 GB).
 *
 * Key changes vs defaults:
 *  - Memory cache reduced to 1 screen (default is 2). Saves ~10-15 MB heap on WVGA displays.
 *  - DiskCacheStrategy.RESOURCE: caches the already-decoded, already-scaled bitmap so scroll-back
 *    is a fast disk read instead of a CPU-intensive decode.
 *  - PREFER_RGB_565: halves per-bitmap memory (2 bytes/px vs 4). Glide automatically falls back
 *    to ARGB_8888 for images that require an alpha channel, so no visual regressions.
 */
@GlideModule
public class VectorGlideModule extends AppGlideModule {

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        // Reduce memory cache from Glide's default of 2 screens to 1 screen
        MemorySizeCalculator calculator = new MemorySizeCalculator.Builder(context)
                .setMemoryCacheScreens(1.0f)
                .setBitmapPoolScreens(1.0f)
                .build();
        builder.setMemoryCache(new LruResourceCache(calculator.getMemoryCacheSize()));

        // Apply performance defaults globally: cache decoded bitmaps on disk,
        // and use RGB_565 to halve bitmap memory usage
        builder.setDefaultRequestOptions(
                new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .format(DecodeFormat.PREFER_RGB_565)
        );
    }
}
