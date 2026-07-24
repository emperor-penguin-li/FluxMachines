# FluxMachines

一个面向生存流程的 Minecraft Forge 科技模组，提供矿物加工、能源设备、自动化机器与可穿戴电力装备。

适用版本： **Minecraft 1.20.1 / Forge 47.4.0**

## 内容概览

- **矿物与材料**：加入锡、镍，以及钛、钴、钨、锇、铱等工业材料与配套中间件。
- **基础加工**：粉碎机、线材机、合金炉可用于粉碎、拉丝与合金熔炼；配方可通过 JEI 查看。
- **进阶工业**：谐振粉碎机、高压浸出反应器、电解纯化器、真空等离子炉和量子装配台构成高阶处理链。
- **能源设备**：所有设备使用 Forge Energy（FE）。充电座可为站在其上的玩家背包、盔甲栏、副手及 Curios 饰品栏中的可充电物品供能。
- **加速器**：可通过“加速器连接器”绑定同一维度内的方块实体，并在 GUI 中调整工作倍速。倍速越高，耗电越高。
- **刷怪抑制器**：建成多方块结构并供能后，可在配置的范围内抑制怪物生成。
- **电力戒指**：
    - 电力飞行戒指：提供飞行、冲刺加速与坠落保护。
    - 电力维生戒指：提供急救、营养补充与力场保护。

## 安装

1. 安装 Minecraft **1.20.1** 与 Forge **47.4.0** 或兼容的 47.x 版本。
2. 将构建出的 `FluxMachines-1.0.0.jar` 放入游戏实例的 `mods` 文件夹。
3. 同时安装必需前置：
    - [Curios API](https://modrinth.com/mod/curios)
4. 推荐安装 [Just Enough Items (JEI)](https://modrinth.com/mod/jei)，以浏览配方。

> 模组需要 FE 能源来源。开发环境中包含 Mekanism 作为运行时依赖，正式整合包可按需选择任意兼容 Forge Energy 的供能模组。

## 配置

服务端首次启动后会生成 `config/fluxmachines.toml`。

修改服务端配置后，请按服务器/整合包的常规流程重启或重新加载配置。

## 本地开发与构建

环境要求：JDK 17，以及 PowerShell 7。

```powershell
# 构建项目
.\gradlew.bat build

# 启动开发客户端
.\gradlew.bat runClient
```

构建产物位于 `build/libs/`。

## 项目结构

```text
src/main/java/              模组逻辑
src/main/resources/         资源、配方、语言文件、战利品表与世界生成
scripts/generate-textures/  构建阶段执行的纹理生成脚本
```
