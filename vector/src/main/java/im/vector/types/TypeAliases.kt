/*
 * Copyright 2018-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial
 * Please see LICENSE files in the repository root for full details.
 */

package im.vector.types


/* ==========================================================================================
 * Types for JSON
 * ========================================================================================== */

typealias JsonDict<T> = Map<String, T>

/* ==========================================================================================
 * Types for Widget event
 * ========================================================================================== */

// Example of data:
// {
//   "event.data": {
//       "action": "get_widgets",
//       "room_id": "!byqyNXFYAGirEulaEm:matrix.org",
//       "_id": "1526370173321-0.55myregve98-1"
//   }
// }
typealias WidgetEventData = JsonDict<JsonDict<Any>>

