/*
 * Copyright 2025 New Vector Ltd.
 * Copyright 2014 OpenMarket Ltd
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 * Please see LICENSE in the repository root for full details.
 */
package im.vector.util;

import org.matrix.androidsdk.crypto.model.crypto.EncryptedFileInfo;

import java.io.Serializable;

public class SlidableMediaInfo implements Serializable {

    // Message.MSGTYPE_XXX
    public String mFileName;
    public String mMessageType;
    public String mMediaUrl;
    public String mThumbnailUrl;
    public String mMimeType;
    public EncryptedFileInfo mEncryptedFileInfo;

    // exif infos
    public int mRotationAngle = 0;
    public int mOrientation = 0;

    // default constructor
    public SlidableMediaInfo() {
    }
}
