/*
 * Copyright 2019-2025 New Vector Ltd.
 * Copyright 2018 New Vector Ltd
 * Copyright 2015 OpenMarket Ltd
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial
 * Please see LICENSE files in the repository root for full details.
 */
package im.vector.widgets;

import android.net.Uri;

import org.matrix.androidsdk.HomeServerConnectionConfig;
import org.matrix.androidsdk.RestClient;
import org.matrix.androidsdk.core.JsonUtils;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.rest.callback.RestAdapterCallback;
import org.matrix.androidsdk.rest.model.openid.RequestOpenIdTokenResponse;

class WidgetsRestClient extends RestClient<WidgetsApi> {

    private static final String API_VERSION = "1.1";

    /**
     * {@inheritDoc}
     */
    public WidgetsRestClient(IntegrationManagerConfig config) {
        super(new HomeServerConnectionConfig.Builder()
                        .withHomeServerUri(Uri.parse(config.getApiUrl()))
                        .build(),
                WidgetsApi.class,
                "",
                JsonUtils.getGson(false));
    }

    /**
     * Register to the server
     *
     * @param requestOpenIdTokenResponse the response of a OpenId request (Ref: https://github.com/matrix-org/matrix-doc/pull/1961)
     * @param callback                   the asynchronous callback called when finished
     */
    public void register(final RequestOpenIdTokenResponse requestOpenIdTokenResponse, final ApiCallback<RegisterResponse> callback) {
        final String description = "Register";

        mApi.register(requestOpenIdTokenResponse, API_VERSION).enqueue(new RestAdapterCallback<>(description,
                mUnsentEventsManager, callback, () -> register(requestOpenIdTokenResponse, callback)));
    }

    /**
     * Validates the scalar token to the server
     */
    public void validateToken(final String scalarToken, final ApiCallback<Void> callback) {
        final String description = "Validate";

        mApi.validateToken(scalarToken, API_VERSION).enqueue(new RestAdapterCallback<>(description,
                mUnsentEventsManager, callback, null));
    }
}
