/*
 * Copyright 2018-2025 New Vector Ltd.
 * Copyright 2015 OpenMarket Ltd
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 * Please see LICENSE in the repository root for full details.
 */

package im.vector.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.DialogFragment;

import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.core.Log;

import java.util.ArrayList;
import java.util.List;

import im.vector.Matrix;
import im.vector.R;
import im.vector.adapters.VectorGroupsListAdapter;

/**
 * A dialog fragment showing the group ids list
 */
public class VectorUserGroupsDialogFragment extends DialogFragment {
    private static final String LOG_TAG = VectorUserGroupsDialogFragment.class.getSimpleName();

    private static final String ARG_SESSION_ID = "ARG_SESSION_ID";
    private static final String ARG_USER_ID = "ARG_USER_ID";
    private static final String ARG_GROUPS_ID = "ARG_GROUPS_ID";

    public static VectorUserGroupsDialogFragment newInstance(String sessionId, String userId, List<String> groupIds) {
        VectorUserGroupsDialogFragment f = new VectorUserGroupsDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SESSION_ID, sessionId);
        args.putString(ARG_USER_ID, userId);
        args.putStringArrayList(ARG_GROUPS_ID, new ArrayList<>(groupIds));
        f.setArguments(args);
        return f;
    }

    private MXSession mSession;
    private String mUserId;
    private List<String> mGroupIds;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSession = Matrix.getInstance(getContext()).getSession(getArguments().getString(ARG_SESSION_ID));
        mUserId = getArguments().getString(ARG_USER_ID);
        mGroupIds = getArguments().getStringArrayList(ARG_GROUPS_ID);

        // sanity check
        if ((mSession == null) || TextUtils.isEmpty(mUserId)) {
            Log.e(LOG_TAG, "## onCreate() : invalid parameters");
            dismiss();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_dialog_groups_list, container, false);
        ListView listView = v.findViewById(R.id.listView_groups);

        final VectorGroupsListAdapter adapter = new VectorGroupsListAdapter(getActivity(), R.layout.adapter_item_group_view, mSession);
        adapter.addAll(mGroupIds);
        listView.setAdapter(adapter);

        return v;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog d = super.onCreateDialog(savedInstanceState);
        d.setTitle(getString(R.string.groups_list));
        return d;
    }
}
