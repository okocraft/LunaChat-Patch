SCRIPT_DIR="$(
  cd "$(dirname "$0")" || exit
  pwd
)"

LOGS_DIR="$SCRIPT_DIR"/logs

if [ ! -e "$LOGS_DIR" ]; then
  mkdir -p "$LOGS_DIR"
  echo "Directory created: " "$LOGS_DIR"
fi

exec 1> >(tee -a "$SCRIPT_DIR"/logs/createPatch-"$(date "+%s")".log)
echo "Current directory: " "$SCRIPT_DIR"

cd "$SCRIPT_DIR"/work/LunaChat || exit

PATCH_FILE="$SCRIPT_DIR"/patches/"$(date "+%s")".patch

git diff > "$PATCH_FILE"
echo "Patch created: " "$PATCH_FILE"
