package com.example.eperpusapp.Model;

import com.google.gson.annotations.SerializedName;

public class ResponseReturn {

	@SerializedName("message")
	private String message;

	public String getMessage(){
		return message;
	}
}