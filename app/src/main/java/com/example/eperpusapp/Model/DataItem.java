package com.example.eperpusapp.Model;

import com.google.gson.annotations.SerializedName;

public class DataItem{

	@SerializedName("jumlah_copy")
	private int jumlahCopy;

	@SerializedName("file_buku")
	private String fileBuku;

	@SerializedName("idbuku")
	private int idbuku;

	@SerializedName("isbn")
	private String isbn;

	@SerializedName("jumlah_halaman")
	private int jumlahHalaman;

	@SerializedName("kategori")
	private String kategori;

	@SerializedName("foto_buku")
	private String fotoBuku;

	@SerializedName("penerbit")
	private String penerbit;

	@SerializedName("total_dipinjam")
	private int totalDipinjam;

	@SerializedName("tahun_terbit")
	private int tahunTerbit;

	@SerializedName("judul_buku")
	private String judulBuku;

	@SerializedName("pengarang")
	private String pengarang;

	@SerializedName("sinopsis")
	private String sinopsis;

	public int getJumlahCopy(){
		return jumlahCopy;
	}

	public String getFileBuku(){
		return fileBuku;
	}

	public int getIdbuku(){
		return idbuku;
	}

	public String getIsbn(){
		return isbn;
	}

	public int getJumlahHalaman(){
		return jumlahHalaman;
	}

	public String getKategori(){
		return kategori;
	}

	public String getFotoBuku(){
		return fotoBuku;
	}

	public String getPenerbit(){
		return penerbit;
	}

	public int getTotalDipinjam(){
		return totalDipinjam;
	}

	public int getTahunTerbit(){
		return tahunTerbit;
	}

	public String getJudulBuku(){
		return judulBuku;
	}

	public String getPengarang(){
		return pengarang;
	}

	public String getSinopsis(){
		return sinopsis;
	}
}