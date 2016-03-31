package project4.skplanet.com.carowner;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.activeandroid.content.ContentProvider;
import com.facebook.stetho.Stetho;

import java.lang.ref.WeakReference;

import project4.skplanet.com.carowner.common.NetUtils;
import project4.skplanet.com.carowner.model.BLERegion;

public class ListActivity extends Activity {
    private static final String TAG = ListActivity.class.getSimpleName();
    private static CustomHandler mHandler;

    private static class CustomHandler extends Handler {
        WeakReference<Context> contextWeakReference;
        public CustomHandler(Context context) {
            super();
            this.contextWeakReference = new WeakReference<>(context);
        }

        @Override
        public void handleMessage(Message msg) {
            Context context = contextWeakReference.get();
            if (context == null) {
                return;
            }

            String nextUrl = (String) msg.obj;
            NetUtils.nearest(context, nextUrl);
            super.handleMessage(msg);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Stetho.initializeWithDefaults(this);

        mHandler = new CustomHandler(getBaseContext());
        FragmentManager fm = getFragmentManager();

        // Create the list fragment and add it as our sole content.
        if (fm.findFragmentById(android.R.id.content) == null) {
            CursorLoaderListFragment list = new CursorLoaderListFragment();
            fm.beginTransaction().add(android.R.id.content, list).commit();
        }
    }

    public static class CustomCursorAdapter extends CursorAdapter {

        public CustomCursorAdapter(Context context, Cursor c) {
            super(context, c);
        }

        private static class ViewHolder {
            TextView tvMajor;
            TextView tvMinor;
            TextView tvMid;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            ViewHolder viewHolder = (ViewHolder) view.getTag();

            String major = cursor.getString(cursor.getColumnIndexOrThrow("major"));
            String minor = cursor.getString(cursor.getColumnIndexOrThrow("minor"));
            String mid = cursor.getString(cursor.getColumnIndexOrThrow("mid"));

            viewHolder.tvMajor.setText(major);
            viewHolder.tvMinor.setText(minor);
            viewHolder.tvMid.setText(mid);

            try {
                String nextUrl = cursor.getString(cursor.getColumnIndexOrThrow("nextUrl"));
                if (!TextUtils.isEmpty(nextUrl)) {
                    int _major = cursor.getInt(cursor.getColumnIndexOrThrow("major"));
                    int _minor = cursor.getInt(cursor.getColumnIndexOrThrow("minor"));
                    new BLERegion(_major, _minor).update(null);

                    NetUtils.nearest(context, nextUrl);
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View rowView = inflater.inflate(R.layout.list_item, parent, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.tvMajor = (TextView) rowView.findViewById(R.id.tv_major);
            viewHolder.tvMinor = (TextView) rowView.findViewById(R.id.tv_minor);
            viewHolder.tvMid = (TextView) rowView.findViewById(R.id.tv_mid);
            rowView.setTag(viewHolder);

            return rowView;
        }
    }

    public static class CursorLoaderListFragment extends ListFragment
            implements SearchView.OnQueryTextListener, SearchView.OnCloseListener,
            LoaderManager.LoaderCallbacks<Cursor> {

        CustomCursorAdapter mAdapter;

        SearchView mSearchView;

        String mCurFilter;

        @Override public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            // In this sample we are going to use a retained fragment.
            setRetainInstance(true);

            setEmptyText("No phone numbers");

            setHasOptionsMenu(true);

            mAdapter = new CustomCursorAdapter(getActivity(), null);

            setListAdapter(mAdapter);

            setListShown(false);

            getLoaderManager().initLoader(0, null, this);
        }

        public static class MySearchView extends SearchView {
            public MySearchView(Context context) {
                super(context);
            }

            @Override
            public void onActionViewCollapsed() {
                setQuery("", false);
                super.onActionViewCollapsed();
            }
        }

        @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            MenuItem item = menu.add("Search");
            item.setIcon(android.R.drawable.ic_menu_search);
            item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM
                    | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
            mSearchView = new MySearchView(getActivity());
            mSearchView.setOnQueryTextListener(this);
            mSearchView.setOnCloseListener(this);
            mSearchView.setIconifiedByDefault(true);
            item.setActionView(mSearchView);
        }

        public boolean onQueryTextChange(String newText) {
            String newFilter = !TextUtils.isEmpty(newText) ? newText : null;
            if (mCurFilter == null && newFilter == null) {
                return true;
            }
            if (mCurFilter != null && mCurFilter.equals(newFilter)) {
                return true;
            }
            mCurFilter = newFilter;
            getLoaderManager().restartLoader(0, null, this);
            return true;
        }

        @Override public boolean onQueryTextSubmit(String query) {
            // Don't care about this.
            return true;
        }

        @Override
        public boolean onClose() {
            if (!TextUtils.isEmpty(mSearchView.getQuery())) {
                mSearchView.setQuery(null, true);
            }
            return true;
        }

        @Override
        public void onListItemClick(ListView l, View v, int position, long id) {
            // Insert desired behavior here.
            Log.i("FragmentComplexList", "Item clicked: " + id);
        }

        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new CursorLoader(getActivity(),
                    ContentProvider.createUri(BLERegion.class, null),
                    null, // projection
                    null, // selection
                    null, // selectionArgs
                    null);// sortOrder
        }

        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            mAdapter.swapCursor(data);

            // The list should now be shown.
            if (isResumed()) {
                setListShown(true);
            } else {
                setListShownNoAnimation(true);
            }
        }

        public void onLoaderReset(Loader<Cursor> loader) {
            mAdapter.swapCursor(null);
        }
    }

}
