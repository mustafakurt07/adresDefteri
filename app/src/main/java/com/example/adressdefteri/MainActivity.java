package com.example.adressdefteri;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

public class MainActivity extends AppCompatActivity implements KisiListesiFragment.KisiListesiFragmentDinleyicisi , KisiDetayFragment.KisiDetayFragmentDinleyicisi, KisiEkleDuzenleFragment.KisiEkleDuzenleFragmentDinleyicisi {
 private KisiListesiFragment kisiListesiFragment;
public static final String ID="_id";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        String s=sharedPreferences.getString("tema","0");
        int tema=Integer.parseInt(s);
        switch (tema)
        {
            case 0: setTheme(R.style.AppTheme);break;
            case 1: setTheme(R.style.AppThemeMavi);break;
            case 2: setTheme(R.style.AppThemeGri);break;
            case 3: setTheme(R.style.AppThemePink);break;
            case 4: setTheme(R.style.AppThemeKahverengi);break;
            case 5: setTheme(R.style.AppThemeYesil);break;
            case 6: setTheme(R.style.AppThemeMavimsiGri);break;


        }
        setContentView(R.layout.activity_main);
        /*yapılandırma degisikliginde tekrar fragment olusturmamak için*/
        if(savedInstanceState!=null)
            return;

        if(findViewById(R.id.container)!=null)
        {/*telefonda calısıyor demektir*/

            kisiListesiFragment=new KisiListesiFragment();
            FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.container,kisiListesiFragment);
            transaction.commit();

        }


    }

    @Override
    public void elemanSecildi(long id) {
        if(findViewById(R.id.container)!=null)
        {   /*telefon*/

            detayFragmentiniAc(id,R.id.container);

        }else
        {/*tablet*/
            getSupportFragmentManager().popBackStack();
            detayFragmentiniAc(id,R.id.sagPanelContainer);
        }

    }

    private void detayFragmentiniAc(long id, int containerId) {
        Bundle arguments=new Bundle();
        arguments.putLong(ID,id);
        KisiDetayFragment kisiDetayFragment=new KisiDetayFragment();
        kisiDetayFragment.setArguments(arguments);
        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
        transaction.replace(containerId,kisiDetayFragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.addToBackStack(null);
        transaction.commit();



    }

    @Override
    public void kisiEklermisinKardes() {

        if(findViewById(R.id.container)!=null)
        {
            KisiEkleDuzenleFragmentiniAc(R.id.container,null);
        }
        else {
            KisiEkleDuzenleFragmentiniAc(R.id.sagPanelContainer,null);

        }

    }



    @Override
    public void kisiDuzenle(Bundle arguments) {



        if(findViewById(R.id.container)!=null)
        {
            KisiEkleDuzenleFragmentiniAc(R.id.container,arguments);
        }
        else {
            KisiEkleDuzenleFragmentiniAc(R.id.sagPanelContainer,arguments);

        }

    }
    private void KisiEkleDuzenleFragmentiniAc( int view_id,Bundle arguments)
    {
        KisiEkleDuzenleFragment kisiEkleDuzenle=new KisiEkleDuzenleFragment();
        if(arguments!=null)
            kisiEkleDuzenle.setArguments(arguments);
        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
        transaction.replace(view_id,kisiEkleDuzenle);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.addToBackStack(null);
        transaction.commit();


    }
    /*
    @Override
   public void onPointerCaptureChanged(boolean hasCapture) {

    }
*/
    @Override
    public void kisiSilindi() {
        getSupportFragmentManager().popBackStack();
        if(findViewById(R.id.container)==null)
        {
            kisiListesiFragment.guncelle();


        }

    }

    @Override
    public void kisiEkleDuzenleIslemiYapıldı(long id) {
        getSupportFragmentManager().popBackStack();/*ekle veya duzenle fragmentini sil*/
        if(findViewById(R.id.container)==null)
        {
            getSupportFragmentManager().popBackStack();/*varsa detay fragmenti onuda sil*/
            detayFragmentiniAc(id,R.id.sagPanelContainer);
            kisiListesiFragment.guncelle();
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        if(kisiListesiFragment==null)
        {
            /*tabletse*/
            kisiListesiFragment=
                    (KisiListesiFragment) getSupportFragmentManager().findFragmentById(R.id.kisiListesiFragment);
        }
    }
}
