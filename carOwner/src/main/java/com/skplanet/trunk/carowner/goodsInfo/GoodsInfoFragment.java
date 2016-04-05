package com.skplanet.trunk.carowner.goodsInfo;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.activeandroid.content.ContentProvider;
import com.skplanet.trunk.carowner.R;
import com.skplanet.trunk.carowner.common.CursorRecyclerViewAdapter;
import com.skplanet.trunk.carowner.model.Goods;
import com.skplanet.trunk.carowner.model.GoodsModel;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import hugo.weaving.DebugLog;

public class GoodsInfoFragment extends Fragment {

    RecyclerCursorAdapter mAdapter;

    public GoodsInfoFragment() {
    }

    @Override
    public void setArguments(Bundle bundle) {
        super.setArguments(bundle);
    }

    @DebugLog
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_goods_info, container, false);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        RecyclerView recyclerView = (RecyclerView) getActivity().findViewById(R.id.fragment_recycler);
        mAdapter = new RecyclerCursorAdapter(getActivity(), null);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        getLoaderManager().initLoader(0, new Bundle(), mLoaderCallback);
    }

    private LoaderManager.LoaderCallbacks<Cursor> mLoaderCallback = new LoaderManager.LoaderCallbacks<Cursor>() {

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new CursorLoader(getActivity(), ContentProvider.createUri(Goods.class, null),
                    null, // projection
                    null, // selection
                    null, // selectionArgs
                    null);// sortOrder
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
            mAdapter.swapCursor(cursor);
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            mAdapter.swapCursor(null);
        }
    };

    public class RecyclerCursorAdapter extends CursorRecyclerViewAdapter<RecyclerCursorAdapter.ViewHolder> {

        public RecyclerCursorAdapter(Context context, Cursor cursor) {
            super(context, cursor);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);

            View view = inflater.inflate(R.layout.list_item, parent, false);

            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {
            Goods goods = GoodsModel.fromCursor(cursor);

            viewHolder.liftArea.setText(goods.getFullLiftArea());
            viewHolder.landArea.setText(goods.landArea);
            viewHolder.fee.setText(Integer.toString(goods.fee));
            viewHolder.ton.setText(goods.ton);
            viewHolder.carType.setText(goods.carType);

            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
            viewHolder.dateTime.setText(sdf.format(new Date(goods.time)));
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            @Bind(R.id.lift_area_text)
            TextView liftArea;
            @Bind(R.id.land_area_text)
            TextView landArea;
            @Bind(R.id.datetime_text)
            TextView dateTime;
            @Bind(R.id.fee_text)
            TextView fee;
            @Bind(R.id.ton_text)
            TextView ton;
            @Bind(R.id.car_type_text)
            TextView carType;
            @Bind(R.id.description_text)
            TextView descript;

            public ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }
}
