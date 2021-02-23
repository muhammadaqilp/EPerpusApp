package com.example.eperpusapp.Model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ResponseBuku {

	@SerializedName("data")
	private List<DataItemBuku> data;

	@SerializedName("message")
	private String message;

	public List<DataItemBuku> getData(){
		return data;
	}

	public String getMessage(){
		return message;
	}
}