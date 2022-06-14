package statussaver.storysaver.statusdownloader;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.interstitial.InterstitialAd;

import java.util.ArrayList;
import java.util.List;

import statussaver.storysaver.statusdownloader.activities.ChatDirect;
import statussaver.storysaver.statusdownloader.activities.WhatsAppWeb;
import statussaver.storysaver.statusdownloader.modules.AdController;
import statussaver.storysaver.statusdownloader.modules.Utils;
import statussaver.storysaver.statusdownloader.activities.MyStatusActivity;
import statussaver.storysaver.statusdownloader.activities.StatusActivity;

import static statussaver.storysaver.statusdownloader.modules.Utils.createFileFolder;

public class MainActivity extends AppCompatActivity {
    MainActivity activity;
    String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    private FrameLayout adContainerView;
    private AdView adView;
    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedpreferences;
    private InterstitialAd mInterstitialAd;
    AdRequest adRequest;
    int AdCounter = 0;
    public static MainActivity ma;
    Dialog dialog, dialogLang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utils.loadLocale(this);
//        Utils.setLanguage(MainActivity.this, SharedPrefs.getLang(MainActivity.this));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = this;
        ma = this;
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        if (Build.VERSION.SDK_INT >= 23) {
            checkPermissions(0);
        }
        createFileFolder();

        findViewById(R.id.gallery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Utils.hasPermissions(MainActivity.this, Utils.permissions)) {
                    ActivityCompat.requestPermissions(MainActivity.this, Utils.permissions, Utils.perRequest);
                } else {
                    navigate(new Intent(MainActivity.this, MyStatusActivity.class));
                }
//                Intent i = new Intent(MainActivity.this, GalleryActivity.class);
//                startActivity(i);
//                Intent i = new Intent(MainActivity.this, MyStatusActivity.class);
//                startActivity(i);
//                ShowInt();
            }
        });
        findViewById(R.id.rvWhatsApp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Utils.hasPermissions(MainActivity.this, Utils.permissions)) {
                    ActivityCompat.requestPermissions(MainActivity.this, Utils.permissions, Utils.perRequest);
                } else {
                    Intent i = new Intent(MainActivity.this, StatusActivity.class);
                    i.putExtra("type", "WA");
                    startActivity(i);

                    AdController.adCounter++;
                    AdController.showInterAd(MainActivity.this, i, 0);
                }
            }
        });

        langAlert();

        findViewById(R.id.changeLng).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogLang.show();
            }
        });

        findViewById(R.id.quotes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, WhatsAppWeb.class);
                startActivity(i);
            }
        });

        findViewById(R.id.rvWhatsAppB).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Utils.hasPermissions(MainActivity.this, Utils.permissions)) {
                    ActivityCompat.requestPermissions(MainActivity.this, Utils.permissions, Utils.perRequest);
                } else {
                    Intent i = new Intent(MainActivity.this, StatusActivity.class);
                    i.putExtra("type", "WABS");
                    startActivity(i);
                    AdController.adCounter++;
                    AdController.showInterAd(MainActivity.this, i, 0);
                }
            }
        });

        findViewById(R.id.rvWhatsAppG).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Utils.hasPermissions(MainActivity.this, Utils.permissions)) {
                    ActivityCompat.requestPermissions(MainActivity.this, Utils.permissions, Utils.perRequest);
                } else {
                    Intent i = new Intent(MainActivity.this, StatusActivity.class);
                    i.putExtra("type", "WAGB");
                    startActivity(i);
                    AdController.adCounter++;
                    AdController.showInterAd(MainActivity.this, i, 0);
                }
            }
        });
        findViewById(R.id.chatDirect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ChatDirect.class);
                startActivity(i);
                AdController.adCounter++;
                AdController.showInterAd(MainActivity.this, i, 0);
            }
        });
        wAppAlert();
        findViewById(R.id.openWA).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });

        /*admob*/
        LinearLayout container = findViewById(R.id.banner_container);
        AdController.loadBannerAd(MainActivity.this, container);
        AdController.loadInterAd(MainActivity.this);
    }

    boolean isOpenWapp = false, isOpenWbApp = false, isOpenWgApp = false;

    void wAppAlert() {
        dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.popup_lay);

        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));

        RelativeLayout btnWapp = dialog.findViewById(R.id.btn_wapp);
        RelativeLayout btnWappBus = dialog.findViewById(R.id.btn_wapp_bus);
        RelativeLayout btnWappGb = dialog.findViewById(R.id.btn_wapp_gb);

        btnWapp.setOnClickListener(arg0 -> {
            try {
                isOpenWapp = true;
                startActivity(getPackageManager().getLaunchIntentForPackage("com.whatsapp"));
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "Please Install WhatsApp to Download Statuses!", Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();

        });

        btnWappBus.setOnClickListener(arg0 -> {
            try {
                isOpenWbApp = true;
                startActivity(getPackageManager().getLaunchIntentForPackage("com.whatsapp.w4b"));
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "Please Install WhatsApp Business to Download Statuses!", Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
        });

        btnWappGb.setOnClickListener(arg0 -> {
            try {
                isOpenWgApp = true;
                startActivity(getPackageManager().getLaunchIntentForPackage("com.gbwhatsapp"));
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "Please Install WhatsApp GB to Download Statuses!", Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
        });

    }

    void navigate(Intent intent) {
        AdController.adCounter++;
        Log.e("adCounter: ", "" + AdController.adCounter);
        AdController.showInterAd(MainActivity.this, intent, 0);
    }

    public Intent openTelegram() {
        Intent intent;
        try {
            try {
                getPackageManager().getPackageInfo("org.telegram.messenger", 0);
            } catch (PackageManager.NameNotFoundException e) {
                getPackageManager().getPackageInfo("org.thunderdog.challegram", 0);
            }
            // set app Uri
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tg://resolve?domain=gblatest"));
        } catch (PackageManager.NameNotFoundException e) {
            // set browser URI
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.telegram.me/gblatest"));
        }
        return intent;

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     * Called when returning to the activity
     */
    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * Called before the activity is destroyed
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private boolean checkPermissions(int type) {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(activity, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions((Activity) (activity),
                    listPermissionsNeeded.toArray(new
                            String[listPermissionsNeeded.size()]), type);
            return false;
        }
        return true;
    }

    public void langAlert() {
        dialogLang = new Dialog(MainActivity.this);
        dialogLang.setContentView(R.layout.lang_lay);

        dialogLang.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView txtEn = dialogLang.findViewById(R.id.txt_en);
        TextView txtHi = dialogLang.findViewById(R.id.txt_hi);
        TextView txtAr = dialogLang.findViewById(R.id.txt_ar);

        txtEn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.saveLocale(MainActivity.this, "en");
//                SharedPrefs.setLang(MainActivity.this, "en");
                dialogLang.dismiss();
                refresh();
            }
        });

        txtHi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.saveLocale(MainActivity.this, "hi");
//                SharedPrefs.setLang(MainActivity.this, "hi");
                dialogLang.dismiss();
                refresh();
            }
        });

        txtAr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.saveLocale(MainActivity.this, "ar");
//                SharedPrefs.setLang(MainActivity.this, "ar");
                dialogLang.dismiss();
                refresh();
                Log.e("language", "ar");
            }
        });

    }

    public void refresh() {
        finish();
        startActivity(getIntent());
    }
}