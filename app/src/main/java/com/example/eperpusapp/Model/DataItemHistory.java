package com.example.eperpusapp.Model;

import com.google.gson.annotations.SerializedName;

public class DataItemHistory {

	@SerializedName("foto_buku")
	private String fotoBuku;

	@SerializedName("idbuku")
	private int idbuku;

	@SerializedName("tanggal_pinjam")
	private String tanggalPinjam;

	@SerializedName("progress_baca")
	private int progressBaca;

	@SerializedName("idriwayat_pinjam")
	private int idriwayatPinjam;

	@SerializedName("judul_buku")
	private String judulBuku;

	@SerializedName("tanggal_dikembalikan")
	private String tanggalDikembalikan;

	@SerializedName("pengarang")
	private String pengarang;

	public String getFotoBuku(){
		return fotoBuku;
	}

	public int getIdbuku(){
		return idbuku;
	}

	public String getTanggalPinjam(){
		return tanggalPinjam;
	}

	public int getProgressBaca(){
		return progressBaca;
	}

	public int getIdriwayatPinjam(){
		return idriwayatPinjam;
	}

	public String getJudulBuku(){
		return judulBuku;
	}

	public String getTanggalDikembalikan(){
		return tanggalDikembalikan;
	}

	public String getPengarang(){
		return pengarang;
	}
}