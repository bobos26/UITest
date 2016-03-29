package project4.skplanet.com.carowner.Main;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.activeandroid.content.ContentProvider;
import com.facebook.stetho.Stetho;

import butterknife.Bind;
import butterknife.ButterKnife;
import project4.skplanet.com.carowner.CursorRecyclerViewAdapter;
import project4.skplanet.com.carowner.ListActivity;
import project4.skplanet.com.carowner.NavigationDrawerActivity;
import project4.skplanet.com.carowner.NetUtils;
import project4.skplanet.com.carowner.R;
import project4.skplanet.com.carowner.RecyclerActivity;
import project4.skplanet.com.carowner.model.BLEModel;
import project4.skplanet.com.carowner.model.BLERegion;

public class MainActivity extends AppCompatActivity implements MainPresenter.IMainView {
    private static final String TAG = MainActivity.class.getSimpleName();

    @Bind(R.id.major_edit)
    EditText mMajorEdit;

    @Bind(R.id.minor_edit)
    EditText mMinorEdit;

    @Bind(R.id.mid_edit)
    EditText mMidEdit;

    @Bind(R.id.save_button)
    Button mSaveBtn;

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;

    MainPresenter mPresenter;
    CursorRecyclerViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mPresenter = new MainPresenter(this, new BLEModel());

        mAdapter = new RecyclerCursorAdapter(this, null);
        // Attach the adapter to the recyclerview to populate items
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Set layout manager to position the items
        getSupportLoaderManager().initLoader(0, new Bundle(), contactLoader);

        Stetho.initializeWithDefaults(this);
    }

    public void onSaveClick(View v) {
        Log.e(TAG, "saveClicked...");
        int major = Integer.valueOf(mMajorEdit.getText().toString());
        int minor = Integer.valueOf(mMinorEdit.getText().toString());
        String mid = mMidEdit.getText().toString();

        mPresenter.addBLERegion(major, minor, mid);
    }

    @Override
    public void dataReloaded() {
        Log.e(TAG, "dateReloaded()...");
        mAdapter.notifyDataSetChanged();
    }

    private LoaderManager.LoaderCallbacks<Cursor> contactLoader = new LoaderManager.LoaderCallbacks<Cursor>() {

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new CursorLoader(MainActivity.this,
                    ContentProvider.createUri(BLERegion.class, null),
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
            BLERegion bleRegion = BLERegion.fromCursor(cursor);

            viewHolder.tvMajor.setText(Integer.toString(bleRegion.major));
            viewHolder.tvMinor.setText(Integer.toString(bleRegion.minor));
            viewHolder.tvMid.setText(bleRegion.mid);
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvMajor;
            TextView tvMinor;
            TextView tvMid;

            public ViewHolder(View itemView) {
                super(itemView);

                tvMajor = (TextView) itemView.findViewById(R.id.tv_major);
                tvMinor = (TextView) itemView.findViewById(R.id.tv_minor);
                tvMid = (TextView) itemView.findViewById(R.id.tv_mid);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_tmap) {
            startActivity(new Intent(this, NavigationDrawerActivity.class));
            return true;
        } else if (id == R.id.action_list) {
            startActivity(new Intent(this, ListActivity.class));
            return true;
        } else if (id == R.id.action_recycler) {
            startActivity(new Intent(this, RecyclerActivity.class));
            return true;
        } else if (id == R.id.action_initialize) {
            NetUtils.initialize(this);
            return true;
        } else if (id == R.id.action_request_token) {
            NetUtils.requestToken(this);
            return true;
        } else if (id == R.id.action_nearest) {
            NetUtils.nearest(this, null);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
