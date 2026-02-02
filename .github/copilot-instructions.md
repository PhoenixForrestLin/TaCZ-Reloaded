# TaCZ Reloaded - AI 编程助手指导文档

## 项目概述

**TaCZ Reloaded** 是一个 NeoForge 1.21.1 模组，目标是增强 TaCZ 模组的功能，包括弹药生产线和壳弹回收系统。该项目采用模块化架构，强调代码的可扩展性和可维护性。

## 项目结构

```
src/main/java/trepapto/taczreloaded/
├── api/
│   └── item/
│       └── EjectionBehavior.java          # 核心抽象类：定义物品喷射行为
├── eject/
│   ├── EjectMode.java                      # 枚举：喷射模式定义
│   ├── mode/
│   │   ├── HybridEject.java               # 混合喷射实现
│   │   ├── OnFireEject.java               # 着火时喷射实现
│   │   └── OnReloadEject.java             # 重新装弹时喷射实现
│   └── DelayedShellManager.java           # 延迟喷射管理器
└── ... (其他模块)
```

## 核心架构

### 1. EjectionBehavior（喷射行为）
- **定义**：抽象基类，定义物品喷射的核心逻辑
- **职责**：
  - 存储被喷射的物品（ItemStack）
  - 定义喷射条件和时机
  - 执行喷射动作
- **主要方法**：`eject(LivingEntity entity)` - 执行喷射
- **实现类**：
  - `OnReloadEject`：在重新装弹时喷射（带延迟参数）
  - `OnFireEject`：在开火时喷射
  - `HybridEject`：混合模式，支持多种触发条件

### 2. EjectMode（喷射模式枚举）
定义可用的喷射模式：
- `NONE`：无喷射
- `ON_RELOAD`：重新装弹时喷射
- `ON_FIRE`：开火时喷射
- `HYBRID`：混合模式

每个模式都包含相应的 Codec 用于序列化/反序列化。

### 3. DelayedShellManager（延迟壳弹管理器）
- **职责**：管理和协调延迟喷射事件
- **集成点**：
  - 监听 Minecraft 游戏事件（通过 `@SubscribeEvent`）
  - 与 `EjectionBehavior` 实现协作
  - 管理喷射时机和条件

## 开发工作流

### 构建项目
```bash
# 使用 Gradle Wrapper（推荐）
./gradlew build          # 构建项目
./gradlew runClient      # 运行客户端
./gradlew runServer      # 运行服务器
```

### 代码风格和约定

#### 命名约定
- **类名**：使用 PascalCase（如 `OnReloadEject`）
- **方法和变量**：使用 camelCase（如 `executeEjection()`）
- **常量**：使用 UPPER_SNAKE_CASE（如 `DEFAULT_DELAY`）
- **枚举常量**：使用 UPPER_SNAKE_CASE（如 `ON_RELOAD`）

#### 代码组织
- 使用 Java 包结构组织代码：按功能分组
- 相关的实现类放在同一个包中（如 `eject.mode`）
- 公共 API 放在 `api` 包下

#### 导入和依赖
```java
// 标准导入顺序
import com.mojang.serialization.*;          // Mojang 序列化库
import net.minecraft.*;                      // Minecraft 核心库
import net.neoforged.*;                      // NeoForge 框架库
import trepapto.taczreloaded.*;             // 本项目代码
```

### 序列化和编解码

项目使用 **Mojang Codec** 系统实现数据序列化：
```java
// 典型的 Codec 定义模式
public static final Codec<YourClass> CODEC = RecordCodecBuilder.create(
    instance -> instance.group(
        // 定义字段
    ).apply(instance, YourClass::new)
);
```

### 事件处理

使用 NeoForge 事件系统：
```java
@SubscribeEvent
public static void onPlayerEvent(PlayerEvent event) {
    // 处理事件
}
```

确保在模块加载时注册事件处理器。

## 主要集成点

### 与 Minecraft 的集成
- **LivingEntity**：代表游戏中的生物实体（包括玩家）
- **ItemStack**：代表物品堆栈
- **Codec 系统**：用于配置和数据持久化

### 与 NeoForge 的集成
- **事件总线（Event Bus）**：用于监听和响应游戏事件
- **模组配置**：通过 `neoforge.mods.toml` 定义

### 配置系统
- **位置**：`run/config/tacz-*.toml` 文件
- **重要配置**：
  - `tacz-client.toml`：客户端特定配置
  - `tacz-server.toml`：服务器特定配置
  - `tacz-common.toml`：通用配置

## 关键文件指南

| 文件路径 | 目的 | 关键类 |
|---------|------|--------|
| `src/main/java/.../api/item/EjectionBehavior.java` | 喷射行为基类 | `EjectionBehavior` |
| `src/main/java/.../eject/EjectMode.java` | 喷射模式定义 | `EjectMode` |
| `src/main/java/.../eject/mode/OnReloadEject.java` | 装弹时喷射 | `OnReloadEject` |
| `src/main/java/.../eject/mode/OnFireEject.java` | 开火时喷射 | `OnFireEject` |
| `src/main/java/.../eject/DelayedShellManager.java` | 喷射事件管理 | `DelayedShellManager` |
| `build.gradle` | 项目构建配置 | - |
| `gradle.properties` | Gradle 属性 | - |

## 常见任务

### 添加新的喷射模式
1. 在 `EjectMode` 枚举中添加新的模式常量
2. 创建新的 `EjectionBehavior` 实现类（如 `NewModeEject`）
3. 在 `eject/mode` 包中实现具体逻辑
4. 在 `DelayedShellManager` 中添加事件处理

### 修改喷射延迟
- 编辑相应的 `*Eject` 类中的 `delay` 字段
- 更新对应的 Codec 定义
- 在配置文件中暴露相关参数

### 调试喷射行为
- 在 `EjectionBehavior` 的 `eject()` 方法中添加日志
- 在 `DelayedShellManager` 的事件处理方法中添加调试输出
- 运行 `./gradlew runClient` 进行实时调试

## 开发建议

### 代码质量
- 为新功能编写单元测试
- 遵循 DRY（不重复代码）原则
- 使用有意义的变量和方法名称
- 添加 JavaDoc 注释，特别是在公共 API 中

### 性能考虑
- 避免在事件处理器中进行重操作
- 使用延迟加载和缓存来优化性能
- 监控事件触发频率

### 兼容性
- 保持与 NeoForge 版本的兼容性
- 在修改 Codec 时，考虑向后兼容性
- 测试在多个 Minecraft 版本上的兼容性

## 文件和资源

### 资源位置
- **资源文件**：`src/main/resources/assets/taczreloaded/`
- **数据文件**：`src/main/resources/data/taczreloaded/`
- **配置文件**：`run/config/tacz-*.toml`

### 配置合并策略
- 现有配置优先保留
- 新增配置会被合并
- 删除配置需谨慎处理

## 补充资源

- **README**：查看项目根目录的 `README.md` 了解项目概况
- **Gradle 配置**：`build.gradle` 包含依赖和构建配置
- **NeoForge 文档**：https://neoforged.net/
- **Minecraft Mod 开发**：参考官方模组开发指南

---

**最后更新**：2026年1月31日

