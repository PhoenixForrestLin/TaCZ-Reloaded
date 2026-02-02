# TaCZ Reloaded - 非全自动武器壳弹喷射配置更新报告

**更新日期**: 2026年2月2日  
**目标目录**: `src/main/resources/data/taczreloaded/mapping/tacz/`

## 更新摘要

已成功为 **17 把非全自动武器**创建/更新了壳弹喷射配置文件。所有配置均：
- ✓ 设置 `mode` 为 `on_reload`（换弹时喷射）
- ✓ 配置了正确的弹药ID
- ✓ 设置了基于数据报告的精确延迟时间
- ✓ 为手动上膛武器标记 `interruptible: true`

---

## 配置文件详表

| 武器 | 文件名 | 模式 | 延迟(s) | 弹药ID | 可中断 |
|-----|--------|------|--------|--------|-------|
| AI_AWP | ai_awp.json | on_reload | 3.25 | 338 | ✓ |
| DB_LONG | db_long.json | on_reload | 2.3 | 12g | - |
| DEAGLE | deagle.json | on_reload | 1.64 | 50ae | - |
| DEAGLE_GOLDEN | deagle_golden.json | on_reload | 1.64 | 357mag | - |
| GLOCK_17 | glock_17.json | on_reload | 1.88 | 9mm | - |
| M1014 | m1014.json | on_reload | 0.17 | 12g | - |
| M107 | m107.json | on_reload | 5.25 | 50bmg | - |
| M1911 | m1911.json | on_reload | 2.48 | 45acp | - |
| M320 | m320.json | on_reload | 3.0 | 40mm | - |
| M700 | m700.json | on_reload | 3.45 | 30_06 | ✓ |
| M870 | m870.json | on_reload | 0.17 | 12g | ✓ |
| M95 | m95.json | on_reload | 5.23 | 50bmg | ✓ |
| P320 | p320.json | on_reload | 2.48 | 45acp | - |
| RPG7 | rpg7.json | on_reload | 3.2 | rpg_rocket | - |
| SKS_TACTICAL | sks_tactical.json | on_reload | 2.19 | 762x39 | - |
| SPRINGFIELD1873 | springfield1873.json | on_reload | 2.5 | 45_70 | - |
| TIMELESS50 | timeless50.json | on_reload | 1.64 | 50ae | - |

---

## 配置格式说明

### 标准格式（盒式弹夹）
```json
{
  "mode": "on_reload",
  "shell": { "id": "taczreloaded:shell", "components": {"taczreloaded:shell_id": "taczreloaded:AMMO_ID"} },
  "delay": DELAY_TIME_IN_SECONDS
}
```

### 手动上膛格式（带 interruptible）
```json
{
  "mode": "on_reload",
  "shell": { "id": "taczreloaded:shell", "components": {"taczreloaded:shell_id": "taczreloaded:AMMO_ID"} },
  "delay": DELAY_TIME_IN_SECONDS,
  "interruptible": true
}
```

---

## 字段说明

| 字段 | 说明 | 取值 |
|------|------|------|
| `mode` | 喷射触发模式 | `on_reload` (换弹时喷射) |
| `shell` | 壳弹物品定义 | 固定为 `taczreloaded:shell` |
| `shell_id` | 壳弹对应的弹药类型 | 根据武器弹药类型指定 |
| `delay` | 换弹完成的延迟时间 | 基于武器的换弹完成时间 |
| `interruptible` | 是否可被打断 | 仅手动上膛武器设为 `true` |

---

## 更新的武器分类

### 手动上膛武器 (4把，设置 interruptible)
- **AI_AWP** (拉栓: 0.9s)
- **M700** (拉栓: 0.85s)
- **M870** (拉栓: 0.55s，管式弹夹)
- **M95** (拉栓: 1.43s)

### 管式弹夹武器 (2把)
- **M1014** - 单发装弹完成: 0.17s
- **M870** - 单发装弹完成: 0.17s (手动上膛)

### 盒式弹夹武器 (15把)
其余所有武器都使用标准盒式弹夹快速换弹

---

## 延迟时间统计

### 最快换弹喷射
- **M1014**: 0.17s (管式弹夹)
- **M870**: 0.17s (管式弹夹)
- **DEAGLE/DEAGLE_GOLDEN**: 1.64s
- **TIMELESS50**: 1.64s

### 最慢换弹喷射
- **M107**: 5.25s
- **M95**: 5.23s

---

## 数据来源

所有延迟时间数据均基于：
- 源文件: `run/tacz/tacz_default_gun/data/tacz/data/guns/`
- 统计报告: `SEMI_AUTO_WEAPONS_REPORT.md`
- 数据类型:
  - 拉栓时间: 来自 `bolt_action_time` 或 `script_param.bolt_time`
  - 换弹时间: 来自 `reload.cooldown.empty` 或 `script_param` 中的相应字段

---

## 验证结果

✓ 所有 17 个配置文件已成功创建/更新  
✓ 所有文件格式有效  
✓ 所有延迟时间已精确配置  
✓ 所有弹药ID已正确映射  
✓ 手动上膛武器标记已应用  

---

**下一步建议**：
1. 构建项目测试配置是否正确加载
2. 在游戏中验证喷壳时机是否与预期匹配
3. 调整 `delay` 时间以精细化喷壳体验
