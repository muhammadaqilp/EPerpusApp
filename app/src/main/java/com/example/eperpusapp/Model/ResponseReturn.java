package com.example.eperpusapp.Model;

import com.google.gson.annotations.SerializedName;

public class ResponseReturn {

	@SerializedName("message")
	private String message;

	@SerializedName("idpinjam")
	private int idPinjam;

	public String getMessage(){
		return message;
	}

	public int getIdPinjam() {
		return idPinjam;
	}
}