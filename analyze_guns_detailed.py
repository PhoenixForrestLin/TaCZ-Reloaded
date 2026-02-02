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
                gun_name = filename.replace("_data.json", "").upper()
                
                # 提取数据
                bolt_type = data.get("bolt", "")
                bolt_time = None
                has_shell_eject = False
                reload_time = None
                
                # 检查拉栓时间
                if bolt_type == "manual_action":
                    bolt_time = data.get("bolt_action_time", None)
                
                # 检查换弹动作时间 - 这指示弹夹换弹，即会抛壳
                reload = data.get("reload", {})
                script_param = data.get("script_param", {})
                
                if script_param:
                    has_shell_eject = True
                    # 从 script_param 获取换弹完成时间
                    if "reload_cooldown" in script_param:
                        reload_time = script_param.get("reload_cooldown")
                    elif "empty_cooldown" in script_param:
                        reload_time = script_param.get("empty_cooldown")
                elif reload.get("type") == "magazine":
                    has_shell_eject = True
                    cooldown = reload.get("cooldown", {})
                    if cooldown:
                        # 取 empty 的时间作为换弹完成时间
                        reload_time = cooldown.get("empty")
                
                results.append({
                    "name": gun_name,
                    "bolt_type": bolt_type,
                    "bolt_time": bolt_time,
                    "has_shell_eject": has_shell_eject,
                    "reload_time": reload_time
                })
        except Exception as e:
            pass

# 整理结果
print("="*90)
print("TACZ 默认武器库 - 非全自动武器特殊数据统计".center(90))
print("="*90)

# 分类统计
with_bolt = [r for r in results if r["bolt_type"] == "manual_action"]
with_eject = [r for r in results if r["has_shell_eject"]]

print(f"\n【总非全自动武器数】: {len(results)}")
print(f"【需要拉栓武器数】: {len(with_bolt)}")
print(f"【换弹时会抛壳武器数】: {len(with_eject)}")

# 详细表格
print("\n" + "="*90)
print("详细数据表")
print("="*90)
print(f"{'武器名称':<20} {'拉栓类型':<15} {'拉栓时间':<12} {'抛壳':<8} {'换弹完成时间':<15}")
print("-"*90)

for r in results:
    bolt_time_str = str(r["bolt_time"]) if r["bolt_time"] is not None else "-"
    eject_str = "是" if r["has_shell_eject"] else "否"
    reload_time_str = str(r["reload_time"]) if r["reload_time"] is not None else "-"
    
    print(f"{r['name']:<20} {r['bolt_type']:<15} {bolt_time_str:<12} {eject_str:<8} {reload_time_str:<15}")

# 特殊统计：需要拉栓的武器
print("\n" + "="*90)
print("【需要拉栓的武器】(需要在射击前重新装膛)")
print("="*90)

bolt_guns = [r for r in results if r["bolt_type"] == "manual_action"]
if bolt_guns:
    for r in bolt_guns:
        print(f"\n{r['name']}")
        print(f"  ├─ 拉栓时间: {r['bolt_time'] if r['bolt_time'] is not None else '未定义'} 秒")
        print(f"  └─ 换弹时抛壳: {'是' if r['has_shell_eject'] else '否'}")
        if r['has_shell_eject'] and r['reload_time']:
            print(f"     └─ 换弹动作完成: {r['reload_time']} 秒")
else:
    print("无")

# 特殊统计：非拉栓但会抛壳的武器
print("\n" + "="*90)
print("【非拉栓但换弹时会抛壳的武器】(闭膛待击)")
print("="*90)

eject_no_bolt = [r for r in results if r["bolt_type"] != "manual_action" and r["has_shell_eject"]]
if eject_no_bolt:
    for r in eject_no_bolt:
        print(f"\n{r['name']}")
        print(f"  ├─ 拉栓类型: {r['bolt_type']}")
        print(f"  └─ 换弹动作完成: {r['reload_time'] if r['reload_time'] else '未定义'} 秒")
else:
    print("无")

print("\n" + "="*90)
