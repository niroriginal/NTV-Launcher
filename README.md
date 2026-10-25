# DNA Cinematic TV Launcher

A lightweight Android TV home-screen launcher project designed for Android 12 / DNA Hub 2.0.

## Included
- Google TV-inspired cinematic dark gradient home screen
- Live date and 24-hour clock
- App row populated from installed apps (up to 14, alphabetically)
- D-pad focus states and OK-to-open support
- Quick Settings shortcut
- VPN shortcut that looks for common VPN apps and lets you choose a detected app
- HOME intent filter so Android can offer it as a launcher

## Build an APK
1. Install Android Studio on a computer.
2. Open this project folder (`DNA_Cinematic_TV_Launcher`) in Android Studio.
3. Let Gradle sync. Use JDK 17 if prompted.
4. Select **Build > Build Bundle(s) / APK(s) > Build APK(s)**.
5. Find the debug APK under `app/build/outputs/apk/debug/app-debug.apk`.

This source project is provided; an APK has not been compiled or tested on a physical DNA Hub 2.0 device in this environment.

## Install on the DNA Hub 2.0
- Enable Developer options / USB debugging if your firmware allows it, then install with Android Studio or ADB:
  `adb install -r app/build/outputs/apk/debug/app-debug.apk`
- Alternatively copy the APK to USB storage and install it with a trusted file manager, if sideloading is enabled.
- Press the remote's **Home** button. If Android shows a home-app chooser, select **DNA Cinematic Launcher** and choose **Always**.
- If no chooser appears, open Settings > Apps > Default apps > Home app (wording varies by firmware) and select the launcher.

## Notes / limitations
- Some operator TV boxes lock down default-home selection. If the firmware prevents choosing a third-party HOME app, this app cannot override that restriction without device-specific workarounds.
- The launcher does not include streaming content recommendations; it presents installed apps only.
- Android package visibility and OEM firmware can affect which apps are listed. The manifest requests package visibility for this simple launcher.
- The VPN shortcut opens a detected VPN app; it does not itself provide VPN service or change VPN settings.
- Compile SDK 35 requires a recent Android SDK Platform 35 installation.

## Build the APK using only your phone (GitHub Actions)
This project now includes a GitHub Actions workflow that builds the APK in GitHub's cloud.

1. On your phone, sign in or create an account at GitHub.com.
2. Create a new repository and upload the contents of this project ZIP (extract it first; make sure `.github/workflows/build-apk.yml` is included). You can use GitHub's website in your phone browser; if hidden files are awkward to upload, use GitHub's mobile app or a browser in desktop mode.
3. Open the repository's **Actions** tab and select **Build DNA Cinematic Launcher APK**.
4. Tap **Run workflow** if it is not already running. Wait for the run to finish successfully.
5. Open the completed run and download the artifact named **DNA-Cinematic-TV-Launcher-APK**.
6. Extract the artifact ZIP on your phone to get `app-debug.apk`, then transfer it to your TV box and install it if sideloading is enabled.

Note: GitHub may require you to enable Actions for the repository. The APK is not built until the workflow finishes successfully.
