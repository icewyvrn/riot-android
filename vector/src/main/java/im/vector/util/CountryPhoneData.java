/*
 * Copyright 2025 New Vector Ltd.
 * Copyright 2017 Vector Creations Ltd
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 * Please see LICENSE in the repository root for full details.
 */

package im.vector.util;

public class CountryPhoneData {

    // The ISO country code (ex: FR)
    private final String mCountryCode;

    // The country name
    private final String mCountryName;

    // The country calling code (ex: 33 for France)
    private final int mCallingCode;

    CountryPhoneData(String countryCode, String countryName, int callingCode) {
        mCountryCode = countryCode;
        mCountryName = countryName;
        mCallingCode = callingCode;
    }

    public String getCountryCode() {
        return mCountryCode;
    }

    public String getCountryName() {
        return mCountryName;
    }

    public int getCallingCode() {
        return mCallingCode;
    }

    public String getFormattedCallingCode() {
        return "+" + mCallingCode;
    }
}