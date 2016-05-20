package com.skplanet.trunk.carowner.common;


import android.database.Cursor;
import android.database.CursorJoiner;
import android.provider.BaseColumns;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

abstract public class CursorRecyclerViewAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    private static final String TAG = CursorRecyclerViewAdapter.class.getSimpleName();

    protected Cursor mCursor;
    protected ArrayList<Integer> mInsertedItems = new ArrayList<>();

    abstract public void onBindViewHolder(VH holder, Cursor data);

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(true);
    }

    public Cursor getItemByPosition(int position) {
        if (mCursor != null && !mCursor.isClosed()) {
            if (mCursor.moveToPosition(position)) {
                return mCursor;
            }
        }
        return null;
    }

    public void onBindViewHolder(VH holder, int position) {
        Cursor data = getItemByPosition(position);
        if (data == null) {
            return;
        }

        onBindViewHolder(holder, data);
    }

    @Override
    public int getItemCount() {
        if (mCursor == null || mCursor.isClosed()) {
            return 0;
        }
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        mInsertedItems.clear();
        Cursor oldCursor = mCursor;

        boolean notifyDataset = true;
        mCursor = newCursor;
        if (oldCursor != null && newCursor != null && !oldCursor.isClosed() && !newCursor.isClosed()) {
            notifyDataset = notifyItems(oldCursor, newCursor);
        }
        if (oldCursor != null && !oldCursor.isClosed()) {
            oldCursor.close();
        }
        if (notifyDataset) {
            notifyDataSetChanged();
        }
    }

    private boolean notifyItems(Cursor oldCursor, Cursor newCursor) {
        String[] columns = new String[]{
                "sortOrder"
        };
        CursorJoiner joiner = new CursorJoiner(oldCursor, columns, newCursor, columns);
        for (CursorJoiner.Result res : joiner) {
            switch (res) {
                case LEFT:
                    notifyItemRemoved(newCursor.getPosition());
                    break;
                case RIGHT:
                    mInsertedItems.add(newCursor.getPosition());
                    notifyItemInserted(newCursor.getPosition());
                    break;
                case BOTH:
                    if (getRowHash(oldCursor) != getRowHash(newCursor)) {
                        notifyItemChanged(newCursor.getPosition());
                    }
                    break;
            }
        }

        return false;
    }

    private static int getRowHash(Cursor cursor) {
        StringBuilder result = new StringBuilder("row");
        for (int i = 0; i < cursor.getColumnCount(); i++) {
            result.append(cursor.getString(i));
        }
        return result.toString().hashCode();
    }

    @Override
    public long getItemId(int position) {
        Cursor item = this.getItemByPosition(position);
        if (item != null) {
            return item.getInt(item.getColumnIndex(BaseColumns._ID));
        }
        return 0;
    }
}
