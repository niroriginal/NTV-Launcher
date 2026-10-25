package fi.niroshan.dnalauncher;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.WallpaperManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends Activity {
    private final int BG = Color.rgb(10, 15, 30);
    private final int WHITE = Color.WHITE;
    private final Handler handler = new Handler();
    private TextView clock;
    private LinearLayout appRow;
    private final List<View> focusables = new ArrayList<>();

    private final Runnable tick = new Runnable() {
        @Override public void run() {
            if (clock != null) {
                clock.setText(new SimpleDateFormat("EEEE, d MMMM   •   HH:mm", Locale.getDefault()).format(new Date()));
            }
            handler.postDelayed(this, 30_000);
        }
    };

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        getWindow().getDecorView().setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        buildUi();
        handler.post(tick);
    }

    @Override protected void onDestroy() {
        handler.removeCallbacks(tick);
        super.onDestroy();
    }

    private void buildUi() {
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(42), dp(24), dp(42), dp(28));
        root.setBackground(new GradientDrawable(GradientDrawable.Orientation.TL_BR,
            new int[]{Color.rgb(21, 35, 68), Color.rgb(13, 18, 37), Color.rgb(5, 8, 18)}));
        setContentView(root);

        LinearLayout top = new LinearLayout(this);
        top.setGravity(Gravity.CENTER_VERTICAL);
        top.setOrientation(LinearLayout.HORIZONTAL);
        root.addView(top, new LinearLayout.LayoutParams(-1, dp(52)));

        TextView brand = text("DNA  |  CINEMATIC", 20, true);
        brand.setTextColor(Color.rgb(126, 190, 255));
        top.addView(brand, new LinearLayout.LayoutParams(0, -2, 1));
        clock = text("", 17, false);
        clock.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        top.addView(clock);

        TextView heading = text("Your entertainment starts here", 31, true);
        heading.setPadding(0, dp(28), 0, dp(5));
        root.addView(heading);
        TextView sub = text("Pick up where you left off, or open an app", 16, false);
        sub.setTextColor(Color.rgb(185, 197, 220));
        root.addView(sub);

        TextView section = text("YOUR APPS", 15, true);
        section.setTextColor(Color.rgb(151, 181, 231));
        section.setPadding(0, dp(26), 0, dp(12));
        root.addView(section);

        HorizontalScrollView horizontal = new HorizontalScrollView(this);
        horizontal.setHorizontalScrollBarEnabled(false);
        appRow = new LinearLayout(this);
        appRow.setOrientation(LinearLayout.HORIZONTAL);
        horizontal.addView(appRow);
        root.addView(horizontal, new LinearLayout.LayoutParams(-1, dp(142)));
        populateApps();

        TextView quick = text("QUICK ACCESS", 15, true);
        quick.setTextColor(Color.rgb(151, 181, 231));
        quick.setPadding(0, dp(20), 0, dp(10));
        root.addView(quick);

        LinearLayout shortcuts = new LinearLayout(this);
        shortcuts.setOrientation(LinearLayout.HORIZONTAL);
        root.addView(shortcuts);
        addShortcut(shortcuts, "⚙", "Settings", v -> openSettings());
        addShortcut(shortcuts, "◈", "VPN", v -> openVpn());
        addShortcut(shortcuts, "↻", "Refresh apps", v -> { appRow.removeAllViews(); populateApps(); });

        TextView footer = text("Use the remote's arrows to navigate  •  OK to open  •  Back to return", 13, false);
        footer.setTextColor(Color.rgb(145, 157, 182));
        footer.setPadding(0, dp(20), 0, 0);
        root.addView(footer);

        if (!focusables.isEmpty()) {
            focusables.get(0).requestFocus();
        }
    }

    private void populateApps() {
        PackageManager pm = getPackageManager();
        List<ApplicationInfo> apps = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        List<ApplicationInfo> launchable = new ArrayList<>();
        for (ApplicationInfo info : apps) {
            Intent launch = pm.getLaunchIntentForPackage(info.packageName);
            if (launch != null && !info.packageName.equals(getPackageName())) launchable.add(info);
        }
        Collections.sort(launchable, (a, b) ->
            pm.getApplicationLabel(a).toString().compareToIgnoreCase(pm.getApplicationLabel(b).toString()));

        int shown = 0;
        for (ApplicationInfo info : launchable) {
            if (shown >= 14) break;
            String label = pm.getApplicationLabel(info).toString();
            android.graphics.drawable.Drawable icon = pm.getApplicationIcon(info);
            LinearLayout card = new LinearLayout(this);
            card.setOrientation(LinearLayout.VERTICAL);
            card.setGravity(Gravity.CENTER);
            card.setFocusable(true);
            card.setFocusableInTouchMode(true);
            card.setPadding(dp(12), dp(10), dp(12), dp(10));
            card.setBackground(cardBackground(false));
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(dp(126), dp(124));
            lp.setMargins(0, 0, dp(14), 0);
            appRow.addView(card, lp);

            android.widget.ImageView image = new android.widget.ImageView(this);
            image.setImageDrawable(icon);
            image.setScaleType(android.widget.ImageView.ScaleType.FIT_CENTER);
            card.addView(image, new LinearLayout.LayoutParams(dp(48), dp(48)));
            TextView name = text(label, 13, true);
            name.setGravity(Gravity.CENTER);
            name.setMaxLines(2);
            name.setTextColor(WHITE);
            LinearLayout.LayoutParams nameLp = new LinearLayout.LayoutParams(-1, dp(42));
            nameLp.topMargin = dp(5);
            card.addView(name, nameLp);
            card.setOnClickListener(v -> launchPackage(info.packageName));
            setFocusStyle(card);
            focusables.add(card);
            shown++;
        }
        if (shown == 0) {
            TextView empty = text("No launchable apps found", 16, false);
            appRow.addView(empty);
        }
    }

    private void addShortcut(LinearLayout parent, String symbol, String label, View.OnClickListener action) {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.HORIZONTAL);
        card.setGravity(Gravity.CENTER_VERTICAL);
        card.setFocusable(true);
        card.setPadding(dp(18), dp(10), dp(22), dp(10));
        card.setBackground(cardBackground(false));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-2, dp(58));
        lp.setMargins(0, 0, dp(14), 0);
        parent.addView(card, lp);
        TextView icon = text(symbol, 23, true);
        icon.setTextColor(Color.rgb(126, 190, 255));
        card.addView(icon);
        TextView title = text(label, 16, true);
        title.setPadding(dp(12), 0, 0, 0);
        card.addView(title);
        card.setOnClickListener(action);
        setFocusStyle(card);
        focusables.add(card);
    }

    private void setFocusStyle(View view) {
        view.setOnFocusChangeListener((v, focused) -> {
            v.setBackground(cardBackground(focused));
            v.setScaleX(focused ? 1.045f : 1f);
            v.setScaleY(focused ? 1.045f : 1f);
        });
    }

    private GradientDrawable cardBackground(boolean focused) {
        GradientDrawable d = new GradientDrawable();
        d.setColor(focused ? Color.rgb(49, 76, 122) : Color.rgb(28, 39, 63));
        d.setCornerRadius(dp(16));
        d.setStroke(dp(focused ? 3 : 1), focused ? Color.rgb(145, 202, 255) : Color.rgb(65, 82, 112));
        return d;
    }

    private TextView text(String value, int size, boolean bold) {
        TextView t = new TextView(this);
        t.setText(value);
        t.setTextSize(size);
        t.setTextColor(WHITE);
        t.setTypeface(Typeface.create("sans-serif", bold ? Typeface.BOLD : Typeface.NORMAL));
        t.setGravity(Gravity.CENTER_VERTICAL);
        return t;
    }

    private void launchPackage(String packageName) {
        Intent i = getPackageManager().getLaunchIntentForPackage(packageName);
        if (i != null) {
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            try { startActivity(i); } catch (ActivityNotFoundException ignored) {}
        }
    }

    private void openSettings() {
        try { startActivity(new Intent(Settings.ACTION_SETTINGS)); }
        catch (Exception e) {
            try { startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS)); }
            catch (Exception ignored) { new AlertDialog.Builder(this).setMessage("Settings are not available on this device.").setPositiveButton("OK", null).show(); }
        }
    }

    private void openVpn() {
        PackageManager pm = getPackageManager();
        String[] likely = {"com.wireguard.android", "com.cloudflare.onedotonedotonedotone", "org.mozilla.firefox"};
        for (String pkg : likely) {
            if (pm.getLaunchIntentForPackage(pkg) != null) { launchPackage(pkg); return; }
        }
        List<ApplicationInfo> apps = pm.getInstalledApplications(0);
        List<String> names = new ArrayList<>();
        List<String> packages = new ArrayList<>();
        for (ApplicationInfo a : apps) {
            String n = pm.getApplicationLabel(a).toString();
            String low = n.toLowerCase(Locale.ROOT);
            if (low.contains("vpn") || low.contains("wireguard") || low.contains("proton") || low.contains("nord") || low.contains("surfshark") || low.contains("windscribe")) {
                if (pm.getLaunchIntentForPackage(a.packageName) != null) { names.add(n); packages.add(a.packageName); }
            }
        }
        if (names.isEmpty()) {
            new AlertDialog.Builder(this).setTitle("VPN app").setMessage("No recognised VPN app was found. Install your preferred VPN app, then tap Refresh apps.").setPositiveButton("OK", null).show();
            return;
        }
        new AlertDialog.Builder(this).setTitle("Choose VPN app")
            .setItems(names.toArray(new String[0]), (dialog, which) -> launchPackage(packages.get(which))).show();
    }

    @Override public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) { openSettings(); return true; }
        return super.onKeyDown(keyCode, event);
    }

    private int dp(int value) { return (int)(value * getResources().getDisplayMetrics().density + 0.5f); }
}
