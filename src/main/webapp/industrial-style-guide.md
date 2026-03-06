# 工业极简主义风格设计规范

## 概述

工业极简主义风格是一种强调专业、安全、高效的界面设计风格，专注于数据直观呈现与功能明确划分。该风格去除所有非必要装饰元素，通过清晰的色彩对比与合理的布局结构，帮助用户快速获取关键信息，特别适合燃气能源行业的监控管理系统。

### 核心设计理念

本风格的设计原则可以归纳为以下四个核心要点：

**扁平化优先**——不依赖阴影、渐变或纹理等视觉装饰，所有元素通过边框与背景色对比来区分层级。这种设计不仅加载性能更优，还能确保界面在不同分辨率与显示设备上保持一致的专业外观。

**数据驱动**——将关键数据以等宽字体高亮显示，配合直观的进度条与状态徽章，让操作人员一眼即可掌握设备运行状态。数值不再是枯燥的数字，而是具有明确语义的状态指示。

**高对比度**——深色主背景搭配明亮的数据强调色，确保在各种光线条件下都能清晰辨识。同时，通过标准化的色彩体系（绿=正常、橙=警告、红=危险），建立清晰的视觉语言。

**功能明确**——每个界面元素都有明确的视觉边界与交互反馈。按钮、开关、输入框都采用统一的圆角与边框规范，用户无需学习即可理解操作方式。

---

## 配色方案

### 色彩体系

工业极简主义风格采用深色系作为主基调，通过明确的色彩对比来区分不同功能区域与状态。以下是完整的色彩变量定义：

#### 背景色彩

```
#111827  主背景色 (bg-primary)
#1F2937  次级背景 (bg-secondary) - 用于卡片、侧边栏
#374151  第三级背景 (bg-tertiary) - 用于悬停状态、表头
#4B5563  边框颜色 (border-color)
```

背景色的选择遵循「层层递进」原则：主背景最深，卡片背景稍浅，交互元素使用最浅的背景色。这种层级关系帮助用户快速理解界面结构。

#### 文字色彩

```
#F9FAFB  主要文字 (text-primary)
#9CA3AF  次要文字 (text-secondary)
#6B7280  弱化文字 (text-muted)
```

文字色彩的对比度经过精心调整，确保在深色背景上具有良好的可读性。主要文字接近白色，次要文字使用灰色调，既保持清晰度又不抢夺视觉焦点。

#### 强调色彩

```
#3B82F6  安全蓝 (accent-blue) - 主强调色，用于主要按钮与激活状态
#06B6D4  电光青 (accent-cyan) - 辅助强调色，用于数据高亮与链接
#10B981  翠绿 (status-normal) - 表示正常运行、成功状态
#F59E0B  琥珀橙 (status-warning) - 表示警告、液位偏低
#EF4444  红色 (status-critical) - 表示危险、压力异常
#6B7280  灰色 (status-offline) - 表示离线、未连接
```

### 状态色彩语义

在燃气监控系统中，状态色彩的标准化至关重要。以下是建议的状态色彩使用规范：

| 状态 | 色彩 | 使用场景 |
|------|------|----------|
| 正常运行 | #10B981 | 设备正常、阀门开启、压力正常 |
| 液位偏低 | #F59E0B | 液位低于30%、需要关注 |
| 压力异常 | #EF4444 | 压力超过安全阈值、紧急告警 |
| 设备离线 | #6B7280 | 设备断开连接、数据未更新 |

### 色彩对比度

所有文字与背景的色彩组合均满足 WCAG AA 标准对比度要求（至少4.5:1），确保在各种视力条件下都能清晰阅读。状态徽章使用带透明度的背景色，既保持色彩辨识度，又避免过于刺眼。

---

## 字体规范

### 字体选择

工业极简主义风格使用两种字体组合：界面文字采用无衬线字体确保清晰易读，数据数值采用等宽字体增强专业感与数据对比性。

#### 界面字体

```
font-family: 'Inter', -apple-system, BlinkMacSystemFont, sans-serif
```

Inter 字体是专门为屏幕阅读优化的无衬线字体，具有出色的可读性与多语言支持能力。系统字体回退列表确保在任何设备上都能获得良好的显示效果。

#### 数据字体

```
font-family: 'JetBrains Mono', 'Roboto Mono', monospace
```

JetBrains Mono 是一种专为代码与数据设计的等宽字体，每个数字与字母占用相同宽度，使纵向数据对比更加直观。这种设计借鉴了工业控制界面的传统，增强监控系统的专业感。

### 字号层级

```
页面标题    24px / 700 weight    页面主标题
区块标题    16px / 600 weight    卡片标题、表格标题
KPI数值    28px / 600 weight    关键数据展示
正文       14px / 400 weight    表格内容、导航
标签/小字   12-13px / 500 weight  状态徽章、辅助信息
```

字号设计遵循「重要性递减」原则：标题使用较大字号与较粗字重，正文使用标准字号与常规字重，辅助信息使用较小字号。这种层级关系帮助用户快速扫描与定位信息。

### 数字显示

对于储罐编号、压力值、时间等数据，建议使用等宽字体并适当调整字重。例如：

```
.tank-id {
    font-family: 'JetBrains Mono', monospace;
    font-weight: 500;
    color: #06B6D4;
}
```

---

## 组件规范

### 卡片组件

卡片是承载信息的主要容器，其设计强调清晰的边界与高效的信息展示。

```css
.kpi-card {
    background-color: #1F2937;
    border: 1px solid #4B5563;
    border-radius: 8px;
    padding: 20px;
    transition: all 0.15s ease;
}

.kpi-card:hover {
    background-color: #374151;
}
```

**设计要点：**

- 边框使用1px实线，明确区分卡片与背景
- 圆角统一为8px，保持视觉一致性
- 悬停时背景色变浅，提供交互反馈
- 无阴影设计，保持扁平化风格

### 状态徽章

状态徽章用于直观展示设备或数据的当前状态，是监控系统界面最重要的视觉元素之一。

```css
.status-badge {
    display: inline-flex;
    align-items: center;
    gap: 6px;
    padding: 4px 10px;
    border-radius: 4px;
    font-size: 12px;
    font-weight: 500;
}

.status-badge.normal {
    background-color: rgba(16, 185, 129, 0.15);
    color: #10B981;
    border: 1px solid rgba(16, 185, 129, 0.3);
}

.status-dot {
    width: 6px;
    height: 6px;
    border-radius: 50%;
    background-color: currentColor;
}
```

**设计要点：**

- 使用半透明背景色，避免过于沉重的视觉负担
- 边框使用更低的透明度，增加层次感
- 状态圆点使用6px尺寸，确保清晰可见
- 圆角保持小巧，与整体硬朗风格一致

### 开关按钮

开关按钮用于控制设备的启停状态，是人机交互的关键界面元素。

```css
.toggle-switch {
    position: relative;
    width: 48px;
    height: 24px;
    background-color: #374151;
    border-radius: 4px;
    cursor: pointer;
    transition: all 0.2s ease;
}

.toggle-switch.active {
    background-color: #10B981;
}

.toggle-switch::after {
    content: '';
    position: absolute;
    top: 2px;
    left: 2px;
    width: 20px;
    height: 20px;
    background-color: white;
    border-radius: 2px;
    transition: all 0.2s ease;
}

.toggle-switch.active::after {
    left: 26px;
}
```

**设计要点：**

- 尺寸为48x24px，符合工业控件标准
- 使用方形圆角，与整体风格保持一致
- 开启状态使用绿色背景，直观表示「运行中」
- 滑块移动使用200ms过渡动画，提供流畅的交互体验

### 进度条

进度条用于展示液位、储量等连续变化的数据。

```css
.level-bar {
    width: 120px;
    height: 6px;
    background-color: #374151;
    border-radius: 3px;
    overflow: hidden;
}

.level-fill {
    height: 100%;
    border-radius: 3px;
    transition: width 0.3s ease;
}

.level-fill.high { background-color: #10B981; }
.level-fill.medium { background-color: #F59E0B; }
.level-fill.low { background-color: #EF4444; }
```

**设计要点：**

- 高度仅为6px，保持数据密集区的整洁
- 使用颜色编码：绿=充足、橙=偏低、红=危险
- 宽度变化使用300ms过渡动画，避免突兀跳转

### 表格

表格是数据展示的核心组件，其设计需要兼顾信息密度与可读性。

```css
th {
    text-align: left;
    padding: 12px 20px;
    font-size: 12px;
    font-weight: 600;
    color: #6B7280;
    text-transform: uppercase;
    letter-spacing: 0.5px;
    background-color: #374151;
    border-bottom: 1px solid #4B5563;
}

td {
    padding: 16px 20px;
    font-size: 14px;
    border-bottom: 1px solid #4B5563;
}

tr:hover td {
    background-color: #374151;
}
```

**设计要点：**

- 表头使用大写字母与较小字号，区分于数据内容
- 行高设置为16px padding，平衡信息密度与呼吸感
- 悬停时整行背景变色，帮助用户定位当前行
- 单元格之间使用1px边框明确分隔

### 按钮

按钮是触发操作的主要界面元素，需要清晰区分主次操作。

```css
.btn {
    padding: 8px 16px;
    border-radius: 6px;
    font-size: 13px;
    font-weight: 500;
    cursor: pointer;
    transition: all 0.15s ease;
    display: flex;
    align-items: center;
    gap: 6px;
}

.btn-primary {
    background-color: #3B82F6;
    color: white;
    border: none;
}

.btn-primary:hover {
    background-color: #2563EB;
}

.btn-secondary {
    background-color: transparent;
    color: #9CA3AF;
    border: 1px solid #4B5563;
}

.btn-secondary:hover {
    background-color: #374151;
    color: #F9FAFB;
}
```

**设计要点：**

- 主按钮使用蓝色填充，视觉权重最高
- 次按钮使用边框样式，视觉权重较低
- 悬停时背景色变化，提供明确的交互反馈
- 按钮内使用图标+文字组合，确保操作意图明确

---

## 布局规范

### 页面结构

工业极简主义风格采用经典的侧边栏+主内容区布局，这种结构适合需要频繁切换功能模块的管理系统。

```
+------------------+----------------------------------------+
|                  |              顶部栏                   |
|    侧边栏        |  (面包屑 + 通知图标 + 用户头像)        |
|    (260px)      +----------------------------------------+
|                  |                                        |
|  - Logo         |           主内容区                     |
|  - 导航菜单     |                                        |
|                  |  - 页面标题                            |
|                  |  - KPI卡片网格 (4列)                   |
|                  |  - 数据表格                            |
|                  |                                        |
+------------------+----------------------------------------+
```

### 间距系统

间距是界面呼吸感的来源，合理的间距让界面既不拥挤也不松散。

```
页面边距        24px
卡片间距       16px
卡片内边距     20px
表格单元格     16px 20px
按钮间距       12px
```

### 响应式断点

```css
/* 桌面端 > 1200px */
.kpi-grid {
    grid-template-columns: repeat(4, 1fr);
}

/* 平板端 768px - 1200px */
@media (max-width: 1200px) {
    .kpi-grid {
        grid-template-columns: repeat(2, 1fr);
    }
}

/* 移动端 < 768px */
@media (max-width: 768px) {
    .sidebar {
        width: 70px;  /* 收起侧边栏 */
    }
    .kpi-grid {
        grid-template-columns: 1fr;
    }
}
```

---

## 交互设计

### 过渡时间

所有交互元素的过渡动画使用统一的150-200ms时间，既能提供流畅的视觉反馈，又不会因为动画过长而影响操作效率。

```css
transition: all 0.15s ease;    /* 常规元素 */
transition: all 0.2s ease;    /* 开关按钮 */
transition: all 0.3s ease;    /* 进度条 */
```

### 悬停状态

悬停状态是最常见的交互反馈形式，通过颜色变化提示用户元素可交互。

- 导航项：背景色变浅
- 卡片：背景色变浅
- 表格行：整行背景高亮
- 按钮：背景色加深
- 操作图标：颜色变化

### 禁用状态

对于不可操作的元素，使用降低透明度的方式表示禁用状态，同时移除交互反馈。

```css
.element:disabled {
    opacity: 0.5;
    cursor: not-allowed;
}
```

---

## 燃气行业特殊规范

### 设备状态显示

燃气监控系统的核心是设备状态的可视化呈现。以下是建议的显示规范：

**储罐状态**

| 状态 | 显示方式 | 颜色 |
|------|----------|------|
| 正常运行 | 绿色徽章 + 圆点 | #10B981 |
| 液位偏低 | 橙色徽章 + 圆点 | #F59E0B |
| 压力异常 | 红色徽章 + 圆点 + 闪烁 | #EF4444 |
| 设备离线 | 灰色徽章 + 圆点 | #6B7280 |

**告警机制**

建议在页面顶部设置固定告警栏，实时显示当前活跃的告警数量。告警等级使用以下颜色区分：

- 一级告警（紧急）：红色 #EF4444
- 二级告警（严重）：橙色 #F59E0B
- 三级告警（一般）：蓝色 #3B82F6

### 数据精度

压力、温度等数值显示建议保留合适的小数位数：

- 压力：保留1位小数，单位Bar
- 温度：保留1位小数，单位°C
- 液位：保留整数，单位%
- 时间：显示到秒，格式HH:MM:SS

---

## CSS 变量速查表

以下是完整的CSS变量定义，开发者可以直接复制使用：

```css
:root {
    /* 背景色 */
    --bg-primary: #111827;
    --bg-secondary: #1F2937;
    --bg-tertiary: #374151;
    --border-color: #4B5563;

    /* 文字色 */
    --text-primary: #F9FAFB;
    --text-secondary: #9CA3AF;
    --text-muted: #6B7280;

    /* 强调色 */
    --accent-blue: #3B82F6;
    --accent-cyan: #06B6D4;
    --status-normal: #10B981;
    --status-warning: #F59E0B;
    --status-critical: #EF4444;
    --status-offline: #6B7280;
}
```

---

## 风格对比

| 特性 | 工业极简主义 | 玻璃拟态科技风 | B端新拟态 |
|------|-------------|----------------|-----------|
| 背景 | 深炭灰纯色 | 渐变+光晕动画 | 雾蓝米白纯色 |
| 阴影 | 无/扁平 | 悬浮感 | 凹凸双阴影 |
| 边框 | 1px实线 | 半透明渐变 | 无边框 |
| 圆角 | 4-8px | 16-20px | 20-30px |
| 字体 | Inter + JetBrains Mono | Exo 2 / Rajdhani | Nunito / Quicksand |
| 交互 | 背景色变化 | 发光+上浮 | 凹陷效果 |
| 适用场景 | 工业专业 | 科技展示 | 日常操作 |

---

## 实施建议

### 立即可做的改动

如果希望以最小的成本提升现有系统的视觉效果，建议按以下优先级进行改进：

1. **替换配色方案**——采用本规范定义的色彩体系，通过CSS变量实现全局统一
2. **优化状态显示**——将文字状态的「打开/关闭」改为彩色徽章+图标
3. **改进表格样式**——增加行高、区分表头、添加悬停效果
4. **调整间距**——增加模块间距至16-24px，提升呼吸感

### 需要前端支持的改动

以下改动需要一定的前端开发工作量：

1. **引入组件库**——建议使用Tailwind CSS或Element Plus，利用其成熟的组件体系
2. **响应式布局**——适配平板巡检场景，实现侧边栏折叠
3. **暗黑模式**——为24小时监控室提供低光环境适配
4. **数据可视化**——集成ECharts或D3.js，实现更丰富的图表展示

---

**文档版本：** 1.0
**最后更新：** 2026-03-05
**适用项目：** LPG燃气后台管理系统
**风格名称：** 工业极简主义 (Industrial Minimalism)