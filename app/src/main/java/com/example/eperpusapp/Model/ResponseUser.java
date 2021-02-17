package com.example.eperpusapp.Model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ResponseUser{

	@SerializedName("data")
	private DataItemUser data;

	@SerializedName("message")
	private String message;

	public DataItemUser getData(){
		return data;
	}

	public String getMessage(){
		return message;
	}
}