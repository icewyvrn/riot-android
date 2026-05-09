/*
 * Copyright 2026
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

package im.vector.adapters;

import android.content.Context;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.data.Room;
import org.matrix.androidsdk.rest.model.publicroom.PublicRoom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import im.vector.R;
import im.vector.settings.VectorLocale;
import im.vector.ui.themes.ThemeUtils;
import im.vector.util.RoomUtils;
import im.vector.util.VectorUtils;

public class RoomsListAdapter extends BaseAdapter implements Filterable {

    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_INVITE = 1;
    private static final int VIEW_TYPE_ROOM = 2;
    private static final int VIEW_TYPE_PUBLIC_HEADER = 3;
    private static final int VIEW_TYPE_PUBLIC_ROOM = 4;

    private final Context mContext;
    private final LayoutInflater mInflater;
    private final MXSession mSession;
    private final RoomAdapter.OnSelectItemListener mSelectionListener;
    private final AbsAdapter.RoomInvitationListener mInvitationListener;
    private final AbsAdapter.MoreRoomActionListener mMoreRoomActionListener;

    private final List<Room> mInvitations = new ArrayList<>();
    private final List<Room> mFilteredInvitations = new ArrayList<>();
    private final List<Room> mRooms = new ArrayList<>();
    private final List<Room> mFilteredRooms = new ArrayList<>();
    private final List<PublicRoom> mPublicRooms = new ArrayList<>();

    private CharSequence mCurrentFilterPattern;
    private ArrayAdapter<CharSequence> mRoomDirectoryAdapter;
    private View.OnTouchListener mPublicRoomsTouchListener;
    private String mSelectedDirectoryDisplayName;
    private int mEstimatedPublicRoomsCount = -1;

    public RoomsListAdapter(@NonNull Context context,
                            @NonNull MXSession session,
                            @NonNull RoomAdapter.OnSelectItemListener selectionListener,
                            @NonNull AbsAdapter.RoomInvitationListener invitationListener,
                            @NonNull AbsAdapter.MoreRoomActionListener moreRoomActionListener) {
        mContext = context;
        mSession = session;
        mSelectionListener = selectionListener;
        mInvitationListener = invitationListener;
        mMoreRoomActionListener = moreRoomActionListener;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        int count = 0;

        if (!mFilteredInvitations.isEmpty()) {
            count += 1 + mFilteredInvitations.size();
        }

        if (!mFilteredRooms.isEmpty()) {
            count += 1 + mFilteredRooms.size();
        }

        count += 1 + mPublicRooms.size();
        return count;
    }

    @Override
    public Object getItem(int position) {
        ItemDescriptor descriptor = resolvePosition(position);
        if (descriptor == null) {
            return null;
        }

        switch (descriptor.viewType) {
            case VIEW_TYPE_INVITE:
                return mFilteredInvitations.get(descriptor.indexInSection);
            case VIEW_TYPE_ROOM:
                return mFilteredRooms.get(descriptor.indexInSection);
            case VIEW_TYPE_PUBLIC_ROOM:
                return mPublicRooms.get(descriptor.indexInSection);
            default:
                return null;
        }
    }

    @Override
    public long getItemId(int position) {
        ItemDescriptor descriptor = resolvePosition(position);
        if (descriptor == null) {
            return position;
        }

        switch (descriptor.viewType) {
            case VIEW_TYPE_INVITE:
            case VIEW_TYPE_ROOM:
                return mFilteredRooms.contains(getItem(position)) || mFilteredInvitations.contains(getItem(position))
                        ? ((Room) getItem(position)).getRoomId().hashCode()
                        : position;
            case VIEW_TYPE_PUBLIC_ROOM:
                PublicRoom publicRoom = mPublicRooms.get(descriptor.indexInSection);
                if (!TextUtils.isEmpty(publicRoom.roomId)) {
                    return publicRoom.roomId.hashCode();
                }
                if (!TextUtils.isEmpty(publicRoom.canonicalAlias)) {
                    return publicRoom.canonicalAlias.hashCode();
                }
                return ("public:" + descriptor.indexInSection).hashCode();
            case VIEW_TYPE_HEADER:
                return ("header:" + descriptor.sectionTitle).hashCode();
            case VIEW_TYPE_PUBLIC_HEADER:
                return "public_header".hashCode();
            default:
                return position;
        }
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isEnabled(int position) {
        ItemDescriptor descriptor = resolvePosition(position);
        return descriptor != null && descriptor.viewType != VIEW_TYPE_HEADER && descriptor.viewType != VIEW_TYPE_PUBLIC_HEADER;
    }

    @Override
    public int getViewTypeCount() {
        return 5;
    }

    @Override
    public int getItemViewType(int position) {
        ItemDescriptor descriptor = resolvePosition(position);
        return descriptor != null ? descriptor.viewType : VIEW_TYPE_ROOM;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemDescriptor descriptor = resolvePosition(position);

        if (descriptor == null) {
            return convertView != null ? convertView : new View(mContext);
        }

        switch (descriptor.viewType) {
            case VIEW_TYPE_HEADER:
                return bindHeaderView(convertView, parent, descriptor.sectionTitle, descriptor.sectionCount);
            case VIEW_TYPE_INVITE:
                return bindInvitationView(convertView, parent, mFilteredInvitations.get(descriptor.indexInSection));
            case VIEW_TYPE_ROOM:
                return bindRoomView(convertView, parent, mFilteredRooms.get(descriptor.indexInSection));
            case VIEW_TYPE_PUBLIC_HEADER:
                return bindPublicHeaderView(convertView, parent);
            case VIEW_TYPE_PUBLIC_ROOM:
                return bindPublicRoomView(convertView, parent, mPublicRooms.get(descriptor.indexInSection));
            default:
                return convertView != null ? convertView : new View(mContext);
        }
    }

    public void setInvitation(@NonNull List<Room> invitations) {
        mInvitations.clear();
        mInvitations.addAll(invitations);
        applyFilterInternal();
        notifyDataSetChanged();
    }

    public void setRooms(@NonNull List<Room> rooms) {
        mRooms.clear();
        mRooms.addAll(rooms);
        Collections.sort(mRooms, RoomUtils.getRoomsDateComparator(mSession, false));
        applyFilterInternal();
        notifyDataSetChanged();
    }

    public void setPublicRooms(@NonNull List<PublicRoom> publicRooms) {
        mPublicRooms.clear();
        mPublicRooms.addAll(publicRooms);
        notifyDataSetChanged();
    }

    public void addPublicRooms(@NonNull List<PublicRoom> publicRooms) {
        mPublicRooms.addAll(publicRooms);
        notifyDataSetChanged();
    }

    public void setEstimatedPublicRoomsCount(int estimatedPublicRoomsCount) {
        mEstimatedPublicRoomsCount = estimatedPublicRoomsCount;
        notifyDataSetChanged();
    }

    public void setNoMorePublicRooms(boolean noMorePublicRooms) {
        notifyDataSetChanged();
    }

    public void setDirectoryAdapter(ArrayAdapter<CharSequence> roomDirectoryAdapter) {
        mRoomDirectoryAdapter = roomDirectoryAdapter;
        notifyDataSetChanged();
    }

    public void setDirectoryTouchListener(View.OnTouchListener publicRoomsTouchListener) {
        mPublicRoomsTouchListener = publicRoomsTouchListener;
        notifyDataSetChanged();
    }

    public void setSelectedDirectoryDisplayName(String selectedDirectoryDisplayName) {
        mSelectedDirectoryDisplayName = selectedDirectoryDisplayName;
        notifyDataSetChanged();
    }

    public int getPublicRoomsHeaderPosition() {
        int position = 0;
        if (!mFilteredInvitations.isEmpty()) {
            position += 1 + mFilteredInvitations.size();
        }
        if (!mFilteredRooms.isEmpty()) {
            position += 1 + mFilteredRooms.size();
        }
        return position;
    }

    public int getPublicRoomCount() {
        return mPublicRooms.size();
    }

    public int getFirstSelectablePosition() {
        for (int position = 0; position < getCount(); position++) {
            if (isEnabled(position)) {
                return position;
            }
        }
        return ListView.INVALID_POSITION;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                mCurrentFilterPattern = TextUtils.isEmpty(constraint) ? null : constraint.toString().trim();
                FilterResults filterResults = new FilterResults();
                filterResults.count = applyFilterInternal();
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                notifyDataSetChanged();
            }
        };
    }

    private int applyFilterInternal() {
        mFilteredInvitations.clear();
        mFilteredRooms.clear();

        if (TextUtils.isEmpty(mCurrentFilterPattern)) {
            mFilteredInvitations.addAll(mInvitations);
            mFilteredRooms.addAll(mRooms);
        } else {
            mFilteredInvitations.addAll(RoomUtils.getFilteredRooms(mContext, mInvitations, mCurrentFilterPattern.toString()));
            mFilteredRooms.addAll(RoomUtils.getFilteredRooms(mContext, mRooms, mCurrentFilterPattern.toString()));
        }

        return mFilteredInvitations.size() + mFilteredRooms.size();
    }

    private View bindHeaderView(View convertView, ViewGroup parent, String title, int count) {
        HeaderHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.adapter_section_header, parent, false);
            holder = new HeaderHolder();
            holder.titleView = convertView.findViewById(R.id.section_title);
            convertView.setTag(holder);
        } else {
            holder = (HeaderHolder) convertView.getTag();
        }

        String headerText = count > 0 ? title + "   " + count : title;
        SpannableString spannableString = new SpannableString(headerText.toUpperCase(VectorLocale.INSTANCE.getApplicationLocale()));
        if (count > 0) {
            spannableString.setSpan(
                    new ForegroundColorSpan(ThemeUtils.INSTANCE.getColor(mContext, R.attr.vctr_list_header_secondary_text_color)),
                    title.length(),
                    headerText.length(),
                    0
            );
        }
        holder.titleView.setText(spannableString);
        convertView.setFocusable(false);
        convertView.setClickable(false);
        return convertView;
    }

    private View bindPublicHeaderView(View convertView, ViewGroup parent) {
        PublicHeaderHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.adapter_section_header_public_room, parent, false);
            holder = new PublicHeaderHolder();
            holder.titleView = convertView.findViewById(R.id.section_title);
            holder.selector = convertView.findViewById(R.id.public_rooms_selector);
            convertView.setTag(holder);
        } else {
            holder = (PublicHeaderHolder) convertView.getTag();
        }

        int count = mPublicRooms.isEmpty() && mEstimatedPublicRoomsCount >= 0 ? mEstimatedPublicRoomsCount : mPublicRooms.size();
        String title = mContext.getString(R.string.rooms_directory_header);
        String headerText = count > 0 ? title + "   " + count : title;
        SpannableString spannableString = new SpannableString(headerText.toUpperCase(VectorLocale.INSTANCE.getApplicationLocale()));
        if (count > 0) {
            spannableString.setSpan(
                    new ForegroundColorSpan(ThemeUtils.INSTANCE.getColor(mContext, R.attr.vctr_list_header_secondary_text_color)),
                    title.length(),
                    headerText.length(),
                    0
            );
        }
        holder.titleView.setText(spannableString);

        if (mRoomDirectoryAdapter != null) {
            if (holder.selector.getAdapter() != mRoomDirectoryAdapter) {
                holder.selector.setAdapter(mRoomDirectoryAdapter);
            } else {
                mRoomDirectoryAdapter.notifyDataSetChanged();
            }

            if (!TextUtils.isEmpty(mSelectedDirectoryDisplayName) && mRoomDirectoryAdapter.getCount() == 0) {
                mRoomDirectoryAdapter.add(mSelectedDirectoryDisplayName);
            }
        }

        holder.selector.setOnTouchListener(mPublicRoomsTouchListener);
        convertView.setFocusable(false);
        convertView.setClickable(false);
        return convertView;
    }

    private View bindInvitationView(View convertView, ViewGroup parent, Room room) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.adapter_item_room_invite, parent, false);
            convertView.setTag(new RoomInvitationViewHolder(convertView));
        }

        RoomInvitationViewHolder viewHolder = (RoomInvitationViewHolder) convertView.getTag();
        viewHolder.populateViews(mContext, mSession, room, mInvitationListener, mMoreRoomActionListener);
        convertView.setFocusable(false);
        convertView.setClickable(false);
        return convertView;
    }

    private View bindRoomView(View convertView, ViewGroup parent, final Room room) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.adapter_item_room_view, parent, false);
            convertView.setTag(new RoomViewHolder(convertView));
        }

        RoomViewHolder viewHolder = (RoomViewHolder) convertView.getTag();
        viewHolder.populateViews(mContext, mSession, room, false, false, mMoreRoomActionListener);
        convertView.setOnClickListener(null);
        convertView.setFocusable(false);
        convertView.setClickable(false);
        return convertView;
    }

    private View bindPublicRoomView(View convertView, ViewGroup parent, final PublicRoom publicRoom) {
        PublicRoomHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.adapter_item_public_room_view, parent, false);
            holder = new PublicRoomHolder();
            holder.avatarView = convertView.findViewById(R.id.public_room_avatar);
            holder.nameView = convertView.findViewById(R.id.public_room_name);
            holder.topicView = convertView.findViewById(R.id.public_room_topic);
            holder.memberCountView = convertView.findViewById(R.id.public_room_members_count);
            convertView.setTag(holder);
        } else {
            holder = (PublicRoomHolder) convertView.getTag();
        }

        String roomName = !TextUtils.isEmpty(publicRoom.name) ? publicRoom.name : VectorUtils.getPublicRoomDisplayName(publicRoom);
        holder.avatarView.setBackgroundColor(ContextCompat.getColor(mContext, android.R.color.transparent));
        VectorUtils.loadUserAvatar(mContext, mSession, holder.avatarView, publicRoom.avatarUrl, publicRoom.roomId, roomName);
        holder.nameView.setText(roomName);
        holder.topicView.setText(publicRoom.topic);
        holder.memberCountView.setText(mContext.getResources().getQuantityString(
                R.plurals.public_room_nb_users,
                publicRoom.numJoinedMembers,
                publicRoom.numJoinedMembers
        ));

        convertView.setOnClickListener(null);
        convertView.setFocusable(false);
        convertView.setClickable(false);
        return convertView;
    }

    private ItemDescriptor resolvePosition(int position) {
        int cursor = position;

        if (!mFilteredInvitations.isEmpty()) {
            if (cursor == 0) {
                return new ItemDescriptor(VIEW_TYPE_HEADER, 0, mContext.getString(R.string.room_recents_invites), mFilteredInvitations.size());
            }
            cursor -= 1;
            if (cursor < mFilteredInvitations.size()) {
                return new ItemDescriptor(VIEW_TYPE_INVITE, cursor, null, 0);
            }
            cursor -= mFilteredInvitations.size();
        }

        if (!mFilteredRooms.isEmpty()) {
            if (cursor == 0) {
                return new ItemDescriptor(VIEW_TYPE_HEADER, 0, mContext.getString(R.string.rooms_header), mFilteredRooms.size());
            }
            cursor -= 1;
            if (cursor < mFilteredRooms.size()) {
                return new ItemDescriptor(VIEW_TYPE_ROOM, cursor, null, 0);
            }
            cursor -= mFilteredRooms.size();
        }

        if (cursor == 0) {
            return new ItemDescriptor(VIEW_TYPE_PUBLIC_HEADER, 0, mContext.getString(R.string.rooms_directory_header), mPublicRooms.size());
        }

        cursor -= 1;
        if (cursor < mPublicRooms.size()) {
            return new ItemDescriptor(VIEW_TYPE_PUBLIC_ROOM, cursor, null, 0);
        }

        return null;
    }

    private static final class HeaderHolder {
        TextView titleView;
    }

    private static final class PublicHeaderHolder {
        TextView titleView;
        Spinner selector;
    }

    private static final class PublicRoomHolder {
        ImageView avatarView;
        TextView nameView;
        TextView topicView;
        TextView memberCountView;
    }

    private static final class ItemDescriptor {
        final int viewType;
        final int indexInSection;
        final String sectionTitle;
        final int sectionCount;

        ItemDescriptor(int viewType, int indexInSection, String sectionTitle, int sectionCount) {
            this.viewType = viewType;
            this.indexInSection = indexInSection;
            this.sectionTitle = sectionTitle;
            this.sectionCount = sectionCount;
        }
    }
}