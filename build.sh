SCRIPT_DIR="$(
  cd "$(dirname "$0")" || exit
  pwd
)"

echo "Current directory: " "$SCRIPT_DIR"

BUILD_DIR="$SCRIPT_DIR"/build

if [ ! -e "$BUILD_DIR" ]; then
  mkdir -p "$BUILD_DIR"
  echo "Directory created: " "$BUILD_DIR"
fi

cd "$BUILD_DIR" || exit 1

LUNACHAT_DIR="$BUILD_DIR"/LunaChat

if [ -e "$LUNACHAT_DIR" ]; then
  rm -rf LunaChat
  echo "Directory deleted: " "$LUNACHAT_DIR"
fi

git clone https://github.com/ucchyocean/LunaChat

cd "$LUNACHAT_DIR" || exit 1

for patch in "$SCRIPT_DIR"/patches/*.patch; do
  git apply "$patch"
  echo "Patch applied: " "$patch"
done

mvn package

cp "$SCRIPT_DIR"/build/LunaChat/target/LunaChat.jar "$SCRIPT_DIR"