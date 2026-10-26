
package fi.niroshan.dnalauncher;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity {

    private final int bg = Color.rgb(13, 16, 29);
    private final int purple = Color.rgb(120, 91, 255);
    private LinearLayout appRow;

    private int dp(float value) {
        return (int) (value * getResources().getDisplayMetrics().density + 0.5f);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(bg);
        getWindow().setNavigationBarColor(bg);
        getWindow().getDecorView().setSystemUiVisibility(0);
        buildScreen();
    }

    private GradientDrawable rounded(int color, int radius) {
        GradientDrawable shape = new GradientDrawable();
        shape.setColor(color);
        shape.setCornerRadius(dp(radius));
        return shape;
    }

    private TextView text(String value, int size, int color, boolean bold) {
        TextView t = new TextView(this);
        t.setText(value);
        t.setTextSize(size);
        t.setTextColor(color);
        t.setGravity(Gravity.CENTER_VERTICAL);
        if (bold) t.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        return t;
    }

    private void buildScreen() {
        ScrollView page = new ScrollView(this);
        page.setFillViewport(true);
        page.setBackgroundColor(bg);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(42), dp(24), dp(42), dp(30));
        page.addView(root);

        LinearLayout header = new LinearLayout(this);
        header.setGravity(Gravity.CENTER_VERTICAL);
        header.setOrientation(LinearLayout.HORIZONTAL);

        TextView logo = text("DNA", 30, Color.WHITE, true);
        header.addView(logo);

        TextView subtitle = text("  CINEMA", 15, 0xFFB9B3FF, true);
        header.addView(subtitle);

        View spacer = new View(this);
        header.addView(spacer, new LinearLayout.LayoutParams(0, 1, 1));

        TextView clockLabel = text("YOUR ENTERTAINMENT", 12, 0xFFB6BED2, true);
        header.addView(clockLabel);
        root.addView(header);

        TextView welcome = text("Everything you love.", 32, Color.WHITE, true);
        LinearLayout.LayoutParams welcomeParams =
                new LinearLayout.LayoutParams(-1, -2);
        welcomeParams.topMargin = dp(24);
        root.addView(welcome, welcomeParams);

        TextView description = text(
                "Your apps. Your screen. Your DNA Hub.",
                15, 0xFFB6BED2, false);
        LinearLayout.LayoutParams descParams =
                new LinearLayout.LayoutParams(-1, -2);
        descParams.topMargin = dp(5);
        root.addView(description, descParams);

        LinearLayout banner = new LinearLayout(this);
        banner.setOrientation(LinearLayout.VERTICAL);
        banner.setGravity(Gravity.CENTER_VERTICAL);
        banner.setPadding(dp(28), dp(18), dp(28), dp(18));

        GradientDrawable bannerBg = new GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT,
                new int[] {
                        Color.rgb(77, 53, 161),
                        Color.rgb(37, 52, 103),
                        Color.rgb(24, 30, 52)
                });
        bannerBg.setCornerRadius(dp(20));
        banner.setBackground(bannerBg);
        banner.setFocusable(false);

        TextView bannerTag = text(
                "WELCOME HOME", 12, 0xFFDAD4FF, true);
        banner.addView(bannerTag);

        TextView bannerTitle = text(
                "Entertainment starts here",
                25, Color.WHITE, true);
        LinearLayout.LayoutParams titleParams =
                new LinearLayout.LayoutParams(-1, -2);
        titleParams.topMargin = dp(8);
        banner.addView(bannerTitle, titleParams);

        TextView bannerText = text(
                "Choose an app below to get started.",
                14, 0xFFE0E3F0, false);
        LinearLayout.LayoutParams bannerTextParams =
                new LinearLayout.LayoutParams(-1, -2);
        bannerTextParams.topMargin = dp(5);
        banner.addView(bannerText, bannerTextParams);

        LinearLayout.LayoutParams bannerParams =
                new LinearLayout.LayoutParams(-1, dp(155));
        bannerParams.topMargin = dp(22);
        root.addView(banner, bannerParams);

        TextView appsTitle = text(
                "Your apps", 22, Color.WHITE, true);
        LinearLayout.LayoutParams appsTitleParams =
                new LinearLayout.LayoutParams(-1, -2);
        appsTitleParams.topMargin = dp(27);
        root.addView(appsTitle, appsTitleParams);

        TextView appsHint = text(
                "Select an app with your remote",
                13, 0xFF9EA8C0, false);
        LinearLayout.LayoutParams hintParams =
                new LinearLayout.LayoutParams(-1, -2);
        hintParams.topMargin = dp(4);
        root.addView(appsHint, hintParams);

        HorizontalScrollView horizontal = new HorizontalScrollView(this);
        horizontal.setHorizontalScrollBarEnabled(false);
        horizontal.setClipToPadding(false);
        horizontal.setFocusable(false);

        appRow = new LinearLayout(this);
        appRow.setOrientation(LinearLayout.HORIZONTAL);
        appRow.setPadding(dp(4), dp(14), dp(8), dp(18));

        horizontal.addView(appRow);
        LinearLayout.LayoutParams rowParams =
                new LinearLayout.LayoutParams(-1, -2);
        root.addView(horizontal, rowParams);

        setContentView(page);
        loadApps();
    }

    private void loadApps() {
        appRow.removeAllViews();

        PackageManager pm = getPackageManager();
        Map<String, ResolveInfo> found = new LinkedHashMap<>();

        Intent tvIntent = new Intent(Intent.ACTION_MAIN);
        tvIntent.addCategory(Intent.CATEGORY_LEANBACK_LAUNCHER);

        List<ResolveInfo> tvApps =
                pm.queryIntentActivities(tvIntent, 0);

        for (ResolveInfo info : tvApps) {
            addApp(found, info);
        }

        Intent phoneIntent = new Intent(Intent.ACTION_MAIN);
        phoneIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> normalApps =
                pm.queryIntentActivities(phoneIntent, 0);

        for (ResolveInfo info : normalApps) {
            addApp(found, info);
        }

        List<ResolveInfo> apps = new ArrayList<>(found.values());

        apps.sort((a, b) -> String.CASE_INSENSITIVE_ORDER.compare(
                a.loadLabel(pm).toString(),
                b.loadLabel(pm).toString()));

        if (apps.isEmpty()) {
            TextView empty = text(
                    "No launchable apps found. Check app visibility settings.",
                    16, Color.WHITE, false);
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
        String appName = info.loadLabel(pm).toString();

        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setGravity(Gravity.CENTER);
        card.setFocusable(true);
        card.setClickable(true);
        card.setPadding(dp(8), dp(10), dp(8), dp(10));
        card.setBackground(rounded(Color.rgb(31, 37, 57), 14));

        LinearLayout.LayoutParams cardParams =
                new LinearLayout.LayoutParams(dp(142), dp(142));
        cardParams.setMargins(0, 0, dp(14), 0);
        appRow.addView(card, cardParams);

        ImageView icon = new ImageView(this);
        icon.setImageDrawable(info.loadIcon(pm));
        icon.setScaleType(ImageView.ScaleType.FIT_CENTER);

        LinearLayout.LayoutParams iconParams =
                new LinearLayout.LayoutParams(dp(66), dp(66));
        iconParams.bottomMargin = dp(10);
        card.addView(icon, iconParams);

        TextView name = text(appName, 13, Color.WHITE, true);
        name.setGravity(Gravity.CENTER);
        name.setMaxLines(2);
        card.addView(name);

        card.setOnFocusChangeListener((view, focused) -> {
            if (focused) {
                view.setBackground(rounded(purple, 14));
                view.setScaleX(1.06f);
                view.setScaleY(1.06f);
            } else {
                view.setBackground(
                        rounded(Color.rgb(31, 37, 57), 14));
                view.setScaleX(1f);
                view.setScaleY(1f);
            }
        });

        card.setOnClickListener(view -> launchApp(info));
    }

    private void launchApp(ResolveInfo info) {
        try {
            if (info.activityInfo == null) {
                Toast.makeText(this,
                        "App activity not found",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            ComponentName component = new ComponentName(
                    info.activityInfo.packageName,
                    info.activityInfo.name);

            Intent launch = new Intent(Intent.ACTION_MAIN);
            launch.addCategory(Intent.CATEGORY_LAUNCHER);
            launch.setComponent(component);
            launch.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            try {
                startActivity(launch);
            } catch (Exception firstError) {
                Intent tvLaunch = new Intent(Intent.ACTION_MAIN);
                tvLaunch.addCategory(Intent.CATEGORY_LEANBACK_LAUNCHER);
                tvLaunch.setComponent(component);
                tvLaunch.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(tvLaunch);
            }

        } catch (Exception e) {
            Toast.makeText(this,
                    "Could not open " + info.loadLabel(getPackageManager()),
                    Toast.LENGTH_LONG).show();
        }
    }
}
