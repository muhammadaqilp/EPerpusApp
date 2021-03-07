package com.example.eperpusapp.Model;

import com.google.gson.annotations.SerializedName;

public class DataItemMyBook {

	@SerializedName("file_buku")
	private String fileBuku;

	@SerializedName("foto_buku")
	private String fotoBuku;

	@SerializedName("tanggal_pinjam")
	private String tanggalPinjam;

	@SerializedName("progress_baca")
	private int progressBaca;

	@SerializedName("jumlah_halaman")
	private int jumlahHalaman;

	@SerializedName("idbuku")
	private int idBuku;

	@SerializedName("idsedang_pinjam")
	private int idPinjam;

	@SerializedName("tanggal_kembali")
	private String tanggalKembali;

	@SerializedName("judul_buku")
	private String judulBuku;

	@SerializedName("pengarang")
	private String pengarang;

	public String getFileBuku(){
		return fileBuku;
	}

	public String getFotoBuku(){
		return fotoBuku;
	}

	public String getTanggalPinjam(){
		return tanggalPinjam;
	}

	public int getProgressBaca(){
		return progressBaca;
	}

	public String getTanggalKembali(){
		return tanggalKembali;
	}

	public String getJudulBuku(){
		return judulBuku;
	}

	public String getPengarang(){
		return pengarang;
	}

	public int getIdBuku() {
		return idBuku;
	}

	public int getIdPinjam() {
		return idPinjam;
	}

	public int getJumlahHalaman() {
		return jumlahHalaman;
	}
}