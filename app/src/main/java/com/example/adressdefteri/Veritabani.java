package com.example.adressdefteri;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Veritabani extends SQLiteOpenHelper {
    private static final String VERITABANI_ISMI="veritabani";
    private static final String TABLO_ISMI="tablo";
    private static final int  VERSIYON=2;
    private  static final String ID="id";
    private  static final String AD="ad";
    private  static final String TELEFON="telefon";
    private  static final String MAIL="mail";
    private  static final String ADRES="adres";
    private  static final String PROFIL="profil";



    public Veritabani(@Nullable Context context) {
        super(context, VERITABANI_ISMI, null,VERSIYON);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + TABLO_ISMI +
                " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                AD + " TEXT NOT NULL, " +
                TELEFON + " TEXT, " +
                MAIL + " TEXT, " +
                ADRES + " TEXT, " +
                PROFIL + " BLOB);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLO_ISMI);
        onCreate(db);

    }

    public long kaydet(KisiModel model) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(AD, model.getAd());
        cv.put(TELEFON, model.getTelefon());
        cv.put(MAIL, model.getMail());
        cv.put(ADRES, model.getAdres());

        if (model.getProfilFoto() != null) {
            cv.put(PROFIL, model.getProfilFoto());
        }

        long id = db.insert(TABLO_ISMI, null, cv);
        db.close();

        return id;

    }

    public void guncelle(long id, KisiModel model) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(AD, model.getAd());
        cv.put(TELEFON, model.getTelefon());
        cv.put(MAIL, model.getMail());
        cv.put(ADRES, model.getAdres());

        if (model.getProfilFoto() != null) {
            cv.put(PROFIL, model.getProfilFoto());
        }

        db.update(TABLO_ISMI, cv, ID + "=" + id, null);

        db.close();

    }

    public List<KisiModel> getTumKisiler() {
        List<KisiModel>modelList=new ArrayList<KisiModel>();
        SQLiteDatabase db=getReadableDatabase();
        String [] sutunlar =new String[]{AD,ADRES,TELEFON,MAIL,ID,PROFIL};
        Cursor c=db.query(TABLO_ISMI,sutunlar,null,null,null,null,null);
        int adSirano = c.getColumnIndex(AD);
        int adresSirano = c.getColumnIndex(ADRES);
        int telefonSirano = c.getColumnIndex(TELEFON);
        int mailSirano = c.getColumnIndex(MAIL);
        int idSirano = c.getColumnIndex(ID);
        int profilSirano = c.getColumnIndex(PROFIL);
        if (c.moveToFirst()){

            do {
                KisiModel model =new KisiModel();

                model.setAd(c.getString(adSirano));
                model.setAdres(c.getString(adresSirano));
                model.setTelefon(c.getString(telefonSirano));
                model.setMail(c.getString(mailSirano));
                model.setId(c.getLong(idSirano));

                if (c.getBlob(profilSirano)!=null)
                    model.setProfilFoto(c.getBlob(profilSirano));

                modelList.add(model);
            }while (c.moveToNext());


        }else{
            modelList=null;
        }
        db.close();

        return modelList;
    }

    public KisiModel getKisi(long id) {
        KisiModel model=new KisiModel();
        SQLiteDatabase db=getReadableDatabase();
        String [] sutunlar =new String[]{AD,ADRES,TELEFON,MAIL,ID,PROFIL};
        Cursor c = db.query(TABLO_ISMI,sutunlar,ID+" = ? ",new String[]{String.valueOf(id)},null,null,null);
        /*c.moveToNext();*/
        c.moveToFirst(); /* Buraya dikkat*/

        int adSirano = c.getColumnIndex(AD);
        int adresSirano = c.getColumnIndex(ADRES);
        int telefonSirano = c.getColumnIndex(TELEFON);
        int mailSirano = c.getColumnIndex(MAIL);
        int idSirano = c.getColumnIndex(ID);
        int profilSirano = c.getColumnIndex(PROFIL);


        model.setAd(c.getString(adSirano));
        model.setAdres(c.getString(adresSirano));
        model.setTelefon(c.getString(telefonSirano));
        model.setMail(c.getString(mailSirano));
        model.setId(c.getLong(idSirano));

        if (c.getBlob(profilSirano)!=null)
            model.setProfilFoto(c.getBlob(profilSirano));

        db.close();

        return model;
    }

    public void sil(long id) {
        SQLiteDatabase db=getWritableDatabase();
        db.delete(TABLO_ISMI,ID+"=?",new String[]{String.valueOf(id)});
        db.close();
    }
}
