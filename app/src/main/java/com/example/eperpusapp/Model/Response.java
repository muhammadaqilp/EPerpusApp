package com.example.eperpusapp.Model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Response{

	@SerializedName("data")
	private List<DataItem> data;

	public List<DataItem> getData(){
		return data;
	}
}