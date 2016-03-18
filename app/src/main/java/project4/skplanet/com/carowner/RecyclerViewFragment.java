package project4.skplanet.com.carowner;

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

import project4.skplanet.com.carowner.model.BLERegion;

public class RecyclerViewFragment extends Fragment {

    RecyclerCursorAdapter adapter;

	public RecyclerViewFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.recycler_fragment, container, false);

		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		RecyclerView rvContacts = (RecyclerView) getActivity().findViewById(R.id.rvContacts);

		// Initialize contacts
		// Create adapter passing in the sample user data
		adapter = new RecyclerCursorAdapter(getActivity(), null);
		// Attach the adapter to the recyclerview to populate items
		rvContacts.setAdapter(adapter);
		// Set layout manager to position the items
		rvContacts.setLayoutManager(new LinearLayoutManager(getActivity()));

		getLoaderManager().initLoader(0, new Bundle(), contactLoader);
	}

	private LoaderManager.LoaderCallbacks<Cursor> contactLoader = new LoaderManager.LoaderCallbacks<Cursor>() {

		@Override
		public Loader<Cursor> onCreateLoader(int id, Bundle args) {
			return new CursorLoader(getActivity(), ContentProvider.createUri(BLERegion.class, null), null, // projection
					null, // selection
					null, // selectionArgs
					null);// sortOrder
		}

		@Override
		public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
			adapter.swapCursor(cursor);
		}

		@Override
		public void onLoaderReset(Loader<Cursor> loader) {
			adapter.swapCursor(null);
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
}
