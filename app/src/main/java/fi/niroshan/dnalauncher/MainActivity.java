
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

    private final int bg = Color.rgb(12, 15, 27);
    private final int purple = Color.rgb(111, 82, 230);
    private LinearLayout appRow;

    private int dp(float value) {
        return (int) (value *
            getResources().getDisplayMetrics().density + 0.5f);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setStatusBarColor(bg);
        getWindow().setNavigationBarColor(bg);

        buildScreen();
    }

    private TextView text(
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

    private GradientDrawable rounded(int color, int radius) {
        GradientDrawable shape = new GradientDrawable();
        shape.setColor(color);
        shape.setCornerRadius(dp(radius));
        return shape;
    }

    private void buildScreen() {
        ScrollView page = new ScrollView(this);
        page.setFillViewport(true);

        GradientDrawable wallpaper = new GradientDrawable(
            GradientDrawable.Orientation.TL_BR,
            new int[] {
                Color.rgb(22, 20, 46),
                Color.rgb(12, 15, 27),
                Color.rgb(8, 11, 20)
            }
        );
        page.setBackground(wallpaper);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(42), dp(24), dp(42), dp(32));

        page.addView(root);

        // Odyssy brand header
        LinearLayout header = new LinearLayout(this);
        header.setGravity(Gravity.CENTER_VERTICAL);

        TextView logo = text("ODYSSY", 30, Color.WHITE, true);
        header.addView(logo);

        TextView brandSuffix =
            text("  TV", 15, 0xFFB8A8FF, true);
        header.addView(brandSuffix);

        View spacer = new View(this);
        header.addView(spacer,
            new LinearLayout.LayoutParams(0, 1, 1));

        TextView headerLabel = text(
            "YOUR ENTERTAINMENT",
            12, 0xFFB7C4E0, true
        );
        header.addView(headerLabel);

        root.addView(header);

        // Welcome section
        TextView eyebrow = text(
            "YOUR NEXT ADVENTURE STARTS HERE",
            11, 0xFFB9A8FF, true
        );

        LinearLayout.LayoutParams eyebrowParams =
            new LinearLayout.LayoutParams(-1, -2);
        eyebrowParams.topMargin = dp(30);

        root.addView(eyebrow, eyebrowParams);

        TextView heading = text(
            "Everything you love.",
            34, Color.WHITE, true
        );

        LinearLayout.LayoutParams headingParams =
            new LinearLayout.LayoutParams(-1, -2);
        headingParams.topMargin = dp(7);

        root.addView(heading, headingParams);

        TextView intro = text(
            "Your apps. Your screen. Your Odyssy.",
            15, 0xFFB4BCD0, false
        );

        LinearLayout.LayoutParams introParams =
            new LinearLayout.LayoutParams(-1, -2);
        introParams.topMargin = dp(5);

        root.addView(intro, introParams);

        // Cinematic hero banner
        LinearLayout hero = new LinearLayout(this);
        hero.setOrientation(LinearLayout.VERTICAL);
        hero.setGravity(Gravity.CENTER_VERTICAL);
        hero.setPadding(dp(30), dp(24), dp(30), dp(24));

        GradientDrawable heroBackground = new GradientDrawable(
            GradientDrawable.Orientation.TL_BR,
            new int[] {
                Color.rgb(112, 59, 211),
                Color.rgb(58, 57, 143),
                Color.rgb(22, 31, 60)
            }
        );
        heroBackground.setCornerRadius(dp(22));
        hero.setBackground(heroBackground);

        TextView heroTag = text(
            "ODYSSY  /  ENTERTAINMENT",
            12, 0xFFE0D8FF, true
        );
        hero.addView(heroTag);

        TextView heroTitle = text(
            "Make tonight cinematic.",
            29, Color.WHITE, true
        );

        LinearLayout.LayoutParams heroTitleParams =
            new LinearLayout.LayoutParams(-1, -2);
        heroTitleParams.topMargin = dp(12);

        hero.addView(heroTitle, heroTitleParams);

        TextView heroSubtitle = text(
            "Your favourite entertainment, all in one place.",
            15, 0xFFE0E4F6, false
        );

        LinearLayout.LayoutParams heroSubtitleParams =
            new LinearLayout.LayoutParams(-1, -2);
        heroSubtitleParams.topMargin = dp(7);

        hero.addView(heroSubtitle, heroSubtitleParams);

        LinearLayout.LayoutParams heroParams =
            new LinearLayout.LayoutParams(-1, dp(178));
        heroParams.topMargin = dp(23);

        root.addView(hero, heroParams);

        // App section
        TextView appsTitle = text(
            "Your apps", 23, Color.WHITE, true
        );

        LinearLayout.LayoutParams appsTitleParams =
            new LinearLayout.LayoutParams(-1, -2);
        appsTitleParams.topMargin = dp(28);

        root.addView(appsTitle, appsTitleParams);

        TextView hint = text(
            "Choose an app to start exploring",
            13, 0xFF9EAAC4, false
        );

        LinearLayout.LayoutParams hintParams =
            new LinearLayout.LayoutParams(-1, -2);
        hintParams.topMargin = dp(5);

        root.addView(hint, hintParams);

        HorizontalScrollView horizontal =
            new HorizontalScrollView(this);

        horizontal.setHorizontalScrollBarEnabled(false);
        horizontal.setClipToPadding(false);
        horizontal.setFocusable(false);

        appRow = new LinearLayout(this);
        appRow.setOrientation(LinearLayout.HORIZONTAL);
        appRow.setPadding(dp(5), dp(18), dp(12), dp(20));

        horizontal.addView(appRow);

        root.addView(horizontal,
            new LinearLayout.LayoutParams(-1, -2));

        setContentView(page);

        loadApps();
    }

    // Discover installed TV and regular launcher apps
    private void loadApps() {
        appRow.removeAllViews();

        PackageManager pm = getPackageManager();

        Map<String, ResolveInfo> found =
            new LinkedHashMap<>();

        Intent tvIntent = new Intent(Intent.ACTION_MAIN);
        tvIntent.addCategory(
            Intent.CATEGORY_LEANBACK_LAUNCHER);

        List<ResolveInfo> tvApps =
            pm.queryIntentActivities(tvIntent, 0);

        for (ResolveInfo info : tvApps) {
            addApp(found, info);
        }

        Intent normalIntent = new Intent(Intent.ACTION_MAIN);
        normalIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> normalApps =
            pm.queryIntentActivities(normalIntent, 0);

        for (ResolveInfo info : normalApps) {
            addApp(found, info);
        }

        List<ResolveInfo> apps =
            new ArrayList<>(found.values());

        apps.sort((a, b) ->
            String.CASE_INSENSITIVE_ORDER.compare(
                a.loadLabel(pm).toString(),
                b.loadLabel(pm).toString()
            )
        );

        if (apps.isEmpty()) {
            TextView empty = text(
                "No launchable apps found.",
                16, Color.WHITE, false
            );
            appRow.addView(empty);
            return;
        }

        for (ResolveInfo info : apps) {
            addAppCard(info, pm);
        }

        appRow.getChildAt(0).requestFocus();
    }

    private void addApp(
            Map<String, ResolveInfo> found,
            ResolveInfo info) {

        if (info.activityInfo == null) return;

        String packageName =
            info.activityInfo.packageName;

        if (packageName.equals(getPackageName())) return;

        if (!found.containsKey(packageName)) {
            found.put(packageName, info);
        }
    }

    // Cinematic app cards with real icons and remote focus
    private void addAppCard(
            ResolveInfo info, PackageManager pm) {

        String appName = info.loadLabel(pm).toString();

        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setGravity(Gravity.CENTER);
        card.setFocusable(true);
        card.setClickable(true);
        card.setPadding(dp(9), dp(12), dp(9), dp(12));

        GradientDrawable normal = new GradientDrawable();
        normal.setColor(Color.rgb(30, 36, 57));
        normal.setCornerRadius(dp(17));
        normal.setStroke(dp(1), Color.rgb(48, 56, 81));
        card.setBackground(normal);

        LinearLayout.LayoutParams cardParams =
            new LinearLayout.LayoutParams(dp(150), dp(154));
        cardParams.setMargins(0, 0, dp(15), 0);

        appRow.addView(card, cardParams);

        ImageView icon = new ImageView(this);
        icon.setImageDrawable(info.loadIcon(pm));
        icon.setScaleType(ImageView.ScaleType.FIT_CENTER);

        LinearLayout.LayoutParams iconParams =
            new LinearLayout.LayoutParams(dp(67), dp(67));
        iconParams.bottomMargin = dp(12);

        card.addView(icon, iconParams);

        TextView name = text(
            appName, 13, Color.WHITE, true);
        name.setGravity(Gravity.CENTER);
        name.setMaxLines(2);

        card.addView(name);

        card.setOnFocusChangeListener((view, focused) -> {
            GradientDrawable state = new GradientDrawable();
            state.setCornerRadius(dp(17));

            if (focused) {
                state.setColor(purple);
                state.setStroke(dp(2), 0xFFD7CCFF);
                view.setElevation(dp(8));
                view.setScaleX(1.06f);
                view.setScaleY(1.06f);
            } else {
                state.setColor(Color.rgb(30, 36, 57));
                state.setStroke(dp(1),
                    Color.rgb(48, 56, 81));
                view.setElevation(0);
                view.setScaleX(1f);
                view.setScaleY(1f);
            }

            view.setBackground(state);
        });

        card.setOnClickListener(view -> launchApp(info));
    }

    // Keep real app launch handling
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
                info.activityInfo.name
            );

            Intent launch = new Intent(Intent.ACTION_MAIN);
            launch.addCategory(Intent.CATEGORY_LAUNCHER);
            launch.setComponent(component);
            launch.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            try {
                startActivity(launch);
            } catch (Exception firstError) {
                Intent tvLaunch = new Intent(Intent.ACTION_MAIN);
                tvLaunch.addCategory(
                    Intent.CATEGORY_LEANBACK_LAUNCHER);
                tvLaunch.setComponent(component);
                tvLaunch.addFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(tvLaunch);
            }

        } catch (Exception e) {
            Toast.makeText(this,
                "Could not open " +
                info.loadLabel(getPackageManager()),
                Toast.LENGTH_LONG).show();
        }
    }
}
