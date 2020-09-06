SCRIPT_DIR="$(
  cd "$(dirname "$0")" || exit
  pwd
)"

echo "Current directory: " "$SCRIPT_DIR"

cd "$SCRIPT_DIR"/work/LunaChat || exit

PATCH_FILE="$SCRIPT_DIR"/patches/"$(date "+%s")".patch

git diff > "$PATCH_FILE"
echo "Patch created: " "$PATCH_FILE"