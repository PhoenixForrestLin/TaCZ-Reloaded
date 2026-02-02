import json5
import os

gun_dir = r"c:\Users\linyu\Documents\mcmod\tacz\run\tacz\tacz_default_gun\data\tacz\data\guns"

# 读取所有文件并分析
results = []

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
                
                # 提取数据
                bolt_type = data.get("bolt", "")
                bolt_time = None
                reload_mode = "magazine"
                reload_time = None
                reload_details = {}
                
                # 检查拉栓时间
                if bolt_type == "manual_action":
                    bolt_time = data.get("bolt_action_time", None)
                    script_param = data.get("script_param", {})
                    if bolt_time is None and "bolt_time" in script_param:
                        bolt_time = script_param.get("bolt_time")
                
                # 检查换弹动作时间
                reload = data.get("reload", {})
                script_param = data.get("script_param", {})
                
                if script_param:
                    reload_details = script_param
                    # 判断是管式弹夹还是盒式弹夹
                    if "ending" in script_param and "loop" in script_param:
                        # 这是管式弹夹 (tube magazine)
                        reload_mode = "tube_magazine"
                        reload_time = script_param.get("ending")
                    elif "reload_cooldown" in script_param:
                        reload_time = script_param.get("reload_cooldown")
                    elif "empty_cooldown" in script_param:
                        reload_time = script_param.get("empty_cooldown")
                elif reload.get("type") == "magazine":
                    cooldown = reload.get("cooldown", {})
                    reload_details = {"cooldown": cooldown, "feed": reload.get("feed", {})}
                    if cooldown:
                        reload_time = cooldown.get("empty")
                
                results.append({
                    "name": gun_name,
                    "bolt_type": bolt_type,
                    "bolt_time": bolt_time,
                    "reload_mode": reload_mode,
                    "reload_time": reload_time,
                    "reload_details": reload_details
                })
        except Exception as e:
            pass

# 输出报告
print("="*100)
print("TACZ 默认武器库 - 非全自动武器特殊数据完整报告".center(100))
print("="*100)

# 统计信息
with_bolt = [r for r in results if r["bolt_type"] == "manual_action"]
tube_mag = [r for r in results if r["reload_mode"] == "tube_magazine"]
box_mag = [r for r in results if r["reload_mode"] == "magazine"]

print(f"\n【统计概览】")
print(f"  • 非全自动武器总数: {len(results)} 把")
print(f"  • 手动上膛(manual_action)武器: {len(with_bolt)} 把")
print(f"  • 管式弹夹(tube magazine)装弹: {len(tube_mag)} 把")
print(f"  • 盒式弹夹(box magazine)装弹: {len(box_mag)} 把")

# 详细表格
print("\n" + "="*100)
print("【全武器特殊数据对比表】")
print("="*100)
print(f"{'武器':<20} {'拉栓类型':<16} {'拉栓时间':<10} {'弹夹类型':<12} {'换弹完成':<12}")
print("-"*100)

for r in sorted(results, key=lambda x: x['name']):
    bolt_str = f"{r['bolt_type']:<16}"
    bolt_time_str = f"{r['bolt_time']}s" if r['bolt_time'] is not None else "-"
    reload_type_str = "tube mag" if r["reload_mode"] == "tube_magazine" else "box mag"
    reload_time_str = f"{r['reload_time']}s" if r['reload_time'] is not None else "-"
    
    print(f"{r['name']:<20} {bolt_str:<16} {bolt_time_str:<10} {reload_type_str:<12} {reload_time_str:<12}")

# 需要拉栓的武器详细信息
print("\n" + "="*100)
print("【需要拉栓的武器(手动上膛)】")
print("="*100)

bolt_guns = sorted([r for r in results if r["bolt_type"] == "manual_action"], key=lambda x: x['name'])
for i, r in enumerate(bolt_guns, 1):
    bolt_info = f"{r['bolt_time']}秒" if r['bolt_time'] is not None else "未明确定义"
    reload_info = f"{r['reload_time']}秒" if r['reload_time'] else "未明确定义"
    reload_type = "管式弹夹" if r["reload_mode"] == "tube_magazine" else "盒式弹夹"
    
    print(f"\n  {i}. {r['name'].upper()}")
    print(f"     ├─ 拉栓时间: {bolt_info}")
    print(f"     ├─ 装弹方式: {reload_type}")
    print(f"     └─ 换弹完成时间: {reload_info}")

# 弹夹类型统计
print("\n" + "="*100)
print("【管式弹夹装弹武器(Tube Magazine)】- 单发装弹")
print("="*100)

tube_guns = sorted([r for r in results if r["reload_mode"] == "tube_magazine"], key=lambda x: x['name'])
if tube_guns:
    for i, r in enumerate(tube_guns, 1):
        bolt_info = ""
        if r['bolt_time'] is not None:
            bolt_info = f" | 拉栓: {r['bolt_time']}s"
        print(f"  {i}. {r['name'].upper():<20} 单发装弹完成: {r['reload_time']}s{bolt_info}")
else:
    print("无")

# 盒式弹夹统计
print("\n" + "="*100)
print("【盒式弹夹装弹武器(Box Magazine)】")
print("="*100)

box_guns = sorted([r for r in results if r["reload_mode"] == "magazine"], key=lambda x: x['name'])
if box_guns:
    for i, r in enumerate(box_guns, 1):
        print(f"  {i:2}. {r['name'].upper():<20} 换弹完成: {r['reload_time']}s")
else:
    print("无")

print("\n" + "="*100)
