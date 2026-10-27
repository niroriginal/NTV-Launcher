
package fi.niroshan.dnalauncher;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.content.ComponentName;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends Activity {

    private LinearLayout root;
    private LinearLayout appRow;
    private LinearLayout recentRow;
    private LinearLayout servicesRow;
    private View wallpaperView;
    private SharedPreferences prefs;
    private List<ResolveInfo> currentApps = new ArrayList<>();

    private String wallpaper = "Aurora";
    private String accent = "Violet";
    private boolean animations = true;

    private final int white = Color.WHITE;
    private final int muted = 0xFFB8C1D8;

    private int dp(float value) {
        return (int) (value *
            getResources().getDisplayMetrics().density + 0.5f);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (prefs != null && recentRow != null) {
            populateRecentApps(currentApps, getPackageManager());
        }
    }

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);

        prefs = getSharedPreferences("odyssy_settings", MODE_PRIVATE);
        wallpaper = prefs.getString("wallpaper", "Aurora");
        accent = prefs.getString("accent", "Violet");
        animations = prefs.getBoolean("animations", true);

        getWindow().setStatusBarColor(Color.rgb(10, 12, 25));
        getWindow().setNavigationBarColor(Color.rgb(10, 12, 25));

        buildScreen();
    }

    private int accentColor() {
        switch (accent) {
            case "Blue":
                return Color.rgb(52, 125, 246);
            case "Pink":
                return Color.rgb(225, 75, 163);
            case "Green":
                return Color.rgb(38, 170, 126);
            case "Amber":
                return Color.rgb(227, 145, 45);
            default:
                return Color.rgb(119, 83, 244);
        }
    }

    private int[] wallpaperColors() {
        switch (wallpaper) {
            case "Midnight":
                return new int[] {
                    Color.rgb(30, 35, 58),
                    Color.rgb(12, 15, 27),
                    Color.rgb(5, 7, 15)
                };
            case "Ocean":
                return new int[] {
                    Color.rgb(16, 105, 143),
                    Color.rgb(16, 42, 77),
                    Color.rgb(7, 13, 28)
                };
            case "Sunset":
                return new int[] {
                    Color.rgb(169, 72, 91),
                    Color.rgb(75, 43, 96),
                    Color.rgb(14, 15, 34)
                };
            default:
                return new int[] {
                    accentColor(),
                    Color.rgb(35, 38, 86),
                    Color.rgb(8, 11, 24)
                };
        }
    }

    private GradientDrawable gradient(int[] colors, int radius) {
        GradientDrawable d = new GradientDrawable(
            GradientDrawable.Orientation.TL_BR, colors);
        d.setCornerRadius(dp(radius));
        return d;
    }

    private TextView makeText(
            String value, int size, int color, boolean bold) {
        TextView t = new TextView(this);
        t.setText(value);
        t.setTextSize(size);
        t.setTextColor(color);
        t.setGravity(Gravity.CENTER_VERTICAL);
        if (bold) {
            t.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        }
        return t;
    }

    private LinearLayout vertical() {
        LinearLayout l = new LinearLayout(this);
        l.setOrientation(LinearLayout.VERTICAL);
        return l;
    }

    private LinearLayout horizontal() {
        LinearLayout l = new LinearLayout(this);
        l.setOrientation(LinearLayout.HORIZONTAL);
        l.setGravity(Gravity.CENTER_VERTICAL);
        return l;
    }

    private void buildScreen() {
        ScrollView page = new ScrollView(this);
        page.setFillViewport(true);
        page.setBackground(gradient(wallpaperColors(), 0));

        root = vertical();
        root.setPadding(dp(38), dp(24), dp(38), dp(32));
        page.addView(root);

        // Brand header
        LinearLayout header = horizontal();

        TextView logo = makeText("ODYSSY", 29, white, true);
        header.addView(logo);

        TextView suffix = makeText("  TV", 14, accentColor(), true);
        header.addView(suffix);

        View spacer = new View(this);
        header.addView(spacer,
            new LinearLayout.LayoutParams(0, 1, 1));

        TextView settings = makeText("⚙  SETTINGS", 13, white, true);
        settings.setPadding(dp(15), dp(10), dp(15), dp(10));
        settings.setBackground(gradient(
            new int[] { 0x443F4A70, 0x223F4A70 }, 14));
        settings.setFocusable(true);
        settings.setOnClickListener(v -> showSettings());
        header.addView(settings);

        root.addView(header);

        TextView eyebrow = makeText(
            "YOUR ENTERTAINMENT UNIVERSE",
            11, accentColor(), true);
        LinearLayout.LayoutParams ep =
            new LinearLayout.LayoutParams(-1, -2);
        ep.topMargin = dp(28);
        root.addView(eyebrow, ep);

        TextView title = makeText(
            "Everything you love.", 34, white, true);
        LinearLayout.LayoutParams tp =
            new LinearLayout.LayoutParams(-1, -2);
        tp.topMargin = dp(7);
        root.addView(title, tp);

        TextView intro = makeText(
            "One screen. Endless possibilities.",
            15, muted, false);
        LinearLayout.LayoutParams ip =
            new LinearLayout.LayoutParams(-1, -2);
        ip.topMargin = dp(5);
        root.addView(intro, ip);

        // Cinematic hero banner
        LinearLayout hero = vertical();
        hero.setGravity(Gravity.CENTER_VERTICAL);
        hero.setPadding(dp(28), dp(22), dp(28), dp(22));
        hero.setBackground(gradient(wallpaperColors(), 22));
        hero.setFocusable(false);

        TextView heroTag = makeText(
            "O D Y S S Y   /   HOME",
            12, 0xFFE7E0FF, true);
        hero.addView(heroTag);

        TextView heroTitle = makeText(
            "Your next adventure starts here.",
            27, white, true);
        LinearLayout.LayoutParams htp =
            new LinearLayout.LayoutParams(-1, -2);
        htp.topMargin = dp(12);
        hero.addView(heroTitle, htp);

        TextView heroDesc = makeText(
            "Discover your favourite apps and entertainment.",
            14, 0xFFE1E5F5, false);
        LinearLayout.LayoutParams hdp =
            new LinearLayout.LayoutParams(-1, -2);
        hdp.topMargin = dp(8);
        hero.addView(heroDesc, hdp);

        LinearLayout.LayoutParams hp =
            new LinearLayout.LayoutParams(-1, dp(170));
        hp.topMargin = dp(22);
        root.addView(hero, hp);

        // Streaming services: direct app launches, never generic web links.
        addSectionTitle(root, "Your streaming services", 24);
        TextView servicesHint = makeText(
            "Open your installed streaming apps", 13, muted, false);
        root.addView(servicesHint);
        HorizontalScrollView servicesScroll = new HorizontalScrollView(this);
        servicesScroll.setHorizontalScrollBarEnabled(false);
        servicesScroll.setFocusable(false);
        servicesRow = horizontal();
        servicesRow.setPadding(dp(5), dp(14), dp(12), dp(18));
        servicesScroll.addView(servicesRow);
        root.addView(servicesScroll, new LinearLayout.LayoutParams(-1, -2));

        addSectionTitle(root, "Recently opened", 8);
        TextView recentHint = makeText(
            "Apps you launched recently on this device", 13, muted, false);
        root.addView(recentHint);
        HorizontalScrollView recentScroll = new HorizontalScrollView(this);
        recentScroll.setHorizontalScrollBarEnabled(false);
        recentScroll.setFocusable(false);
        recentRow = horizontal();
        recentRow.setPadding(dp(5), dp(14), dp(12), dp(18));
        recentScroll.addView(recentRow);
        root.addView(recentScroll, new LinearLayout.LayoutParams(-1, -2));

        TextView appsTitle = makeText(
            "All your apps", 23, white, true);
        LinearLayout.LayoutParams atp =
            new LinearLayout.LayoutParams(-1, -2);
        atp.topMargin = dp(26);
        root.addView(appsTitle, atp);

        TextView hint = makeText(
            "Choose an app to begin",
            13, muted, false);
        LinearLayout.LayoutParams hintp =
            new LinearLayout.LayoutParams(-1, -2);
        hintp.topMargin = dp(5);
        root.addView(hint, hintp);

        HorizontalScrollView hsv = new HorizontalScrollView(this);
        hsv.setHorizontalScrollBarEnabled(false);
        hsv.setFocusable(false);
        hsv.setClipToPadding(false);

        appRow = horizontal();
        appRow.setPadding(dp(5), dp(17), dp(12), dp(20));
        hsv.addView(appRow);

        root.addView(hsv,
            new LinearLayout.LayoutParams(-1, -2));

        setContentView(page);
        loadApps();
    }

    private void loadApps() {
        appRow.removeAllViews();

        PackageManager pm = getPackageManager();
        Map<String, ResolveInfo> found = new LinkedHashMap<>();

        Intent tv = new Intent(Intent.ACTION_MAIN);
        tv.addCategory(Intent.CATEGORY_LEANBACK_LAUNCHER);

        for (ResolveInfo r : pm.queryIntentActivities(tv, 0)) {
            addApp(found, r);
        }

        Intent normal = new Intent(Intent.ACTION_MAIN);
        normal.addCategory(Intent.CATEGORY_LAUNCHER);

        for (ResolveInfo r : pm.queryIntentActivities(normal, 0)) {
            addApp(found, r);
        }

        List<ResolveInfo> apps = new ArrayList<>(found.values());
        apps.sort((a, b) ->
            String.CASE_INSENSITIVE_ORDER.compare(
                a.loadLabel(pm).toString(),
                b.loadLabel(pm).toString()));
        currentApps = apps;

        populateStreamingServices(apps, pm);
        populateRecentApps(apps, pm);

        if (apps.isEmpty()) {
            TextView empty = makeText(
                "No launchable apps found.", 16, white, false);
            appRow.addView(empty);
            return;
        }

        for (ResolveInfo info : apps) {
            addAppCard(info, pm);
        }

        appRow.getChildAt(0).requestFocus();
    }

    private void addApp(
            Map<String, ResolveInfo> found, ResolveInfo info) {
        if (info.activityInfo == null) return;

        String packageName = info.activityInfo.packageName;
        if (packageName.equals(getPackageName())) return;

        if (!found.containsKey(packageName)) {
            found.put(packageName, info);
        }
    }

    private void addAppCard(ResolveInfo info, PackageManager pm) {
        LinearLayout card = vertical();
        card.setGravity(Gravity.CENTER);
        card.setPadding(dp(9), dp(12), dp(9), dp(12));
        card.setFocusable(true);
        card.setClickable(true);
        card.setBackground(gradient(
            new int[] { 0xFF252B43, 0xFF1B2035 }, 17));

        LinearLayout.LayoutParams cp =
            new LinearLayout.LayoutParams(dp(148), dp(151));
        cp.setMargins(0, 0, dp(14), 0);
        appRow.addView(card, cp);

        ImageView icon = new ImageView(this);
        icon.setImageDrawable(info.loadIcon(pm));
        icon.setScaleType(ImageView.ScaleType.FIT_CENTER);

        LinearLayout.LayoutParams iconp =
            new LinearLayout.LayoutParams(dp(65), dp(65));
        iconp.bottomMargin = dp(11);
        card.addView(icon, iconp);

        TextView name = makeText(
            info.loadLabel(pm).toString(), 13, white, true);
        name.setGravity(Gravity.CENTER);
        name.setMaxLines(2);
        card.addView(name);

        card.setOnFocusChangeListener((v, focused) -> {
            if (focused) {
                v.setBackground(gradient(
                    new int[] { accentColor(), accentColor() }, 17));
                v.setElevation(dp(8));

                if (animations) {
                    v.animate().scaleX(1.06f).scaleY(1.06f)
                        .setDuration(160)
                        .setInterpolator(new DecelerateInterpolator())
                        .start();
                } else {
                    v.setScaleX(1.04f);
                    v.setScaleY(1.04f);
                }
            } else {
                v.setBackground(gradient(
                    new int[] { 0xFF252B43, 0xFF1B2035 }, 17));
                v.setElevation(0);

                if (animations) {
                    v.animate().scaleX(1f).scaleY(1f)
                        .setDuration(140).start();
                } else {
                    v.setScaleX(1f);
                    v.setScaleY(1f);
                }
            }
        });

        card.setOnClickListener(v -> launchApp(info));
    }

    private void launchApp(ResolveInfo info) {
        if (info.activityInfo == null) return;
        rememberRecentApp(info.activityInfo.packageName);

        ComponentName component = new ComponentName(
            info.activityInfo.packageName,
            info.activityInfo.name);

        Intent launch = new Intent(Intent.ACTION_MAIN);
        launch.addCategory(Intent.CATEGORY_LAUNCHER);
        launch.setComponent(component);
        launch.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        try {
            startActivity(launch);
        } catch (Exception first) {
            try {
                Intent tvLaunch = new Intent(Intent.ACTION_MAIN);
                tvLaunch.addCategory(
                    Intent.CATEGORY_LEANBACK_LAUNCHER);
                tvLaunch.setComponent(component);
                tvLaunch.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(tvLaunch);
            } catch (Exception second) {
                Toast.makeText(this,
                    "Could not open " +
                    info.loadLabel(getPackageManager()),
                    Toast.LENGTH_LONG).show();
            }
        }
    }

    // Settings panel
    private void showSettings() {
        LinearLayout panel = vertical();
        panel.setPadding(dp(22), dp(20), dp(22), dp(22));
        panel.setBackground(gradient(
            new int[] { 0xFF20253B, 0xFF121729 }, 22));

        TextView heading = makeText(
            "Odyssy Settings", 25, white, true);
        panel.addView(heading);

        TextView description = makeText(
            "Personalise your home screen",
            14, muted, false);
        LinearLayout.LayoutParams descp =
            new LinearLayout.LayoutParams(-1, -2);
        descp.topMargin = dp(5);
        panel.addView(description, descp);

        addSettingHeading(panel, "WALLPAPER");

        String[] wallpapers = {
            "Aurora", "Midnight", "Ocean", "Sunset"
        };

        for (String item : wallpapers) {
            addOption(panel, item,
                item.equals(wallpaper), () -> {
                    wallpaper = item;
                    saveSettings();
                    rebuild();
                });
        }

        addSettingHeading(panel, "ACCENT COLOUR");

        String[] accents = {
            "Violet", "Blue", "Pink", "Green", "Amber"
        };

        for (String item : accents) {
            addOption(panel, item,
                item.equals(accent), () -> {
                    accent = item;
                    saveSettings();
                    rebuild();
                });
        }

        addSettingHeading(panel, "ANIMATIONS");

        addOption(panel,
            animations ? "Animations: ON" : "Animations: OFF",
            true, () -> {
                animations = !animations;
                saveSettings();
                showSettings();
            });

        addOption(panel, "Close settings", false, () -> {
            rebuild();
        });

        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(false);
        scroll.setBackgroundColor(0xFF101426);
        scroll.addView(panel);

        android.app.AlertDialog dialog =
            new android.app.AlertDialog.Builder(this)
                .setView(scroll)
                .create();

        dialog.setOnDismissListener(d -> {
            if (!isFinishing()) {
                // The home screen remains available behind settings.
            }
        });

        dialog.show();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent);
            dialog.getWindow().setLayout(
                getResources().getDisplayMetrics().widthPixels * 3 / 4,
                getResources().getDisplayMetrics().heightPixels * 4 / 5);
        }
    }

    private void addSettingHeading(
            LinearLayout panel, String label) {
        TextView heading = makeText(
            label, 12, accentColor(), true);
        LinearLayout.LayoutParams p =
            new LinearLayout.LayoutParams(-1, -2);
        p.topMargin = dp(18);
        p.bottomMargin = dp(5);
        panel.addView(heading, p);
    }

    private void addOption(
            LinearLayout panel,
            String label,
            boolean selected,
            Runnable action) {

        TextView option = makeText(
            (selected ? "✓   " : "     ") + label,
            16, white, selected);

        option.setPadding(dp(14), dp(12), dp(14), dp(12));
        option.setFocusable(true);
        option.setClickable(true);
        option.setBackground(rounded(
            selected ? 0xFF39345F : 0xFF292F46, 12));

        LinearLayout.LayoutParams p =
            new LinearLayout.LayoutParams(-1, -2);
        p.topMargin = dp(4);
        panel.addView(option, p);

        option.setOnClickListener(v -> action.run());

        option.setOnFocusChangeListener((v, focused) -> {
            if (focused) {
                v.setBackground(rounded(accentColor(), 12));
            } else {
                v.setBackground(rounded(
                    selected ? 0xFF39345F : 0xFF292F46, 12));
            }
        });
    }

    private void saveSettings() {
        prefs.edit()
            .putString("wallpaper", wallpaper)
            .putString("accent", accent)
            .putBoolean("animations", animations)
            .apply();
    }

    private void addSectionTitle(LinearLayout parent, String title, int topMarginDp) {
        TextView heading = makeText(title, 23, white, true);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, -2);
        params.topMargin = dp(topMarginDp);
        parent.addView(heading, params);
    }

    private void populateStreamingServices(List<ResolveInfo> apps, PackageManager pm) {
        servicesRow.removeAllViews();
        addDetectedService(apps, pm, "Netflix", new String[] {
            "com.netflix.ninja", "netflix"
        });
        addDetectedService(apps, pm, "Apple TV", new String[] {
            "com.apple.atve.androidtv.appletv", "com.apple.atve.sony.appletv", "apple tv", "appletv"
        });
        if (servicesRow.getChildCount() == 0) {
            TextView note = makeText(
                "Netflix and Apple TV will appear here when installed and visible to Android.",
                14, muted, false);
            servicesRow.addView(note);
        }
    }

    private void addDetectedService(List<ResolveInfo> apps, PackageManager pm,
                                    String serviceName, String[] identifiers) {
        ResolveInfo match = null;
        for (ResolveInfo info : apps) {
            if (info.activityInfo == null) continue;
            String packageName = info.activityInfo.packageName.toLowerCase(Locale.ROOT);
            String label = info.loadLabel(pm).toString().toLowerCase(Locale.ROOT);
            for (String id : identifiers) {
                String key = id.toLowerCase(Locale.ROOT);
                if (packageName.contains(key) || label.contains(key)) {
                    match = info;
                    break;
                }
            }
            if (match != null) break;
        }
        if (match == null) return;
        addLaunchCard(servicesRow, serviceName, "Open streaming app", match, pm, true);
    }

    private void populateRecentApps(List<ResolveInfo> apps, PackageManager pm) {
        recentRow.removeAllViews();
        String saved = prefs.getString("recent_packages", "");
        if (saved.isEmpty()) {
            TextView note = makeText(
                "Your recently opened apps will appear here.", 14, muted, false);
            recentRow.addView(note);
            return;
        }

        String[] packages = saved.split("\\|");
        int count = 0;
        for (String packageName : packages) {
            if (count >= 8) break;
            for (ResolveInfo info : apps) {
                if (info.activityInfo != null
                        && packageName.equals(info.activityInfo.packageName)) {
                    addLaunchCard(recentRow, info.loadLabel(pm).toString(),
                        "Recently opened", info, pm, false);
                    count++;
                    break;
                }
            }
        }
        if (count == 0) {
            TextView note = makeText(
                "Your recently opened apps will appear here.", 14, muted, false);
            recentRow.addView(note);
        }
    }

    private void rememberRecentApp(String packageName) {
        String existing = prefs.getString("recent_packages", "");
        List<String> recent = new ArrayList<>();
        recent.add(packageName);
        if (!existing.isEmpty()) {
            for (String item : existing.split("\\|")) {
                if (!item.equals(packageName) && !item.isEmpty() && recent.size() < 8) {
                    recent.add(item);
                }
            }
        }
        StringBuilder saved = new StringBuilder();
        for (String item : recent) {
            if (saved.length() > 0) saved.append('|');
            saved.append(item);
        }
        prefs.edit().putString("recent_packages", saved.toString()).apply();
    }

    private void addLaunchCard(LinearLayout parent, String title, String subtitle,
                               ResolveInfo info, PackageManager pm, boolean service) {
        LinearLayout card = vertical();
        card.setGravity(Gravity.CENTER_VERTICAL);
        card.setPadding(dp(18), dp(14), dp(18), dp(14));
        card.setFocusable(true);
        card.setClickable(true);
        card.setBackground(gradient(new int[] { 0xFF252B43, 0xFF171C30 }, 18));

        TextView name = makeText(title, 18, white, true);
        card.addView(name);
        TextView detail = makeText(subtitle, 12, muted, false);
        LinearLayout.LayoutParams detailParams = new LinearLayout.LayoutParams(-1, -2);
        detailParams.topMargin = dp(5);
        card.addView(detail, detailParams);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dp(service ? 220 : 190), dp(82));
        params.setMargins(0, 0, dp(12), 0);
        parent.addView(card, params);

        card.setOnFocusChangeListener((v, focused) -> {
            v.setBackground(gradient(
                focused ? new int[] { accentColor(), accentColor() }
                        : new int[] { 0xFF252B43, 0xFF171C30 }, 18));
            v.setScaleX(focused ? 1.04f : 1f);
            v.setScaleY(focused ? 1.04f : 1f);
        });
        card.setOnClickListener(v -> launchApp(info));
    }

    private void rebuild() {
        buildScreen();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            showSettings();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
private android.graphics.drawable.GradientDrawable rounded(int color, int radius) {
    android.graphics.drawable.GradientDrawable drawable =
            new android.graphics.drawable.GradientDrawable();
    drawable.setColor(color);
    drawable.setCornerRadius(radius * getResources().getDisplayMetrics().density);
    return drawable;
}
}
