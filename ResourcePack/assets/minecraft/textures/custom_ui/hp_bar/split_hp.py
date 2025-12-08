import os
import json
from PIL import Image

# ================= 🔧 設定區域 (請依需求修改) =================

# 1. 檔案路徑設定
INPUT_IMAGE = "exp_progress.png"       # 你的滿血原圖
OUTPUT_FOLDER = "generated"    # 輸出圖片的資料夾
JSON_FILENAME = "providers_output.json" # 輸出的 JSON 文字檔

# 2. 材質包路徑設定 (這會寫在 JSON 裡的 "file" 欄位)
RP_TEXTURE_PATH = "custom/hp/"
FILE_PREFIX = "exp_progress"          # 圖片檔名前綴

# 3. Unicode 起始點設定 (必須是私有使用區域 E000 - F8FF)
START_CHAR_HEX = 0xE100 

# 4. 圖片偏移設定 (依據你的圖片設計調整)
ASCENT = 0
HEIGHT = 15

# 5. 【關鍵】負空間設定 (必須與你材質包的 font/default.json 定義一致)
NEGATIVE_SPACE_CHAR = "\uF801"
NEGATIVE_SPACE_VALUE = 100 # 以正值表示單個字符的位移大小

# ==========================================================

def get_unicode_str(code_int):
    """將整數轉換為 Minecraft JSON 格式的 Unicode 字串 (如 \uE100)"""
    return f"\\u{code_int:04X}"

def generate_pack():
    if not os.path.exists(INPUT_IMAGE):
        print(f"❌ 錯誤: 找不到 '{INPUT_IMAGE}'")
        return

    if not os.path.exists(OUTPUT_FOLDER):
        os.makedirs(OUTPUT_FOLDER)

    try:
        original_img = Image.open(INPUT_IMAGE).convert("RGBA")
        width, height = original_img.size
        print(f"📦 原始圖片尺寸: {width}x{height}")
    except Exception as e:
        print(f"❌ 無法開啟圖片: {e}")
        return

    providers_list = []
    
    print("⏳ 正在生成圖片與 JSON...")

    # --- 步驟 1: 迴圈生成 0% 到 100% 的圖片與 JSON ---
    for percent in range(101):
        # A. 圖片處理 (保持畫布大小)
        new_img = Image.new("RGBA", (width, height), (0, 0, 0, 0))
        if percent > 0:
            visible_width = int(width * (percent / 100))
            if visible_width > 0:
                cropped_part = original_img.crop((0, 0, visible_width, height))
                new_img.paste(cropped_part, (0, 0))
        
        filename = f"{FILE_PREFIX}{percent}.png"
        new_img.save(os.path.join(OUTPUT_FOLDER, filename))

        # B. JSON 資料生成
        current_char_code = START_CHAR_HEX + percent
        char_str = get_unicode_str(current_char_code)
        
        provider_entry = {
            "type": "bitmap",
            "file": f"{RP_TEXTURE_PATH}{filename}",
            "ascent": ASCENT,
            "height": HEIGHT,
            "chars": [char_str]
        }
        providers_list.append(provider_entry)

    # --- 步驟 2: 加入你要求的負空間 (Space) 設定 ---
    space_provider = {
        "type": "space",
        "advances": {
            # 這是你要求的精細調整空間
            "\uF801": -100,
            "\uF802": -10,
            "\uF803": -1
        }
    }
    providers_list.append(space_provider)

    # --- 步驟 3: 寫入 JSON 檔案 ---
    final_json_str = json.dumps({"providers": providers_list}, indent=2)
    final_json_str = final_json_str.replace("\\\\u", "\\u")

    with open(JSON_FILENAME, "w", encoding="utf-8") as f:
        f.write(final_json_str)

    print("✅ 完成！")
    print(f"1. 圖片已生成於 '{OUTPUT_FOLDER}/'")
    print(f"2. JSON 設定已生成於 '{JSON_FILENAME}'")
    
    # --- 步驟 4: 輸出左上角位移的計算結果 ---
    
    # 估計將內容推到左上角所需的位移量 (-450px)
    required_negative_space = 5 
    
    # 計算所需的字符組合
    negative_shift_string = NEGATIVE_SPACE_CHAR * required_negative_space
    
    print("\n--- ❗ Java 程式碼所需位移計算結果 ---")
    print(f"單個字符位移: -{NEGATIVE_SPACE_VALUE} 像素 ({NEGATIVE_SPACE_CHAR})")
    print(f"目標位移估計: 至少需要 -450 像素。")
    print(f"所需字符數量: {required_negative_space} 個 (總位移: -{required_negative_space * NEGATIVE_SPACE_VALUE} 像素)")
    print("---------------------------------")
    print("請在 Java BossBar Title 中使用以下字串實現左上角位移：")
    print(f"String NEGATIVE_SHIFT_STRING = \"{negative_shift_string}\";")
    print("---------------------------------")

if __name__ == "__main__":
    generate_pack()