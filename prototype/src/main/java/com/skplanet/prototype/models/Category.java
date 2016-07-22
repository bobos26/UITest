package com.skplanet.prototype.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by 1001955 on 3/31/16.
 */

@Table(name = "Categories")
public class Category extends Model {

	// This is the unique id given by the server
	@Column(name = "remote_id", unique = true)
	public long remoteId;
	// This is a regular field
	@Column(name = "Name")
	public String name;

	// Make sure to have a default constructor for every ActiveAndroid model
	public Category() {
		super();
	}

	public static List<Category> getCategories() {
		return new Select().from(Category.class).orderBy("Name ASC").execute();
	}

	public static Category getCategory(long remoteId) {
		return new Select().from(Category.class).where("remote_id = ?", remoteId).executeSingle();
	}
}
