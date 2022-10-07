package com.adnan.healthAppMaster;

import android.os.Parcel;
import android.os.Parcelable;

public class Profil implements Parcelable {
    private String imeprezime, godinaRodjenja, spol, tezina, krvnaGrupa, slika;

    public Profil(String imeprezime, String godinaRodjenja, String spol, String tezina, String krvnaGrupa, String slika) {
        this.imeprezime = imeprezime;
        this.godinaRodjenja = godinaRodjenja;
        this.spol = spol;
        this.tezina = tezina;
        this.krvnaGrupa = krvnaGrupa;
        this.slika = slika;
    }

    public Profil() {
    }

    protected Profil(Parcel in) {
        imeprezime = in.readString();
        godinaRodjenja = in.readString();
        spol = in.readString();
        tezina = in.readString();
        krvnaGrupa = in.readString();
        slika = in.readString();
    }

    public static final Creator<Profil> CREATOR = new Creator<Profil>() {
        @Override
        public Profil createFromParcel(Parcel in) {
            return new Profil(in);
        }

        @Override
        public Profil[] newArray(int size) {
            return new Profil[size];
        }
    };

    public String getImeprezime() {
        return imeprezime;
    }

    public void setImeprezime(String imeprezime) {
        this.imeprezime = imeprezime;
    }

    public String getGodinaRodjenja() {
        return godinaRodjenja;
    }

    public void setGodinaRodjenja(String godinaRodjenja) {
        this.godinaRodjenja = godinaRodjenja;
    }

    public String getSpol() {
        return spol;
    }

    public void setSpol(String spol) {
        this.spol = spol;
    }

    public String getTezina() {
        return tezina;
    }

    public void setTezina(String tezina) {
        this.tezina = tezina;
    }

    public String getKrvnaGrupa() {
        return krvnaGrupa;
    }

    public void setKrvnaGrupa(String krvnaGrupa) {
        this.krvnaGrupa = krvnaGrupa;
    }

    public String getSlika() {
        return slika;
    }

    public void setSlika(String slika) {
        this.slika = slika;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(imeprezime);
        parcel.writeString(godinaRodjenja);
        parcel.writeString(spol);
        parcel.writeString(tezina);
        parcel.writeString(krvnaGrupa);
        parcel.writeString(slika);
    }
}
