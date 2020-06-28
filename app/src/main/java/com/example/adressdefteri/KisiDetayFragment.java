package com.example.adressdefteri;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import de.hdodenhof.circleimageview.CircleImageView;

public class KisiDetayFragment extends Fragment {
    private KisiDetayFragmentDinleyicisi dinleyici;
    private long id=-1;
    private TextView adSoyad,mail,telefon,adres;
    private CircleImageView profil;
    private Bitmap bitmap;

    public interface KisiDetayFragmentDinleyicisi{

        public void kisiSilindi();/*kisi slindiginde cagrılır*/
        public void kisiDuzenle(Bundle arguments);/*kisi duzenleme yapar*/


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(savedInstanceState!=null)
        {
            id=savedInstanceState.getLong(MainActivity.ID);
        }
        else
        {
            if(getArguments()!=null)
            {
                id=getArguments().getLong(MainActivity.ID);
            }
        }


        return inflater.inflate(R.layout.detay_fragment,container,false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        setHasOptionsMenu(true);
        adSoyad=getActivity().findViewById(R.id.ad);
        mail=getActivity().findViewById(R.id.mail);
        telefon=getActivity().findViewById(R.id.telefon);
        adres=getActivity().findViewById(R.id.adres);
        profil=getActivity().findViewById(R.id.profil_detay);
    }

    @Override
    public void onResume() {
            super.onResume();
            kisiDetayiniGoster(id);
    }

    private void kisiDetayiniGoster(long id) {
        KisiModel model=new KisiModel();
        Veritabani veritabani=new Veritabani(getActivity());
        model=veritabani.getKisi(id);
        adSoyad.setText(model.getAd());
        mail.setText(model.getMail());
        telefon.setText(model.getTelefon());
        adres.setText(model.getAdres());
        if(model.getProfilFoto()!=null)
        {
            byte []bytes=model.getProfilFoto();
            bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
            profil.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(MainActivity.ID,id);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        dinleyici= (KisiDetayFragmentDinleyicisi) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        dinleyici=null;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.detay_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.sil:
            new AlertDialog.Builder(getActivity())
                    .setTitle("Sil")
                    .setIcon(android.R.drawable.ic_delete)
                    .setMessage("Kisiyi silmek istediginden emin misinin?")
                    .setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Veritabani veritabani=new Veritabani(getActivity());
                            veritabani.sil(id);
                            dinleyici.kisiSilindi();
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("Hayır",null).show();
            return true;
            case R.id.duzenle:
                Bundle arguments=new Bundle();
                arguments.putLong(MainActivity.ID,id);
                arguments.putString("ad", adSoyad.getText().toString());
                arguments.putString("adres", adres.getText().toString());
                arguments.putString("telefon", telefon.getText().toString());
                arguments.putString("mail", mail.getText().toString());

                if(bitmap!=null)
                    arguments.putParcelable("Resim",bitmap);




                dinleyici.kisiDuzenle(arguments);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
