package com.skplanet.trunk.carowner.SecondPage;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.skplanet.trunk.carowner.R;

import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.animators.BaseItemAnimator;
import jp.wasabeef.recyclerview.animators.FadeInAnimator;
import jp.wasabeef.recyclerview.animators.FadeInDownAnimator;
import jp.wasabeef.recyclerview.animators.FadeInLeftAnimator;
import jp.wasabeef.recyclerview.animators.FadeInRightAnimator;
import jp.wasabeef.recyclerview.animators.FadeInUpAnimator;
import jp.wasabeef.recyclerview.animators.FlipInBottomXAnimator;
import jp.wasabeef.recyclerview.animators.FlipInLeftYAnimator;
import jp.wasabeef.recyclerview.animators.FlipInRightYAnimator;
import jp.wasabeef.recyclerview.animators.FlipInTopXAnimator;
import jp.wasabeef.recyclerview.animators.LandingAnimator;
import jp.wasabeef.recyclerview.animators.OvershootInLeftAnimator;
import jp.wasabeef.recyclerview.animators.OvershootInRightAnimator;
import jp.wasabeef.recyclerview.animators.ScaleInAnimator;
import jp.wasabeef.recyclerview.animators.ScaleInBottomAnimator;
import jp.wasabeef.recyclerview.animators.ScaleInLeftAnimator;
import jp.wasabeef.recyclerview.animators.ScaleInRightAnimator;
import jp.wasabeef.recyclerview.animators.ScaleInTopAnimator;
import jp.wasabeef.recyclerview.animators.SlideInDownAnimator;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
import jp.wasabeef.recyclerview.animators.SlideInRightAnimator;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class ShowOrdersInfoFragment extends Fragment implements OnClickListener {

    @Bind(R.id.recyclerview)
    RecyclerView recyclerView;

    @Bind(R.id.animationSpinner)
    Spinner animationSpinner;

    @Bind(R.id.resetAnimationButton)
    Button resetAnimationButton;

    @Bind(R.id.insertDataButton)
    Button insertDataButton;

    @Bind(R.id.removeDataButton)
    Button removeDataButton;

    LinearLayoutManager layoutManager;
    RecyclerViewAdapter adapter;
    RegisterGoodsData data;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_orders_info, container, false);
        ButterKnife.bind(this, view);

        initView();

        data = new RegisterGoodsData();
        data.startingCity = "startingCity";
        data.startingDetailAddress = "startingDetailAddress";
        data.startingWideArea = "startingWideArea";
        data.carCapacity = "carCapacity";
        data.carCount = "carCount";
        data.carType = "carType";
        data.fee = "fee";
        data.destinationAddress = "destinationAddress";
        data.registerTime = "registerTime";
        data.goodsDetails = "goodsDetails";

        return view;
    }

    private void initView() {
        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RecyclerViewAdapter(getContext(), RegisterGoodsData.selectAll());
        recyclerView.setAdapter(adapter);

        setAnimationSpinner();
        setDefaultAnimation();

        insertDataButton.setOnClickListener(this);
        removeDataButton.setOnClickListener(this);
        resetAnimationButton.setOnClickListener(this);
    }

    enum Type {
        FadeIn(new FadeInAnimator(new OvershootInterpolator(1f))),
        FadeInDown(new FadeInDownAnimator(new OvershootInterpolator(1f))),
        FadeInUp(new FadeInUpAnimator(new OvershootInterpolator(1f))),
        FadeInLeft(new FadeInLeftAnimator(new OvershootInterpolator(1f))),
        FadeInRight(new FadeInRightAnimator(new OvershootInterpolator(1f))),
        Landing(new LandingAnimator(new OvershootInterpolator(1f))),
        ScaleIn(new ScaleInAnimator(new OvershootInterpolator(1f))),
        ScaleInTop(new ScaleInTopAnimator(new OvershootInterpolator(1f))),
        ScaleInBottom(new ScaleInBottomAnimator(new OvershootInterpolator(1f))),
        ScaleInLeft(new ScaleInLeftAnimator(new OvershootInterpolator(1f))),
        ScaleInRight(new ScaleInRightAnimator(new OvershootInterpolator(1f))),
        FlipInTopX(new FlipInTopXAnimator(new OvershootInterpolator(1f))),
        FlipInBottomX(new FlipInBottomXAnimator(new OvershootInterpolator(1f))),
        FlipInLeftY(new FlipInLeftYAnimator(new OvershootInterpolator(1f))),
        FlipInRightY(new FlipInRightYAnimator(new OvershootInterpolator(1f))),
        SlideInLeft(new SlideInLeftAnimator(new OvershootInterpolator(1f))),
        SlideInRight(new SlideInRightAnimator(new OvershootInterpolator(1f))),
        SlideInDown(new SlideInDownAnimator(new OvershootInterpolator(1f))),
        SlideInUp(new SlideInUpAnimator(new OvershootInterpolator(1f))),
        OvershootInRight(new OvershootInRightAnimator(1.0f)),
        OvershootInLeft(new OvershootInLeftAnimator(1.0f));

        private BaseItemAnimator mAnimator;

        Type(BaseItemAnimator animator) {
            mAnimator = animator;
        }

        public BaseItemAnimator getAnimator() {
            return mAnimator;
        }
    }

    private void setAnimationSpinner() {
        ArrayAdapter<String> spinnerAdapter =
                new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1);
        for (Type type : Type.values()) {
            spinnerAdapter.add(type.name());
        }
        animationSpinner.setAdapter(spinnerAdapter);
        animationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                recyclerView.setItemAnimator(Type.values()[position].getAnimator());
                recyclerView.getItemAnimator().setAddDuration(500);
                recyclerView.getItemAnimator().setRemoveDuration(500);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setDefaultAnimation() {
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);
        recyclerView.setItemAnimator(itemAnimator);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.resetAnimationButton:
                setDefaultAnimation();
                break;
            case R.id.insertDataButton:
                adapter.insertToFront(data);
                recyclerView.scrollToPosition(0);
                break;
            case R.id.removeDataButton:
                adapter.remove(data);
                break;
        }
    }

    class RecyclerViewAdapter extends RecyclerView.Adapter<OrderInfoViewHolder> {

        private List<RegisterGoodsData> listViewItemList = Collections.emptyList();
        private Context context;

        public RecyclerViewAdapter(Context context, List<RegisterGoodsData> listViewItemList) {
            this.context = context;
            this.listViewItemList = listViewItemList;
        }

        @Override
        public OrderInfoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_orders_info, parent, false);
            OrderInfoViewHolder holder = new OrderInfoViewHolder(v);

            return holder;

        }

        @Override
        public void onBindViewHolder(OrderInfoViewHolder holder, final int position) {

            // Use the provided View Holder on the onCreateViewHolder method
            // to populate the current row on the RecyclerView
            RegisterGoodsData listViewItem = listViewItemList.get(position);

            holder.startingPointTextView.setText(
                    new StringBuffer(listViewItem.startingWideArea).append(" ").append(listViewItem.startingCity));
            holder.registeredTimeTextView.setText(listViewItem.registerTime);
            holder.destinationPointTextView.setText(listViewItem.destinationAddress);
            holder.feeTextView.setText(listViewItem.fee);
            holder.capacityTextView.setText(String.format(getString(R.string.ton), listViewItem.carCapacity));
            holder.carTypeTextView.setText(listViewItem.carType);
            holder.goodsDetailTextView.setText(listViewItem.goodsDetails);

            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "Recycle Click" + position, Toast.LENGTH_SHORT).show();
                }
            });

            // animate(holder);
        }

        private void animate(RecyclerView.ViewHolder viewHolder) {
            final Animation animAnticipateOvershoot = AnimationUtils.loadAnimation(context, R.anim.bounce_interpolator);
            viewHolder.itemView.setAnimation(animAnticipateOvershoot);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return listViewItemList.size();
        }

        public void insertToFront(RegisterGoodsData listViewItem) {
            this.listViewItemList.add(0, listViewItem);
            notifyItemInserted(0);

        }

        public void remove(RegisterGoodsData listViewItem) {
            int position = listViewItemList.indexOf(listViewItem);
            try {
                listViewItemList.remove(position);
                notifyItemRemoved(position);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }
    }

}
