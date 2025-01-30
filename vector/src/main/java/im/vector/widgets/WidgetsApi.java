/*
 * Copyright 2025 New Vector Ltd.
 * Copyright 2015 OpenMarket Ltd
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 * Please see LICENSE in the repository root for full details.
 */
package im.vector.widgets;

import org.matrix.androidsdk.rest.model.openid.RequestOpenIdTokenResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

interface WidgetsApi {
    /**
     * register to the server
     *
     * @param requestOpenIdTokenResponse the body content (Ref: https://github.com/matrix-org/matrix-doc/pull/1961)
     */
    @POST("register")
    Call<RegisterResponse> register(@Body RequestOpenIdTokenResponse requestOpenIdTokenResponse, @Query("v") String version);

    @GET("account")
    Call<Void> validateToken(@Query("scalar_token") String scalarToken, @Query("v") String version);

}
