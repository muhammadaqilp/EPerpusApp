package com.example.eperpusapp.Model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ResponseBuku {

	@SerializedName("data")
	private List<DataItemBuku> data;

	public List<DataItemBuku> getData(){
		return data;
	}
}