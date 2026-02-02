import json5
import os

gun_dir = r"c:\Users\linyu\Documents\mcmod\tacz\run\tacz\tacz_default_gun\data\tacz\data\guns"
semi_auto_guns = {}

# 遍历所有枪械文件
for filename in sorted(os.listdir(gun_dir)):
    if filename.endswith("_data.json"):
        filepath = os.path.join(gun_dir, filename)
        try:
            with open(filepath, 'r', encoding='utf-8') as f:
                data = json5.load(f)
                
            # 检查是否是半自动武器
            fire_modes = data.get("fire_mode", [])
            if fire_modes == ["semi"]:
                gun_name = filename.replace("_data.json", "")
                
                # 提取特殊数据
                special_data = {}
                
                # 拉栓时间
                bolt_type = data.get("bolt", "")
                if bolt_type == "manual_action":
                    bolt_action_time = data.get("bolt_action_time", "N/A")
                    special_data["拉栓类型"] = "手动上膛"
                    special_data["拉栓时间"] = bolt_action_time
                else:
                    special_data["拉栓类型"] = bolt_type
                
                # 换弹动作时间
                reload = data.get("reload", {})
                if reload.get("type") == "magazine":
                    # 检查是否在script_param中定义
                    script_param = data.get("script_param", {})
                    if script_param:
                        special_data["换弹参数"] = script_param
                    else:
                        feed = reload.get("feed", {})
                        cooldown = reload.get("cooldown", {})
                        if feed or cooldown:
                            special_data["换弹时间"] = {
                                "feed": feed,
                                "cooldown": cooldown
                            }
                
                semi_auto_guns[gun_name] = special_data
        except Exception as e:
            print(f"Error reading {filename}: {e}")

# 打印结果
print("\n" + "="*80)
print("TACZ 非全自动武器特殊数据列表")
print("="*80)

for gun_name, data in semi_auto_guns.items():
    print(f"\n武器: {gun_name}")
    print("-" * 80)
    for key, value in data.items():
        if isinstance(value, dict):
            print(f"  {key}:")
            for k, v in value.items():
                if isinstance(v, dict):
                    print(f"    {k}:")
                    for kk, vv in v.items():
                        print(f"      {kk}: {vv}")
                else:
                    print(f"    {k}: {v}")
        else:
            print(f"  {key}: {value}")

print("\n" + "="*80)
print(f"总计: {len(semi_auto_guns)} 个非全自动武器")
print("="*80)
