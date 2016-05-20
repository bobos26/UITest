package com.skplanet.trunk.carowner.goodsInfo;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.skplanet.trunk.carowner.R;
import com.skplanet.trunk.carowner.common.CursorRecyclerViewAdapter;
import com.skplanet.trunk.carowner.common.MainThreadImpl;
import com.skplanet.trunk.carowner.model.Goods;
import com.skplanet.trunk.carowner.model.GoodsModel;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import hugo.weaving.DebugLog;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

public class GoodsInfoFragment extends Fragment implements GoodsInfoPresenter.IGoodsInfoView, Spinner.OnItemSelectedListener {

    private static final String TAG = GoodsInfoFragment.class.getSimpleName();
    RecyclerCursorAdapter mAdapter;
    GoodsInfoPresenter mPresenter;
    @Bind(R.id.locationSpinner)
    Spinner mLocationSpinner;
    @Bind(R.id.tonTypeSpinner)
    Spinner mTonTypeSpinner;
    @Bind(R.id.homeButton)
    Button mHomeButton;

    RecyclerView mRecyclerView;
    ArrayAdapter<String> mLocationAdapter;
    String mSelectedLocation, mSelectedTonType;
    String[] mTonTypes;


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
    public void onDestroyView() {
        super.onDestroyView();
        if (mAdapter != null) {
            mAdapter.destroyView();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mRecyclerView = (RecyclerView) getActivity().findViewById(R.id.goodInfoRecyclerView);
        RelativeLayout rootLayout = (RelativeLayout) getActivity().findViewById(R.id.fragment_recycler);
        ButterKnife.bind(this, rootLayout);

        mPresenter = new GoodsInfoPresenter(MainThreadImpl.getInstance(), this);
        mHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        mTonTypes = getResources().getStringArray(R.array.ton_type);
        mLocationAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_dropdown_item,
                mPresenter.getSi());
        mLocationSpinner.setAdapter(mLocationAdapter);
        mLocationSpinner.setOnItemSelectedListener(this);
        mTonTypeSpinner.setOnItemSelectedListener(this);

        mAdapter = new RecyclerCursorAdapter();
        mRecyclerView.setAdapter(mAdapter);
        SlideInLeftAnimator animator = new SlideInLeftAnimator();
        animator.setInterpolator(new OvershootInterpolator());
        mRecyclerView.setItemAnimator(animator);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        getLoaderManager().initLoader(0, new Bundle(), mLoaderCallback);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // TODO spinner가 2개이면, onCreate시 onItemSelected가 2번 호출된다.
        switch (parent.getId()) {
            case R.id.locationSpinner:
                mSelectedLocation = mLocationAdapter.getItem(position);
                getLoaderManager().restartLoader(0, new Bundle(), mLoaderCallback);
                break;
            case R.id.tonTypeSpinner:
                mSelectedTonType = mTonTypes[position];
                getLoaderManager().restartLoader(0, new Bundle(), mLoaderCallback);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    private LoaderManager.LoaderCallbacks<Cursor> mLoaderCallback = new LoaderManager.LoaderCallbacks<Cursor>() {

        @DebugLog
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return mPresenter.getCursorLoader(getContext(), mSelectedLocation, mSelectedTonType);
        }

        @DebugLog
        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
            mAdapter.swapCursor(cursor);
            mRecyclerView.scrollToPosition(0);
        }

        @DebugLog
        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            mAdapter.swapCursor(null);
        }
    };

    public class RecyclerCursorAdapter extends CursorRecyclerViewAdapter<RecyclerCursorAdapter.ViewHolder> implements GoodsModel.ICallback {

        private static final int MSG_TOGGLE_ITEM = -1;

        private Handler mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MSG_TOGGLE_ITEM:
                        notifyItemChanged(msg.arg1);
                        ViewHolder viewHolder = (ViewHolder) msg.obj;
                        if (viewHolder != null) {
                            viewHolder.toggled = false;
                        }
                        break;
                    default:
                        break;
                }
            }
        };
        // sendMessageDelayed(2초)를 쓰고 있으므로, 종료시 대기중인 Message들을 다 종료시켜 줘야한다.
        public void destroyView() {
            if (mHandler != null) {
                mHandler.removeMessages(MSG_TOGGLE_ITEM);
                mHandler = null;
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);

            View view = inflater.inflate(R.layout.list_item, parent, false);

            ViewHolder viewHolder = new ViewHolder(view);
            viewHolder.toggled = true;
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, Cursor cursor) {
            Goods goods = GoodsModel.fromCursor(cursor);

            viewHolder.liftArea.setText(goods.getFullLiftArea());
            viewHolder.landArea.setText(goods.landArea);
            // 운송료를 표시 Format with Comma(,)
            NumberFormat numberFormat = NumberFormat.getCurrencyInstance(Locale.KOREA);
            viewHolder.fee.setText(numberFormat.format(goods.fee));
            viewHolder.ton.setText(goods.ton);
            viewHolder.carType.setText(goods.carType);

            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm:ss");
            viewHolder.dateTime.setText(sdf.format(new Date(goods.time)));

            final int cursorPosition = cursor.getPosition();
            if (mInsertedItems.contains(cursorPosition)) {
                viewHolder.listItemRoot.setBackgroundColor(Color.YELLOW);
                viewHolder.toggled = true;
                mInsertedItems.remove(Integer.valueOf(cursorPosition));
                if (mHandler != null) {
                    Message msg = mHandler.obtainMessage(MSG_TOGGLE_ITEM, cursorPosition, 0, viewHolder);
                    mHandler.sendMessageDelayed(msg, 2000);
                }
            }
            if (!viewHolder.toggled) {
                viewHolder.listItemRoot.setBackgroundColor(Color.WHITE);
            }
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            @Bind(R.id.list_item_root)
            RelativeLayout listItemRoot;
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
            boolean toggled;

            public ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }

        @Override
        public void insertAllCompleted() {
        }

        @Override
        public void deleteAllCompleted() {
        }
    }
}
