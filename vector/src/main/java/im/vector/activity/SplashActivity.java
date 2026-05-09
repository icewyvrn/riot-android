/*
 * Copyright 2014 OpenMarket Ltd
 * Copyright 2017 Vector Creations Ltd
 * Copyright 2018 New Vector Ltd
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
package im.vector.activity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.preference.PreferenceManager;

import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import im.vector.Matrix;
import im.vector.R;
import im.vector.VectorApp;
import im.vector.analytics.TrackingEvent;
import im.vector.push.PushManager;
import im.vector.receiver.VectorUniversalLinkReceiver;
import im.vector.services.EventStreamServiceX;
import im.vector.util.PreferencesManager;

/**
 * SplashActivity displays a splash while loading and initializing the client.
 */
public class SplashActivity extends MXCActionBarActivity {
    private static final String LOG_TAG = SplashActivity.class.getSimpleName();

    public static final String EXTRA_MATRIX_ID = "EXTRA_MATRIX_ID";
    public static final String EXTRA_ROOM_ID = "EXTRA_ROOM_ID";

    private final long mLaunchTime = System.currentTimeMillis();

    private static final String NEED_TO_CLEAR_CACHE_BEFORE_81200 = "NEED_TO_CLEAR_CACHE_BEFORE_81200";

    /* ==========================================================================================
     * UI
     * ========================================================================================== */

    @BindView(R.id.animated_logo_image_view)
    ImageView animatedLogo;

    /**
     * @return true if a store is corrupted.
     */
    private boolean hasCorruptedStore() {
        boolean hasCorruptedStore = false;
        List<MXSession> sessions = Matrix.getMXSessions(this);

        for (MXSession session : sessions) {
            if (session.isAlive()) {
                hasCorruptedStore |= session.getDataHandler().getStore().isCorrupted();
            }
        }
        return hasCorruptedStore;
    }

    /**
     * Close the splash screen if the stores are fully loaded.
     */
    private void onFinish() {
        Log.e(LOG_TAG, "##onFinish() : start VectorHomeActivity");
        final long finishTime = System.currentTimeMillis();
        final long duration = finishTime - mLaunchTime;
        final TrackingEvent event = new TrackingEvent.LaunchScreen(duration);
        VectorApp.getInstance().getAnalytics().trackEvent(event);

        if (!hasCorruptedStore()) {
            // Go to the home page
            Intent intent = new Intent(this, VectorHomeActivity.class);

            Bundle receivedBundle = getIntent().getExtras();

            if (null != receivedBundle) {
                intent.putExtras(receivedBundle);
            }

            // display a spinner while managing the universal link
            if (intent.hasExtra(VectorUniversalLinkReceiver.EXTRA_UNIVERSAL_LINK_URI)) {
                intent.putExtra(VectorHomeActivity.EXTRA_WAITING_VIEW_STATUS, VectorHomeActivity.WAITING_VIEW_START);
            }

            // launch from a shared files menu
            if (getIntent().hasExtra(VectorHomeActivity.EXTRA_SHARED_INTENT_PARAMS)) {
                intent.putExtra(VectorHomeActivity.EXTRA_SHARED_INTENT_PARAMS,
                        (Intent) getIntent().getParcelableExtra(VectorHomeActivity.EXTRA_SHARED_INTENT_PARAMS));
                getIntent().removeExtra(VectorHomeActivity.EXTRA_SHARED_INTENT_PARAMS);
            }

            if (getIntent().hasExtra(EXTRA_ROOM_ID) && getIntent().hasExtra(EXTRA_MATRIX_ID)) {
                Map<String, Object> params = new HashMap<>();

                params.put(VectorRoomActivity.EXTRA_MATRIX_ID, getIntent().getStringExtra(EXTRA_MATRIX_ID));
                params.put(VectorRoomActivity.EXTRA_ROOM_ID, getIntent().getStringExtra(EXTRA_ROOM_ID));
                intent.putExtra(VectorHomeActivity.EXTRA_JUMP_TO_ROOM_PARAMS, (HashMap) params);
            }

            startActivity(intent);
            finish();
        } else {
            CommonActivityUtils.logout(this);
        }
    }

    @Override
    public int getLayoutRes() {
        return R.layout.vector_activity_splash;
    }

    @Override
    public void initUiAndData() {
        List<MXSession> sessions = Matrix.getInstance(getApplicationContext()).getSessions();

        if (sessions == null) {
            Log.e(LOG_TAG, "onCreate no Sessions");
            finish();
            return;
        }

        // Check if store is corrupted, due to change of type of some maps from HashMap to Map in Serialized objects
        // Only on Android 7.1+
        // Only if previous versionCode of the installation is < 81200
        // Only once
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1
                && PreferenceManager.getDefaultSharedPreferences(this).getInt(PreferencesManager.VERSION_BUILD, 0) < 81200
                && PreferenceManager.getDefaultSharedPreferences(this).getBoolean(NEED_TO_CLEAR_CACHE_BEFORE_81200, true)) {
            PreferenceManager.getDefaultSharedPreferences(this)
                    .edit()
                    .putBoolean(NEED_TO_CLEAR_CACHE_BEFORE_81200, false)
                    .apply();

            // Force a clear cache
            Matrix.getInstance(this).reloadSessions(this, true);
            return;
        }

        Drawable background = animatedLogo.getBackground();
        if (background instanceof AnimationDrawable) {
            ((AnimationDrawable) background).start();
        }

        maybeEnableLazyLoadingInBackground(sessions);
        startEventStreamService(sessions);
    }

    private void maybeEnableLazyLoadingInBackground(final List<MXSession> sessions) {
        // Note: currently Riot uses a simple boolean to enable or disable LL, and does not support multi sessions
        // If it was the case, every session may not support LL. So for the moment, only consider 1 session
        if (sessions.size() != 1) {
            return;
        }

        // If LL is already ON, nothing to do
        if (PreferencesManager.useLazyLoading(this)) {
            return;
        } else {
            // Check that user has not explicitly disabled the lazy loading
            if (PreferencesManager.hasUserRefusedLazyLoading(this)) {
                return;
            } else {
                // Probe LL support without blocking the home screen.
                final MXSession session = sessions.get(0);

                session.canEnableLazyLoading(new ApiCallback<Boolean>() {
                    @Override
                    public void onNetworkError(Exception e) {
                        // Ignore, maybe next time.
                    }

                    @Override
                    public void onMatrixError(MatrixError e) {
                        // Ignore, maybe next time.
                    }

                    @Override
                    public void onUnexpectedError(Exception e) {
                        // Ignore, maybe next time.
                    }

                    @Override
                    public void onSuccess(Boolean info) {
                        if (info) {
                            // Enable LL for the next launch without reloading sessions now.
                            PreferencesManager.setUseLazyLoading(SplashActivity.this, true);
                        }
                    }
                });
            }
        }
    }

    private void startEventStreamService(Collection<MXSession> sessions) {
        List<String> matrixIds = new ArrayList<>();

        for (final MXSession session : sessions) {
            if (!session.getDataHandler().isInitialSyncComplete()) {
                VectorApp.addSyncingSession(session);
                matrixIds.add(session.getCredentials().userId);
            }
        }

        // when the events stream has been disconnected by the user
        // they must be awoken even if they are initialized
        if (Matrix.getInstance(this).mHasBeenDisconnected) {
            matrixIds = new ArrayList<>();

            for (MXSession session : sessions) {
                matrixIds.add(session.getCredentials().userId);
            }

            Matrix.getInstance(this).mHasBeenDisconnected = false;
        }

        EventStreamServiceX.Companion.onApplicationStarted(this);

        // trigger the push registration if required
        PushManager pushManager = Matrix.getInstance(getApplicationContext()).getPushManager();
        pushManager.deepCheckRegistration(this);

        onFinish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
