package com.example.eperpusapp.Model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ResponseMyBook {

	@SerializedName("data")
	private List<DataItemMyBook> data;

	public List<DataItemMyBook> getData(){
		return data;
	}
}