/*
 * Copyright 2019-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial
 * Please see LICENSE files in the repository root for full details.
 */
package im.vector.widgets;

import org.matrix.androidsdk.core.model.MatrixError;

/**
 * Widget error code
 */
public class WidgetError extends MatrixError {
    public static final String WIDGET_NOT_ENOUGH_POWER_ERROR_CODE = "WIDGET_NOT_ENOUGH_POWER_ERROR_CODE";
    public static final String WIDGET_CREATION_FAILED_ERROR_CODE = "WIDGET_CREATION_FAILED_ERROR_CODE";

    /**
     * Create a widget error
     *
     * @param code                     the error code (see XX_ERROR_CODE)
     * @param detailedErrorDescription the detailed error description
     */
    public WidgetError(String code, String detailedErrorDescription) {
        errcode = code;
        error = detailedErrorDescription;
    }
}
