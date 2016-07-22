package com.skplanet.prototype.activeAndroid;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.skplanet.prototype.R;
import com.skplanet.prototype.models.Category;
import com.skplanet.prototype.models.Item;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 1001955 on 3/30/16.
 */
public class ActiveAndroidActivity extends Activity implements View.OnClickListener {

	@Bind(R.id.idEditText)
	public EditText idEditText;
	@Bind(R.id.itemEditText)
	public EditText itemEditText;
	@Bind(R.id.categoryEditText)
	public EditText categoryEditText;

	@Bind(R.id.createCategoryItemButton)
	public Button createItemButton;
	@Bind(R.id.deleteItemByIdButton)
	public Button deleteItemButton;
	@Bind(R.id.queryAllItems)
	public Button queryAllItems;
	@Bind(R.id.queryAllCategories)
	public Button queryAllCategories;

	@Bind(R.id.queryResultTextView)
	public TextView queryResultTextView;
	@Bind(R.id.queryResultLinearLayout)
	public LinearLayout queryResultLinearLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_active_android);
		ButterKnife.bind(this);

		createItemButton.setOnClickListener(this);
		deleteItemButton.setOnClickListener(this);
		queryAllItems.setOnClickListener(this);
		queryAllCategories.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
			switch (v.getId()) {
				case R.id.createCategoryItemButton:
					long remoteId = Long.valueOf(idEditText.getText().toString());

					// Create a category
					Category restaurants = new Category();
					restaurants.remoteId = remoteId;
					restaurants.name = categoryEditText.getText().toString();
					restaurants.save();

					// Create an item
					Item item4Create = new Item();
					item4Create.remoteId = remoteId;
					item4Create.category = Category.getCategory(remoteId);
					item4Create.name = itemEditText.getText().toString();
					item4Create.save();

					if (restaurants == item4Create.category) {
						Log.i("JINA", "[+]restaurants: " + restaurants.toString());
						Log.i("JINA", "[+]item4Create.category: " + item4Create.category.toString());
					} else {
						Log.i("JINA", "[-]restaurants: " + restaurants.toString());
						Log.i("JINA", "[-]item4Create.category: " + item4Create.category.toString());
					}
					break;

				case R.id.deleteItemByIdButton:
					long deleteId = Long.valueOf(idEditText.getText().toString());

					// Deleting items
					Item item4Delete = Item.load(Item.class, deleteId);
					item4Delete.delete();

					// or with
					// new Delete().from(Item.class).where("remote_id = ?", 1).execute();
					break;

				case R.id.queryAllItems:
					queryResultLinearLayout.setVisibility(View.VISIBLE);
					String resultItems = "";
					List<Item> itemList = Item.getAllItems();
					for (Item item : itemList) {
						resultItems = resultItems + "remoteId: " + item.remoteId + ", id: " + item.getId() + ", name: "
								+ item.name + ", " + "category: " + item.category + "\n";
					}
					queryResultTextView.setText(resultItems);
					break;

				case R.id.queryAllCategories:
					queryResultLinearLayout.setVisibility(View.VISIBLE);
					String resultCategories = "";
					List<Category> categoryList = Category.getCategories();
					for (Category category : categoryList) {
						resultCategories = resultCategories + "remoteId: " + category.remoteId + ", id: " + category.getId()
								+ ", category: " + category.name + "\n";
					}
					queryResultTextView.setText(resultCategories);
					break;
			}
		}
}
