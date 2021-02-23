package com.example.eperpusapp.Model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ResponseCategory {

	@SerializedName("data")
	private List<String> data;

	public List<String> getData(){
		return data;
	}
}