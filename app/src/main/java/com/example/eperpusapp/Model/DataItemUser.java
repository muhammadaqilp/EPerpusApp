package com.example.eperpusapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class DataItemUser implements Parcelable {
    public DataItemUser() {
    }

    @SerializedName("fakultas")
    private String fakultas;

    @SerializedName("iduser")
    private int iduser;

    @SerializedName("password")
    private String password;

    @SerializedName("angkatan")
    private int angkatan;

    @SerializedName("foto_user")
    private String fotoUser;

    @SerializedName("nama_lengkap")
    private String namaLengkap;

    @SerializedName("jurusan")
    private String jurusan;

    @SerializedName("status")
    private String status;

    @SerializedName("username")
    private String username;

    protected DataItemUser(Parcel in) {
        fakultas = in.readString();
        iduser = in.readInt();
        password = in.readString();
        angkatan = in.readInt();
        fotoUser = in.readString();
        namaLengkap = in.readString();
        jurusan = in.readString();
        status = in.readString();
        username = in.readString();
    }

    public static final Creator<DataItemUser> CREATOR = new Creator<DataItemUser>() {
        @Override
        public DataItemUser createFromParcel(Parcel in) {
            return new DataItemUser(in);
        }

        @Override
        public DataItemUser[] newArray(int size) {
            return new DataItemUser[size];
        }
    };

    public String getFakultas() {
        return fakultas;
    }

    public int getIduser() {
        return iduser;
    }

    public String getPassword() {
        return password;
    }

    public String getFotoUser() {
        return fotoUser;
    }

    public int getAngkatan() {
        return angkatan;
    }

    public String getNamaLengkap() {
        return namaLengkap;
    }

    public String getJurusan() {
        return jurusan;
    }

    public String getStatus() {
        return status;
    }

    public String getUsername() {
        return username;
    }

    public void setFakultas(String fakultas) {
        this.fakultas = fakultas;
    }

    public void setIduser(int iduser) {
        this.iduser = iduser;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAngkatan(int angkatan) {
        this.angkatan = angkatan;
    }

    public void setNamaLengkap(String namaLengkap) {
        this.namaLengkap = namaLengkap;
    }

    public void setJurusan(String jurusan) {
        this.jurusan = jurusan;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFotoUser(String fotoUser) {
        this.fotoUser = fotoUser;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fakultas);
        dest.writeInt(iduser);
        dest.writeString(password);
        dest.writeInt(angkatan);
        dest.writeString(namaLengkap);
        dest.writeString(jurusan);
        dest.writeString(status);
        dest.writeString(username);
        dest.writeString(fotoUser);
    }
}