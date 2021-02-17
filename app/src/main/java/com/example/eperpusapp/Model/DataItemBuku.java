package com.example.eperpusapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class DataItemBuku implements Parcelable {

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

	protected DataItemBuku(Parcel in) {
		jumlahCopy = in.readInt();
		fileBuku = in.readString();
		idbuku = in.readInt();
		isbn = in.readString();
		jumlahHalaman = in.readInt();
		kategori = in.readString();
		fotoBuku = in.readString();
		penerbit = in.readString();
		totalDipinjam = in.readInt();
		tahunTerbit = in.readInt();
		judulBuku = in.readString();
		pengarang = in.readString();
		sinopsis = in.readString();
	}

	public static final Creator<DataItemBuku> CREATOR = new Creator<DataItemBuku>() {
		@Override
		public DataItemBuku createFromParcel(Parcel in) {
			return new DataItemBuku(in);
		}

		@Override
		public DataItemBuku[] newArray(int size) {
			return new DataItemBuku[size];
		}
	};

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

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(jumlahCopy);
		dest.writeString(fileBuku);
		dest.writeInt(idbuku);
		dest.writeString(isbn);
		dest.writeInt(jumlahHalaman);
		dest.writeString(kategori);
		dest.writeString(fotoBuku);
		dest.writeString(penerbit);
		dest.writeInt(totalDipinjam);
		dest.writeInt(tahunTerbit);
		dest.writeString(judulBuku);
		dest.writeString(pengarang);
		dest.writeString(sinopsis);
	}
}