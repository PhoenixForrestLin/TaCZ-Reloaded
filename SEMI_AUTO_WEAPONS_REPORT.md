# TACZ 默认武器库 - 非全自动武器特殊数据报告

**报告日期**: 2026年2月2日  
**数据源**: `run/tacz/tacz_default_gun/data/tacz/data/guns/`

---

## 统计概览

| 指标 | 数量 |
|-----|------|
| 非全自动武器总数 | 17 把 |
| 手动上膛(manual_action)武器 | 5 把 |
| 管式弹夹(tube magazine)装弹 | 2 把 |
| 盒式弹夹(box magazine)装弹 | 15 把 |

---

## 全武器特殊数据对比表

| 武器 | 拉栓类型 | 拉栓时间 | 弹夹类型 | 换弹完成时间 |
|-----|--------|---------|---------|------------|
| AI_AWP | manual_action | 0.9s | box mag | 3.25s |
| DB_LONG | open_bolt | - | box mag | 2.3s |
| DEAGLE | closed_bolt | - | box mag | 1.64s |
| DEAGLE_GOLDEN | closed_bolt | - | box mag | 1.64s |
| GLOCK_17 | closed_bolt | - | box mag | 1.88s |
| M1014 | closed_bolt | - | tube mag | 0.17s |
| M107 | manual_action | - | box mag | 5.25s |
| M1911 | closed_bolt | - | box mag | 2.48s |
| M320 | open_bolt | - | box mag | 3s |
| M700 | manual_action | 0.85s | box mag | 3.45s |
| M870 | manual_action | 0.55s | tube mag | 0.17s |
| M95 | manual_action | 1.43s | box mag | 5.23s |
| P320 | closed_bolt | - | box mag | 2.48s |
| RPG7 | open_bolt | - | box mag | 3.2s |
| SKS_TACTICAL | closed_bolt | - | box mag | 2.19s |
| SPRINGFIELD1873 | open_bolt | - | box mag | 2.5s |
| TIMELESS50 | closed_bolt | - | box mag | 1.64s |

---

## 需要拉栓的武器 (手动上膛)

这些武器需要在射击前重新装膛。

### 1. **AI_AWP** - 精准步枪
- **拉栓时间**: 0.9秒
- **装弹方式**: 盒式弹夹
- **换弹完成时间**: 3.25秒
- **用途**: 高精度狙击

### 2. **M107** - 重型狙击步枪
- **拉栓时间**: 未明确定义
- **装弹方式**: 盒式弹夹
- **换弹完成时间**: 5.25秒 (最长)
- **用途**: 超远距离火力

### 3. **M700** - 经典狙击步枪
- **拉栓时间**: 0.85秒
- **装弹方式**: 盒式弹夹
- **换弹完成时间**: 3.45秒
- **用途**: 标准狙击

### 4. **M870** - 战术霰弹枪
- **拉栓时间**: 0.55秒 (最短)
- **装弹方式**: 管式弹夹 (单发装弹)
- **换弹完成时间**: 0.17秒 (单发完成)
- **用途**: 近距离火力

### 5. **M95** - 超远程狙击枪
- **拉栓时间**: 1.43秒 (最长)
- **装弹方式**: 盒式弹夹
- **换弹完成时间**: 5.23秒
- **用途**: 超远程火力

---

## 管式弹夹装弹武器 (Tube Magazine) - 单发装弹

这些武器使用管式弹夹，需要单发装弹。

| 武器 | 单发装弹完成 | 拉栓 |
|-----|-----------|------|
| **M1014** | 0.17s | 否 |
| **M870** | 0.17s | 是 (0.55s) |

---

## 盒式弹夹装弹武器 (Box Magazine) - 整体换弹

这些武器使用标准盒式弹夹，支持整体快速换弹。

| # | 武器 | 换弹完成时间 |
|---|-----|-----------|
| 1 | AI_AWP | 3.25s |
| 2 | DB_LONG | 2.3s |
| 3 | DEAGLE | 1.64s |
| 4 | DEAGLE_GOLDEN | 1.64s |
| 5 | GLOCK_17 | 1.88s |
| 6 | M107 | 5.25s |
| 7 | M1911 | 2.48s |
| 8 | M320 | 3s |
| 9 | M700 | 3.45s |
| 10 | M95 | 5.23s |
| 11 | P320 | 2.48s |
| 12 | RPG7 | 3.2s |
| 13 | SKS_TACTICAL | 2.19s |
| 14 | SPRINGFIELD1873 | 2.5s |
| 15 | TIMELESS50 | 1.64s |

---

## 换弹性能排序

### 最快换弹 (≤ 1.88s)
1. **DEAGLE / DEAGLE_GOLDEN** - 1.64s
2. **TIMELESS50** - 1.64s
3. **GLOCK_17** - 1.88s

### 最慢换弹 (≥ 5.0s)
1. **M107** - 5.25s
2. **M95** - 5.23s

### 中等换弹 (2-3s)
- DB_LONG, M1911, M320, M700, RPG7, SKS_TACTICAL, SPRINGFIELD1873

---

## 拉栓性能排序

### 最快拉栓
1. **M870** - 0.55s
2. **M700** - 0.85s
3. **AI_AWP** - 0.9s

### 最慢拉栓
1. **M95** - 1.43s

**注**: M107 的拉栓时间未在配置中明确定义。

---

## 数据说明

- **拉栓类型**:
  - `manual_action`: 手动上膛，射击前需要拉栓
  - `closed_bolt`: 闭膛待击，枪机已准备
  - `open_bolt`: 开膛待机

- **装弹方式**:
  - `box mag`: 盒式弹夹，可整体快速换弹
  - `tube mag`: 管式弹夹，需要单发逐发装弹

- **时间单位**: 秒 (s)

---

*所有数据直接基于 `run/tacz/tacz_default_gun/` 目录中的武器配置文件。*
