package com.example.adressdefteri;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;

public class KisiEkleDuzenleFragment extends Fragment {
    private EditText ad, telefon, adres, mail;
    private Button kaydet;
    private TextInputLayout adSoyadInputLayout;
    private ImageView ımageView;
    private Bundle arguments;
    private long id;
    private Bitmap bitmap;
    final int CAMERA_CAPTURE=1;
    final int PIC_CROP=2;
    private Uri picUri;
    private KisiEkleDuzenleFragmentDinleyicisi dinleyici;

    public interface KisiEkleDuzenleFragmentDinleyicisi {
        public void kisiEkleDuzenleIslemiYapıldı(long id);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        dinleyici = (KisiEkleDuzenleFragmentDinleyicisi) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        dinleyici = null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ekle_duzenle_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ad = getActivity().findViewById(R.id.adSoyadEt);
        mail = getActivity().findViewById(R.id.mailEt);
        adres = getActivity().findViewById(R.id.adresEt);
        telefon = getActivity().findViewById(R.id.telefonEt);
        kaydet = getActivity().findViewById(R.id.kaydet);
        adSoyadInputLayout = getActivity().findViewById(R.id.adSoyadTextInputLayout);
        ımageView = getActivity().findViewById(R.id.profile_image);
        arguments = getArguments();
        if (arguments != null) {/*kisi duzenlenecekse*/
            id = arguments.getLong(MainActivity.ID);
            ad.setText(arguments.getString("ad"));
            mail.setText(arguments.getString("mail"));
            telefon.setText(arguments.getString("telefon"));
            adres.setText(arguments.getString("adres"));
            if (arguments.getParcelable("resim") != null) {
                bitmap = arguments.getParcelable("resim");
                ımageView.setImageBitmap(bitmap);
            }
        }
        ımageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,CAMERA_CAPTURE);

            }
        });



        kaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ad.getText().toString().trim().length() != 0) {
                    kaydetVeyaGuncelle();
                    dinleyici.kisiEkleDuzenleIslemiYapıldı(id);

                } else {
                    adSoyadInputLayout.setError("Lütfen bir ad giriniz...");
                    ad.requestFocus();
                }
            }
        });

    }

    private void kaydetVeyaGuncelle() {
        KisiModel model=new KisiModel(ad.getText().toString(),mail.getText().toString(),telefon.getText().toString(),adres.getText().toString());
        Veritabani veritabani=new Veritabani(getActivity());
        if (bitmap!=null)
        {

            byte [] byteArray=getByteArrray(bitmap);
            model.setProfilFoto(byteArray);
        }
        if (arguments == null) { /*yeni kayıt */
           id= veritabani.kaydet(model);

        }
        else
        {
            veritabani.guncelle(id,model);

        }
    }

    private byte[] getByteArrray(Bitmap bitmap) {
        ByteArrayOutputStream bos=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,0,bos);
        return bos.toByteArray();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
       if(resultCode==getActivity().RESULT_OK)
       {
           if(requestCode==CAMERA_CAPTURE)
           {
               picUri=data.getData();
               kirpmaIstegi();
           }
           else if(requestCode==PIC_CROP){
               Bundle bundle =data.getExtras();
               bitmap =bundle.getParcelable("data");
               ımageView.setImageBitmap(bitmap);
           }

       }
    }

    private void kirpmaIstegi() {
        try {
            Intent cropIntent=new Intent("com.android.camera.action.CROP");
            cropIntent.setDataAndType(picUri,"image/*");
            cropIntent.putExtra("crop","true");
            cropIntent.putExtra("aspectX",1);
            cropIntent.putExtra("aspectY",1);
            cropIntent.putExtra("outputX",256);
            cropIntent.putExtra("outputY",256);
            cropIntent.putExtra("return-data",true);
           startActivityForResult(cropIntent,PIC_CROP);




        }
        catch (ActivityNotFoundException anfe)
        {
              String errorMessage="Telefonunun fis.";
            Toast toast=Toast.makeText(getActivity(),errorMessage,Toast.LENGTH_SHORT);
            toast.show();
        }






    }
}

