
package fi.niroshan.dnalauncher;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private final int BG = Color.rgb(13, 16, 28);
    private final int WHITE = Color.WHITE;
    private final int MUTED = Color.rgb(173, 181, 202);
    private LinearLayout root;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(BG);
        getWindow().setNavigationBarColor(BG);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        buildScreen();
    }

    private void buildScreen() {
        root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(42), dp(24), dp(20), dp(18));
        root.setBackgroundColor(BG);

        ScrollView page = new ScrollView(this);
        page.setFillViewport(true);
        page.addView(root);
        setContentView(page);

        LinearLayout header = new LinearLayout(this);
        header.setGravity(Gravity.CENTER_VERTICAL);
        header.setOrientation(LinearLayout.HORIZONTAL);

        TextView logo = text("DNA", 27, WHITE, true);
        header.addView(logo);

        TextView subtitle = text("  CINEMA", 12, MUTED, true);
        header.addView(subtitle);

        View gap = new View(this);
        header.addView(gap, new LinearLayout.LayoutParams(0, 1, 1));

        TextView date = text("YOUR ENTERTAINMENT", 11, MUTED, true);
        header.addView(date);
        root.addView(header);

        TextView greeting = text("Everything you love.", 34, WHITE, true);
        LinearLayout.LayoutParams gp = new LinearLayout.LayoutParams(
                -1, -2);
        gp.topMargin = dp(28);
        root.addView(greeting, gp);

        TextView description = text(
                "Movies, series and apps — all in one place.",
                15, MUTED, false);
        LinearLayout.LayoutParams descp = new LinearLayout.LayoutParams(
                -1, -2);
        descp.topMargin = dp(5);
        root.addView(description, descp);

        TextView featured = text("FEATURED FOR YOU", 12, MUTED, true);
        LinearLayout.LayoutParams fp = new LinearLayout.LayoutParams(-1, -2);
        fp.topMargin = dp(30);
        root.addView(featured, fp);

        LinearLayout hero = new LinearLayout(this);
        hero.setOrientation(LinearLayout.VERTICAL);
        hero.setGravity(Gravity.BOTTOM);
        hero.setPadding(dp(24), dp(20), dp(24), dp(20));
        hero.setBackground(gradient(
                new int[]{Color.rgb(78, 62, 154),
                          Color.rgb(34, 48, 91),
                          Color.rgb(18, 25, 44)}, 24));

        TextView badge = text("DNA HUB  •  YOUR HOME SCREEN", 11,
                Color.rgb(215, 205, 255), true);
        hero.addView(badge);

        TextView heroTitle = text("Your next\nfavourite.", 31, WHITE, true);
        LinearLayout.LayoutParams htp =
                new LinearLayout.LayoutParams(-1, -2);
        htp.topMargin = dp(10);
        hero.addView(heroTitle, htp);

        TextView heroText = text(
                "Pick an app below and settle in.",
                14, Color.rgb(224, 228, 245), false);
        LinearLayout.LayoutParams hxp =
                new LinearLayout.LayoutParams(-1, -2);
        hxp.topMargin = dp(7);
        hero.addView(heroText, hxp);

        LinearLayout.LayoutParams hp =
                new LinearLayout.LayoutParams(-1, dp(205));
        hp.topMargin = dp(12);
        root.addView(hero, hp);

        TextView appsTitle = text("YOUR APPS", 12, MUTED, true);
        LinearLayout.LayoutParams atp =
                new LinearLayout.LayoutParams(-1, -2);
        atp.topMargin = dp(26);
        root.addView(appsTitle, atp);

        HorizontalScrollView appsScroll = new HorizontalScrollView(this);
        appsScroll.setHorizontalScrollBarEnabled(false);

        LinearLayout apps = new LinearLayout(this);
        apps.setOrientation(LinearLayout.HORIZONTAL);
        apps.setPadding(dp(3), dp(12), dp(8), dp(12));

        String[] names = {
                "YouTube", "Netflix", "Spotify", "Prime Video",
                "Settings", "Play Store"
        };
        String[] packages = {
                "com.google.android.youtube.tv",
                "com.netflix.ninja",
                "com.spotify.tv.android",
                "com.amazon.amazonvideo.livingroom",
                "com.android.tv.settings",
                "com.android.vending"
        };
        int[] colors = {
                Color.rgb(190, 40, 48),
                Color.rgb(130, 24, 38),
                Color.rgb(28, 122, 79),
                Color.rgb(33, 91, 154),
                Color.rgb(63, 73, 96),
                Color.rgb(42, 111, 160)
        };

        for (int i = 0; i < names.length; i++) {
            final String pkg = packages[i];
            LinearLayout card = new LinearLayout(this);
            card.setOrientation(LinearLayout.VERTICAL);
            card.setGravity(Gravity.CENTER);
            card.setFocusable(true);
            card.setFocusableInTouchMode(true);
            card.setPadding(dp(8), dp(12), dp(8), dp(12));
            card.setBackground(gradient(
                    new int[]{colors[i], darken(colors[i])}, 16));

            TextView initial = text(
                    names[i].substring(0, 1), 29, WHITE, true);
            initial.setGravity(Gravity.CENTER);
            card.addView(initial);

            TextView name = text(names[i], 12, WHITE, true);
            name.setGravity(Gravity.CENTER);
            LinearLayout.LayoutParams np =
                    new LinearLayout.LayoutParams(-1, -2);
            np.topMargin = dp(8);
            card.addView(name, np);

            card.setOnClickListener(v -> launchApp(pkg));

            card.setOnFocusChangeListener((v, hasFocus) -> {
                v.animate().scaleX(hasFocus ? 1.07f : 1f)
                        .scaleY(hasFocus ? 1.07f : 1f)
                        .setDuration(140).start();
                v.setAlpha(hasFocus ? 1f : 0.88f);
            });

            LinearLayout.LayoutParams cp =
                    new LinearLayout.LayoutParams(dp(145), dp(110));
            cp.rightMargin = dp(12);
            apps.addView(card, cp);
        }

        appsScroll.addView(apps);
        root.addView(appsScroll);

        TextView note = text(
                "Select an app to open it. Some apps may not be installed.",
                12, MUTED, false);
        LinearLayout.LayoutParams ntp =
                new LinearLayout.LayoutParams(-1, -2);
        ntp.topMargin = dp(4);
        root.addView(note, ntp);
    }

    private void launchApp(String packageName) {
        try {
            Intent intent = getPackageManager()
                    .getLeanbackLaunchIntentForPackage(packageName);
            if (intent == null) {
                intent = getPackageManager()
                        .getLaunchIntentForPackage(packageName);
            }
            if (intent != null) {
                startActivity(intent);
            } else {
                android.widget.Toast.makeText(
                        this, "App is not installed",
                        android.widget.Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            android.widget.Toast.makeText(
                    this, "Unable to open this app",
                    android.widget.Toast.LENGTH_SHORT).show();
        }
    }

    private TextView text(String value, float size, int color,
                          boolean bold) {
        TextView t = new TextView(this);
        t.setText(value);
        t.setTextSize(size);
        t.setTextColor(color);
        t.setFontFeatureSettings("kern");
        t.setTypeface(Typeface.create(
                "sans-serif", bold ? Typeface.BOLD : Typeface.NORMAL));
        return t;
    }

    private GradientDrawable gradient(int[] colors, int radius) {
        GradientDrawable d = new GradientDrawable(
                GradientDrawable.Orientation.TL_BR, colors);
        d.setCornerRadius(dp(radius));
        return d;
    }

    private int darken(int color) {
        return Color.rgb(
                (int)(Color.red(color) * 0.65),
                (int)(Color.green(color) * 0.65),
                (int)(Color.blue(color) * 0.65));
    }

    private int dp(int value) {
        return (int)(value * getResources().getDisplayMetrics().density
                + 0.5f);
    }
}
