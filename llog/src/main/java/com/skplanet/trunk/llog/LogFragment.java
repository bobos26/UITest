package com.skplanet.trunk.llog;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.activeandroid.content.ContentProvider;


public class LogFragment extends Fragment {
    public static final String TAG = "LogFragment";
    private static final String ARG_PARAM1 = "param1";

    private String mParam1;
    private OnFragmentInteractionListener mListener;
    private ListView mListView;
    private MyCursorAdapter mAdapter;

    public LogFragment() {
        // Required empty public constructor
    }

    public static LogFragment newInstance(String param1) {
        LogFragment fragment = new LogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_log, container, false);

        mAdapter = new MyCursorAdapter(getContext(), null);
        mListView = (ListView) view.findViewById(R.id.listView);
        mListView.setAdapter(mAdapter);
        getLoaderManager().initLoader(0, new Bundle(), mLoaderCallback);

        return view;
    }

    public void clearList() {
        LogInfo.deleteAll();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private LoaderManager.LoaderCallbacks<Cursor> mLoaderCallback = new LoaderManager.LoaderCallbacks<Cursor>() {
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return LogInfo.getCursorLoader(getContext());
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            mAdapter.swapCursor(data);
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            mAdapter.swapCursor(null);
        }
    };

    public class MyCursorAdapter extends CursorAdapter {

        private class ViewHolder {
            TextView tvInsertedDate;
            TextView tvLog;
        }

        public MyCursorAdapter(Context context, Cursor c) {
            super(context, c, 0);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_2, parent, false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            LogInfo logInfo = LogInfo.fromCursor(cursor);

            ViewHolder viewHolder;
            if (view.getTag() == null) {
                viewHolder = new ViewHolder();
                viewHolder.tvInsertedDate = (TextView) view.findViewById(android.R.id.text1);
                viewHolder.tvLog = (TextView) view.findViewById(android.R.id.text2);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            viewHolder.tvInsertedDate.setText(logInfo.insertedDate);
            viewHolder.tvLog.setText(logInfo.log);
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
