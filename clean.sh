SCRIPT_DIR="$(
  cd "$(dirname "$0")" || exit
  pwd
)"

echo "Current directory: " "$SCRIPT_DIR"

WORK_DIR="$SCRIPT_DIR"/build

if [ ! -e "$WORK_DIR" ]; then
  mkdir -p "$WORK_DIR"
  echo "Directory created: " "$WORK_DIR"
fi

cd "$WORK_DIR" || exit 1

LUNACHAT_DIR="$WORK_DIR"/LunaChat

if [ -e "$LUNACHAT_DIR" ]; then
  rm -rf LunaChat
  echo "Directory deleted: " "$LUNACHAT_DIR"
fi

git clone https://github.com/ucchyocean/LunaChat