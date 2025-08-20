#!/bin/bash
set -e
# Usage: ./build_signed_apk.sh [keystore_password] [key_alias]
# Example: ./build_signed_apk.sh mypass myalias
KS_PASS=${1:-changeit}
KEY_ALIAS=${2:-cryptointelkey}
KEYSTORE_DIR=keystore
KEYSTORE_PATH="$KEYSTORE_DIR/release.keystore"

mkdir -p "$KEYSTORE_DIR"

if [ ! -f "$KEYSTORE_PATH" ]; then
  echo "Generating keystore at $KEYSTORE_PATH (you need keytool available - part of JDK)"
  keytool -genkeypair -v -keystore "$KEYSTORE_PATH" -alias "$KEY_ALIAS" -keyalg RSA -keysize 2048 -validity 10000 -storepass "$KS_PASS" -keypass "$KS_PASS" -dname "CN=CryptoIntel, OU=Dev, O=Personal, L=Dhaka, S=Dhaka, C=BD"
else
  echo "Keystore already exists at $KEYSTORE_PATH"
fi

# Create gradle.properties.local with signing info
cat > gradle.properties.local <<EOL
RELEASE_STORE_FILE=$KEYSTORE_PATH
RELEASE_STORE_PASSWORD=$KS_PASS
RELEASE_KEY_ALIAS=$KEY_ALIAS
RELEASE_KEY_PASSWORD=$KS_PASS
EOL

echo "Now building signed APK (this requires gradle/Android SDK)."
echo "Run in Android Studio or use gradlew: ./gradlew assembleRelease"
echo "After build, the signed APK will be in app/build/outputs/apk/release/"