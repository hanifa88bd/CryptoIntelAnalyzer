# Crypto Intel Analyzer (Android)
**Personal Use Only — Not Public**

About (English):
```
Crypto Intel Analyzer
Developed by Dhrubo
Code created by Rini

Personal Use Only — Not Public
```

## Build
Open in Android Studio (Giraffe+), then Run.

## API
Default base URL: `http://10.0.2.2:8000` (Android emulator → host machine).
Change it at runtime on Home screen. For physical device, set your PC's LAN IP, e.g. `http://192.168.0.10:8000` and ensure the FastAPI demo is running.

## Note
This app expects the FastAPI demo from our previous step. You can also point it to any compatible endpoint with the same routes.

---

## Quick signed APK helper
We've added `build_signed_apk.sh` to help create a keystore and set up local signing properties.

Usage (on your dev machine with JDK & Android SDK):
```bash
./build_signed_apk.sh mykeystorepass myalias
./gradlew assembleRelease
# Signed APK will be in app/build/outputs/apk/release/
```

Notes:
- The project default API_BASE_URL has been set to `http://192.168.0.10:8000`. Change it on the Home screen or in `app/build.gradle.kts` if needed.
- You still need Android Studio / Gradle to compile the APK locally; I cannot run Gradle here, but this script automates keystore creation and signing config.
