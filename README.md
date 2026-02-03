# TaCZ Reloaded

---

## ✨ 功能特性

### 🎯 多种抛壳模式

| 模式 | 说明 | 适用枪械 |
| --- | --- | --- |
| **ON_FIRE** | 开火后延迟抛壳，切枪可打断 | 栓动步枪（AWP、Kar98k） |
| **ON_RELOAD** | 开火累计弹壳，换弹时一次性抛出 | 双管喷子、泵动霰弹枪 |
| **HYBRID** | 正常开火立即抛壳，最后一发换弹时抛 | 大部分自动武器（AK、M4） |
| **NONE** | 不抛壳 | 火箭筒、特殊武器 |

### 🔄 真实的打断机制

- 开火后切枪会打断延迟中的抛壳
- 切回来时继续拉栓抛壳
- 所有延迟均可配置

### 📦 弹壳物品

- 支持多种口径：9mm、.45 ACP、5.56×45、7.62×39、7.62×51 等
- 弹壳可堆叠（需配合 Sensible Stackables 模组突破 99 限制）

---

## 📥 安装

### 依赖

- Minecraft 1.21.1
- NeoForge 21.1.x
- TaCZ 1.1.7+
- Create 6.0+
- [Sensible Stackables](https://www.curseforge.com/minecraft/mc-mods/sensible-stackables) - 解除堆叠上限

### 安装步骤

1. 下载 `taczreloaded-x.x.x.jar`
2. 放入 `.minecraft/mods` 文件夹
3. 启动游戏

---

## ⚙️ 配置

### 枪械映射配置

在 `data/taczreloaded/mapping/` 目录下创建 JSON 文件：

```json
{
  "type": "on_fire",
  "shell": {
    "id": "taczreloaded:shell",
    "components": {
      "taczreloaded:shell_id": "taczreloaded:762x51"
    }
  },
  "delay": 1.0
}
```

### 参数说明

| 参数 | 类型 | 说明 |
| --- | --- | --- |
| `type` | string | 抛壳模式：`on_fire` / `on_reload` / `hybrid` / `none` |
| `shell` | ItemStack | 弹壳物品 |
| `delay` | float | 抛壳延迟（秒） |
| `fireDelay` | float | 开火抛壳延迟（hybrid 模式） |
| `reloadDelay` | float | 换弹抛壳延迟（hybrid 模式） |

---

## 🛠️ 开发者 API

### DataComponent 集成

```java
// 从枪械 ItemStack 获取抛壳行为
EjectionBehavior behavior = gunStack.get(ModDataComponents.EJECTION_BEHAVIOR.get());

// 检查接口实现
if (behavior instanceof IOnFireTrigger trigger) {
    trigger.onFire(shooter, gunStack, currentAmmo);
}
```

### 自定义抛壳类

```java
public class MyCustomEject extends EjectionBehavior 
    implements IOnFireTrigger, IOnReloadTrigger {
    
    // 实现抽象方法和接口...
}
```

---

## 📋 支持的弹壳类型

- 9mm、.45 ACP、5.7×28mm
- 5.56×45mm、5.45×39mm、5.8×42mm
- 7.62×39mm、7.62×51mm、.338 Lapua
- .50 AE、.50 BMG、.45-70
- 12 Gauge

---

## 📜 许可证

MIT License

---

## 🤝 贡献

欢迎提交 Issue 和 Pull Request！

---

**Made with ❤️ for TaCZ community**
