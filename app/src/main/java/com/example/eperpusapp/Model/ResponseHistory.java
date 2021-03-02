package com.example.eperpusapp.Model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ResponseHistory {

	@SerializedName("data")
	private List<DataItemHistory> data;

	public List<DataItemHistory> getData(){
		return data;
	}
}