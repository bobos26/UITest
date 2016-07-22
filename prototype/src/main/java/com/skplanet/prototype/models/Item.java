package com.skplanet.prototype.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by 1001955 on 3/31/16.
 */

@Table(name = "Items")
public class Item extends Model {

	// This is the unique id given by the server
	@Column(name = "remote_id", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
	public long remoteId;
	// This is a regular field
	@Column(name = "Name")
	public String name;
	// This is an association to another active android model
	@Column(name = "Category", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
	public Category category;

	// Make sure to have a default constructor for every ActiveAndroid model
	public Item() {
		super();
	}

	public static List<Item> getAllItems() {
		// This is how you execute a query
		return new Select().from(Item.class).orderBy("Name ASC").execute();
	}
}
