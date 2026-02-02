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
                has_shell_eject = False
                reload_time = None
                
                # 检查拉栓时间
                if bolt_type == "manual_action":
                    # 先检查 bolt_action_time
                    bolt_time = data.get("bolt_action_time", None)
                    
                    # 如果没有，检查 script_param 中的 bolt_time
                    if bolt_time is None:
                        script_param = data.get("script_param", {})
                        if "bolt_time" in script_param:
                            bolt_time = script_param.get("bolt_time")
                
                # 检查换弹动作时间 - 这指示弹夹换弹，即会抛壳
                reload = data.get("reload", {})
                script_param = data.get("script_param", {})
                
                if script_param:
                    has_shell_eject = True
                    # 从 script_param 获取换弹完成时间
                    # 对于特殊的枪械脚本，获取最后一个参数
                    if "reload_cooldown" in script_param:
                        reload_time = script_param.get("reload_cooldown")
                    elif "ending" in script_param:
                        # 这是管式弹夹装弹，获取 ending 时间
                        reload_time = script_param.get("ending")
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
print("TACZ 默认武器库 - 非全自动武器特殊数据详表".center(90))
print("="*90)

# 分类统计
with_bolt = [r for r in results if r["bolt_type"] == "manual_action"]
with_eject = [r for r in results if r["has_shell_eject"]]

print(f"\n【统计信息】")
print(f"  • 总非全自动武器数: {len(results)}")
print(f"  • 需要拉栓武器数: {len(with_bolt)}")
print(f"  • 换弹时会抛壳武器数: {len(with_eject)}")

# 详细表格
print("\n" + "="*90)
print("【详细数据表】")
print("="*90)
print(f"{'武器':<25} {'拉栓类型':<15} {'拉栓时间':<12} {'抛壳':<5} {'换弹完成时间':<15}")
print("-"*90)

for r in sorted(results, key=lambda x: x['name']):
    bolt_time_str = f"{r['bolt_time']}s" if r['bolt_time'] is not None else "-"
    eject_str = "✓" if r['has_shell_eject'] else "-"
    reload_time_str = f"{r['reload_time']}s" if r['reload_time'] is not None else "-"
    
    print(f"{r['name']:<25} {r['bolt_type']:<15} {bolt_time_str:<12} {eject_str:<5} {reload_time_str:<15}")

# 特殊统计：需要拉栓的武器
print("\n" + "="*90)
print("【需要拉栓的武器】(需要在射击前重新装膛的手动上膛武器)")
print("="*90)

bolt_guns = sorted([r for r in results if r["bolt_type"] == "manual_action"], key=lambda x: x['name'])
if bolt_guns:
    for r in bolt_guns:
        bolt_str = f"{r['bolt_time']}秒" if r['bolt_time'] is not None else "未明确定义"
        reload_str = f"{r['reload_time']}秒" if r['reload_time'] else "未明确定义"
        print(f"\n  ► {r['name']:<20} 拉栓时间: {bolt_str:<15} | 换弹完成: {reload_str}")
else:
    print("无")

print("\n" + "="*90)
print("【弹夹换弹详细统计】")
print("="*90)
print("所有非全自动武器都支持弹夹换弹(会抛壳):")
print()

for i, r in enumerate(sorted(results, key=lambda x: x['name']), 1):
    reload_str = f"{r['reload_time']}秒" if r['reload_time'] else "未明确定义"
    bolt_str = f"拉栓: {r['bolt_time']}s | " if r['bolt_time'] is not None else ""
    print(f"  {i:2}. {r['name']:<20} {bolt_str}换弹完成: {reload_str}")

print("\n" + "="*90)
