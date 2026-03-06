# 浅色商务风格设计规范

## 概述

浅色商务风格是一种明亮、专业、清晰的界面设计风格，适用于企业后台管理系统。该风格强调内容的可读性与操作的高效性，通过柔和的色彩搭配与清晰的层级结构，营造舒适的工作氛围。

### 核心设计理念

**明亮通透**——以白色和浅灰色为主基调，最大化内容区域的亮度，减少视觉压抑感。

**清晰层级**——通过微妙的阴影与边框区分元素层级，而非强烈的色彩对比。

**专业克制**——使用低饱和度的强调色，避免过于活泼的色彩影响专业感。

**统一规范**——所有组件遵循统一的设计语言，降低用户学习成本。

---

## 配色方案

### 色彩体系

#### 背景色彩

```
#FFFFFF  主背景色 (内容区域)
#F8FAFC  次级背景 (卡片、表格)
#F1F5F9  第三级背景 (悬停状态、表头)
#E2E8F0  边框颜色
#CBD5E1  次要边框
```

#### 文字色彩

```
#1E293B  主要文字 (标题、重要内容)
#475569  次要文字 (正文、标签)
#64748B  弱化文字 (占位符、辅助信息)
#94A3B8  禁用文字
```

#### 强调色彩

```
#3B82F6  商务蓝 (主强调色，用于主要按钮、链接)
#10B981  翠绿 (成功状态)
#F59E0B  琥珀橙 (警告状态)
#EF4444  红色 (危险状态、错误)
#6366F1  靛蓝 (辅助强调色)
```

### 状态色彩语义

| 状态 | 背景色 | 文字色 | 边框色 | 使用场景 |
|------|--------|--------|--------|----------|
| 正常 | rgba(16,185,129,0.1) | #059669 | rgba(16,185,129,0.3) | 设备正常、成功 |
| 警告 | rgba(245,158,11,0.1) | #D97706 | rgba(245,158,11,0.3) | 液位偏低、注意 |
| 危险 | rgba(239,68,68,0.1) | #DC2626 | rgba(239,68,68,0.3) | 压力异常、错误 |
| 离线 | rgba(100,116,139,0.1) | #475569 | rgba(100,116,139,0.3) | 设备离线 |

---

## 字体规范

### 字体选择

```css
/* 界面字体 */
font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', sans-serif;

/* 数据字体（可选） */
font-family: 'Roboto Mono', 'Courier New', monospace;
```

### 字号层级

```
页面标题    20px / 600 weight    页面主标题
区块标题    16px / 600 weight    卡片标题、表格标题
KPI 数值     24px / 600 weight    关键数据展示
正文       14px / 400 weight    表格内容、表单
标签/小字   12-13px / 400 weight  状态徽章、辅助信息
```

---

## 组件规范

### 1. 搜索区域

```css
.search-area {
    background: #FFFFFF;
    border: 1px solid #E2E8F0;
    border-radius: 8px;
    padding: 16px 20px;
    margin-bottom: 20px;
    display: flex;
    align-items: center;
    flex-wrap: wrap;
    gap: 12px;
}

.search-area label {
    font-size: 14px;
    font-weight: 500;
    color: #475569;
    white-space: nowrap;
}

.search-area input[type="text"],
.search-area select {
    padding: 8px 12px;
    font-size: 14px;
    border: 1px solid #CBD5E1;
    border-radius: 6px;
    background: #FFFFFF;
    color: #1E293B;
    min-width: 180px;
    transition: all 0.15s ease;
}

.search-area input:focus,
.search-area select:focus {
    outline: none;
    border-color: #3B82F6;
    box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
}

.search-area .btn-search {
    background: #3B82F6;
    color: #FFFFFF;
    border: none;
    padding: 8px 20px;
    border-radius: 6px;
    font-size: 14px;
    font-weight: 500;
    cursor: pointer;
    transition: all 0.15s ease;
}

.search-area .btn-search:hover {
    background: #2563EB;
}

.search-area .btn-reset {
    background: #FFFFFF;
    color: #475569;
    border: 1px solid #CBD5E1;
    padding: 8px 20px;
    border-radius: 6px;
    font-size: 14px;
    font-weight: 500;
    cursor: pointer;
    transition: all 0.15s ease;
}

.search-area .btn-reset:hover {
    background: #F8FAFC;
    border-color: #94A3B8;
}
```

### 2. 表格样式

```css
.data-table {
    width: 100%;
    border-collapse: collapse;
    background: #FFFFFF;
    border: 1px solid #E2E8F0;
    border-radius: 8px;
    overflow: hidden;
}

.data-table thead th {
    background: #F8FAFC;
    color: #475569;
    font-size: 13px;
    font-weight: 600;
    text-align: left;
    padding: 14px 16px;
    border-bottom: 1px solid #E2E8F0;
    text-transform: uppercase;
    letter-spacing: 0.3px;
}

.data-table tbody td {
    padding: 14px 16px;
    font-size: 14px;
    color: #1E293B;
    border-bottom: 1px solid #F1F5F9;
    vertical-align: middle;
}

.data-table tbody tr {
    background: #FFFFFF;
    transition: background 0.15s ease;
}

.data-table tbody tr:nth-child(even) {
    background: #F8FAFC;
}

.data-table tbody tr:hover {
    background: #F1F5F9;
}

.data-table tbody tr:last-child td {
    border-bottom: none;
}
```

### 3. 表单样式

```css
.form-container {
    background: #FFFFFF;
    padding: 24px;
}

.form-group {
    margin-bottom: 20px;
}

.form-group label {
    display: block;
    font-size: 14px;
    font-weight: 500;
    color: #475569;
    margin-bottom: 8px;
}

.form-group label .required {
    color: #EF4444;
    margin-right: 4px;
}

.form-group input[type="text"],
.form-group input[type="email"],
.form-group input[type="password"],
.form-group input[type="number"],
.form-group textarea,
.form-group select {
    width: 100%;
    padding: 10px 14px;
    font-size: 14px;
    border: 1px solid #CBD5E1;
    border-radius: 6px;
    background: #FFFFFF;
    color: #1E293B;
    transition: all 0.15s ease;
    box-sizing: border-box;
}

.form-group input:focus,
.form-group textarea:focus,
.form-group select:focus {
    outline: none;
    border-color: #3B82F6;
    box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
}

.form-group input[readonly] {
    background: #F8FAFC;
    color: #64748B;
    cursor: not-allowed;
}

.form-group textarea {
    resize: vertical;
    min-height: 80px;
}

.form-group .help-text {
    font-size: 12px;
    color: #64748B;
    margin-top: 6px;
}
```

### 4. 按钮样式

```css
.btn {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    gap: 6px;
    padding: 10px 20px;
    font-size: 14px;
    font-weight: 500;
    border-radius: 6px;
    cursor: pointer;
    transition: all 0.15s ease;
    border: none;
}

.btn-primary {
    background: #3B82F6;
    color: #FFFFFF;
}

.btn-primary:hover {
    background: #2563EB;
}

.btn-primary:active {
    background: #1D4ED8;
}

.btn-secondary {
    background: #FFFFFF;
    color: #475569;
    border: 1px solid #CBD5E1;
}

.btn-secondary:hover {
    background: #F8FAFC;
    border-color: #94A3B8;
}

.btn-danger {
    background: #EF4444;
    color: #FFFFFF;
}

.btn-danger:hover {
    background: #DC2626;
}

.btn-sm {
    padding: 6px 12px;
    font-size: 13px;
}

.btn-lg {
    padding: 12px 24px;
    font-size: 16px;
}
```

### 5. 弹窗样式（模态框）

```css
.modal-overlay {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: rgba(0, 0, 0, 0.5);
    display: flex;
    align-items: center;
    justify-content: center;
    z-index: 1000;
}

.modal-container {
    background: #FFFFFF;
    border-radius: 12px;
    box-shadow: 0 20px 40px rgba(0, 0, 0, 0.15);
    max-width: 600px;
    width: 90%;
    max-height: 90vh;
    overflow: hidden;
    display: flex;
    flex-direction: column;
}

.modal-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 16px 20px;
    border-bottom: 1px solid #E2E8F0;
    background: #F8FAFC;
}

.modal-title {
    font-size: 16px;
    font-weight: 600;
    color: #1E293B;
    margin: 0;
}

.modal-close {
    background: none;
    border: none;
    font-size: 24px;
    color: #64748B;
    cursor: pointer;
    padding: 0;
    width: 32px;
    height: 32px;
    display: flex;
    align-items: center;
    justify-content: center;
    border-radius: 4px;
    transition: all 0.15s ease;
}

.modal-close:hover {
    background: #F1F5F9;
    color: #1E293B;
}

.modal-body {
    padding: 20px;
    overflow-y: auto;
    flex: 1;
}

.modal-footer {
    display: flex;
    justify-content: flex-end;
    gap: 12px;
    padding: 16px 20px;
    border-top: 1px solid #E2E8F0;
    background: #F8FAFC;
}
```

### 6. 卡片组件

```css
.card {
    background: #FFFFFF;
    border: 1px solid #E2E8F0;
    border-radius: 8px;
    padding: 20px;
    transition: all 0.15s ease;
}

.card:hover {
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
}

.card-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 16px;
    padding-bottom: 12px;
    border-bottom: 1px solid #F1F5F9;
}

.card-title {
    font-size: 16px;
    font-weight: 600;
    color: #1E293B;
    margin: 0;
}
```

### 7. 状态徽章

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

.status-badge.success {
    background: rgba(16, 185, 129, 0.1);
    color: #059669;
    border: 1px solid rgba(16, 185, 129, 0.3);
}

.status-badge.warning {
    background: rgba(245, 158, 11, 0.1);
    color: #D97706;
    border: 1px solid rgba(245, 158, 11, 0.3);
}

.status-badge.danger {
    background: rgba(239, 68, 68, 0.1);
    color: #DC2626;
    border: 1px solid rgba(239, 68, 68, 0.3);
}

.status-badge.info {
    background: rgba(59, 130, 246, 0.1);
    color: #2563EB;
    border: 1px solid rgba(59, 130, 246, 0.3);
}

.status-dot {
    width: 6px;
    height: 6px;
    border-radius: 50%;
    background: currentColor;
}
```

### 8. 分页样式

```css
.pagination {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 8px;
    margin-top: 20px;
}

.pagination a,
.pagination span {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    min-width: 36px;
    height: 36px;
    padding: 0 8px;
    font-size: 14px;
    border: 1px solid #E2E8F0;
    border-radius: 6px;
    color: #475569;
    background: #FFFFFF;
    text-decoration: none;
    transition: all 0.15s ease;
}

.pagination a:hover {
    background: #F8FAFC;
    border-color: #CBD5E1;
}

.pagination .current-page {
    background: #3B82F6;
    color: #FFFFFF;
    border-color: #3B82F6;
}

.pagination .disabled {
    color: #94A3B8;
    background: #F8FAFC;
    cursor: not-allowed;
}
```

---

## 表单排列规范

### 单列表单（适合字段较少的表单）

```css
.form-single-column .form-group {
    margin-bottom: 20px;
}
```

### 双列表单（适合字段较多的表单）

```css
.form-two-column {
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    gap: 20px;
}

@media (max-width: 768px) {
    .form-two-column {
        grid-template-columns: 1fr;
    }
}
```

### 表单组（相关字段组合）

```css
.form-section {
    margin-bottom: 24px;
}

.form-section-title {
    font-size: 14px;
    font-weight: 600;
    color: #1E293B;
    margin-bottom: 16px;
    padding-bottom: 8px;
    border-bottom: 1px solid #E2E8F0;
}
```

---

## 响应式断点

```css
/* 桌面端 > 1024px */
/* 默认样式 */

/* 平板端 768px - 1024px */
@media (max-width: 1024px) {
    .form-two-column {
        grid-template-columns: repeat(2, 1fr);
    }
}

/* 移动端 < 768px */
@media (max-width: 768px) {
    .search-area {
        flex-direction: column;
        align-items: stretch;
    }

    .search-area input[type="text"],
    .search-area select {
        width: 100%;
    }

    .form-two-column {
        grid-template-columns: 1fr;
    }

    .data-table {
        font-size: 13px;
    }

    .data-table th,
    .data-table td {
        padding: 10px 12px;
    }
}
```

---

## 与深色框架的搭配

由于主框架（左侧菜单、顶部导航）保持深色工业风，内容区域使用浅色风格时，需要注意以下过渡：

```css
/* 内容区域容器 */
#content {
    background: #F8FAFC;  /* 使用稍深的背景作为过渡 */
}

/* 内容区域内部使用纯白背景 */
.main-content {
    background: #FFFFFF;
    margin: 20px;
    border-radius: 8px;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}
```

---

## CSS 变量速查表

```css
:root {
    /* 背景色 */
    --light-bg-primary: #FFFFFF;
    --light-bg-secondary: #F8FAFC;
    --light-bg-tertiary: #F1F5F9;
    --light-border: #E2E8F0;
    --light-border-light: #CBD5E1;

    /* 文字色 */
    --light-text-primary: #1E293B;
    --light-text-secondary: #475569;
    --light-text-muted: #64748B;
    --light-text-disabled: #94A3B8;

    /* 强调色 */
    --light-accent-blue: #3B82F6;
    --light-accent-indigo: #6366F1;
    --light-success: #10B981;
    --light-warning: #F59E0B;
    --light-danger: #EF4444;
}
```

---

**文档版本：** 1.0
**最后更新：** 2026-03-05
**适用项目：** LPG 燃气后台管理系统 - 内容区域
**风格名称：** 浅色商务风格 (Light Business Style)
