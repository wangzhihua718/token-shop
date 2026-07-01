<template>
  <div id="app">
    <!-- 全局星星背景 -->
    <div class="global-stars">
      <div v-for="i in 80" :key="i" class="global-star" :style="starStyles[i]"></div>
    </div>
    <router-view v-slot="{ Component }">
      <transition name="fade" mode="out-in">
        <component :is="Component" />
      </transition>
    </router-view>
  </div>
</template>

<script setup lang="ts">
// 根组件 - 生成星星位置
const starStyles = Array.from({ length: 80 }, () => ({
  left: `${Math.random() * 100}%`,
  top: `${Math.random() * 100}%`,
  animationDelay: `${Math.random() * 5}s`,
  animationDuration: `${2 + Math.random() * 4}s`,
  width: `${1 + Math.random() * 2}px`,
  height: `${1 + Math.random() * 2}px`
}))
</script>

<style>
/* ========== 二次元主题全局样式 ========== */
@import url('https://fonts.googleapis.com/css2?family=Noto+Sans+SC:wght@300;400;500;700&display=swap');

:root {
  --primary-pink: #ff6b9d;
  --primary-purple: #c44dff;
  --primary-blue: #4dc8ff;
  --primary-mint: #4dffcb;
  --primary-yellow: #ffd84d;
  --primary-orange: #ff8c4d;
  --gradient-main: linear-gradient(135deg, #ff6b9d 0%, #c44dff 50%, #4dc8ff 100%);
  --gradient-card: linear-gradient(135deg, rgba(255,107,157,0.1) 0%, rgba(196,77,255,0.1) 100%);
  --gradient-hover: linear-gradient(135deg, #ff6b9d 0%, #c44dff 100%);
  --bg-main: #0a0a1a;
  --bg-card: rgba(255, 255, 255, 0.05);
  --bg-glass: rgba(255, 255, 255, 0.08);
  --text-primary: #ffffff;
  --text-secondary: rgba(255, 255, 255, 0.7);
  --text-muted: rgba(255, 255, 255, 0.5);
  --border-glow: 0 0 20px rgba(255, 107, 157, 0.3);
  --border-card: 1px solid rgba(255, 255, 255, 0.1);
}

* { box-sizing: border-box; }

body {
  margin: 0; padding: 0;
  font-family: 'Noto Sans SC', -apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif;
  background: var(--bg-main);
  color: var(--text-primary);
  overflow-x: hidden;
}

/* 动漫壁纸背景 - 真实动漫图片 */
body::before {
  content: '';
  position: fixed;
  top: 0; left: 0;
  width: 100%; height: 100%;
  /* 动漫城市夜景壁纸 */
  background: 
    url('/wallpapers/anime-city-night.jpg') center/cover no-repeat;
  z-index: -2;
  filter: brightness(0.3) saturate(1.3);
}

/* 遮罩层 - 让内容更清晰 + 动漫光晕 */
body::after {
  content: '';
  position: fixed;
  top: 0; left: 0;
  width: 100%; height: 100%;
  background:
    /* 动漫风格光晕 */
    radial-gradient(ellipse 60% 50% at 10% 90%, rgba(255, 107, 157, 0.15) 0%, transparent 60%),
    radial-gradient(ellipse 50% 40% at 90% 10%, rgba(196, 77, 255, 0.15) 0%, transparent 60%),
    radial-gradient(ellipse 70% 50% at 50% 50%, rgba(77, 200, 255, 0.1) 0%, transparent 50%),
    /* 渐变遮罩 */
    linear-gradient(180deg, 
      rgba(10, 10, 26, 0.4) 0%, 
      rgba(10, 10, 26, 0.2) 30%, 
      rgba(10, 10, 26, 0.2) 70%, 
      rgba(10, 10, 26, 0.6) 100%);
  z-index: -1;
}

#app { width: 100%; min-height: 100vh; position: relative; }

/* 全局星星 */
.global-stars { position: fixed; top: 0; left: 0; width: 100%; height: 100%; pointer-events: none; z-index: 0; }
.global-star {
  position: absolute; background: white; border-radius: 50%;
  animation: twinkle 3s ease-in-out infinite;
}
@keyframes twinkle {
  0%, 100% { opacity: 0.15; transform: scale(1); }
  50% { opacity: 0.8; transform: scale(1.5); }
}

/* 滚动条 */
::-webkit-scrollbar { width: 6px; }
::-webkit-scrollbar-track { background: transparent; }
::-webkit-scrollbar-thumb { background: linear-gradient(180deg, var(--primary-pink), var(--primary-purple)); border-radius: 3px; }

/* ========== Element Plus 全局覆盖 ========== */
.el-card {
  background: var(--bg-glass) !important;
  backdrop-filter: blur(20px);
  border: var(--border-card) !important;
  border-radius: 16px !important;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.3) !important;
  color: var(--text-primary) !important;
}
.el-card__header {
  background: transparent !important;
  border-bottom: 1px solid rgba(255, 255, 255, 0.08) !important;
  color: var(--text-primary) !important;
  padding: 16px 20px !important;
}
.el-card__body { color: var(--text-primary) !important; }

.el-button--primary {
  --el-button-bg-color: linear-gradient(135deg, #ff6b9d, #c44dff) !important;
  --el-button-border-color: transparent !important;
  background: linear-gradient(135deg, #ff6b9d, #c44dff) !important;
  border: none !important;
  border-radius: 12px !important;
  box-shadow: 0 4px 15px rgba(255, 107, 157, 0.3) !important;
  transition: all 0.3s !important;
}
.el-button--primary:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 25px rgba(255, 107, 157, 0.5) !important;
}

.el-button { border-radius: 10px !important; }

/* 输入框 */
.el-input__wrapper, .el-textarea__inner {
  background: rgba(255, 255, 255, 0.05) !important;
  border-radius: 12px !important;
  box-shadow: 0 0 0 1px rgba(255, 255, 255, 0.1) !important;
  color: var(--text-primary) !important;
  transition: all 0.3s !important;
}
.el-input__wrapper:hover, .el-textarea__inner:hover {
  box-shadow: 0 0 0 1px rgba(255, 107, 157, 0.4) !important;
}
.el-input__wrapper.is-focus, .el-textarea__inner.is-focus {
  box-shadow: 0 0 0 2px rgba(255, 107, 157, 0.3) !important;
}
.el-input__inner, .el-textarea__inner { color: var(--text-primary) !important; }
.el-input__inner::placeholder, .el-textarea__inner::placeholder { color: var(--text-muted) !important; }

/* Select */
.el-select__wrapper {
  background: rgba(255, 255, 255, 0.05) !important;
  border-radius: 12px !important;
  box-shadow: 0 0 0 1px rgba(255, 255, 255, 0.1) !important;
}
.el-select-dropdown__item { color: var(--text-primary) !important; }
.el-select-dropdown { background: rgba(20, 20, 40, 0.95) !important; backdrop-filter: blur(20px); border: 1px solid rgba(255,255,255,0.1) !important; }

/* Table */
.el-table {
  --el-table-bg-color: transparent !important;
  --el-table-tr-bg-color: transparent !important;
  --el-table-header-bg-color: rgba(255, 255, 255, 0.05) !important;
  --el-table-text-color: var(--text-primary) !important;
  --el-table-header-text-color: var(--text-secondary) !important;
  --el-table-border-color: rgba(255, 255, 255, 0.08) !important;
  --el-table-row-hover-bg-color: rgba(255, 107, 157, 0.08) !important;
  color: var(--text-primary) !important;
}
.el-table th.el-table__cell { background: rgba(255,255,255,0.05) !important; }

/* Tag */
.el-tag { border-radius: 8px !important; border: none !important; }

/* Dialog */
.el-overlay { background: rgba(0, 0, 0, 0.6) !important; backdrop-filter: blur(5px); }
.el-dialog {
  background: rgba(15, 15, 35, 0.95) !important;
  backdrop-filter: blur(30px);
  border: 1px solid rgba(255, 255, 255, 0.1) !important;
  border-radius: 20px !important;
  box-shadow: 0 25px 80px rgba(0, 0, 0, 0.5) !important;
}
.el-dialog__header { border-bottom: 1px solid rgba(255,255,255,0.08) !important; }
.el-dialog__title { color: var(--text-primary) !important; }
.el-dialog__body { color: var(--text-primary) !important; }

/* Pagination */
.el-pagination { --el-text-color-regular: var(--text-secondary) !important; --el-text-color-placeholder: var(--text-muted) !important; }
.el-pagination.is-background .el-pager li:not(.disabled).active { background: linear-gradient(135deg, #ff6b9d, #c44dff) !important; }
.el-pagination.is-background .el-pager li:not(.disabled):hover { color: var(--primary-pink) !important; }
.el-pagination button:hover { color: var(--primary-pink) !important; }

/* Menu */
.el-menu--horizontal > .el-menu-item, .el-menu--horizontal > .el-submenu .el-submenu__title {
  transition: all 0.3s !important;
}

/* Dropdown */
.el-dropdown-menu {
  background: rgba(15, 15, 35, 0.95) !important;
  backdrop-filter: blur(20px);
  border: 1px solid rgba(255,255,255,0.1) !important;
  border-radius: 12px !important;
}
.el-dropdown-menu__item { color: var(--text-primary) !important; }
.el-dropdown-menu__item:hover { background: rgba(255, 107, 157, 0.1) !important; color: var(--primary-pink) !important; }

/* Tabs */
.el-tabs__active-bar { background: linear-gradient(90deg, var(--primary-pink), var(--primary-purple)) !important; }
.el-tabs__item { color: var(--text-secondary) !important; }
.el-tabs__item.is-active { color: var(--primary-pink) !important; }
.el-tabs__item:hover { color: var(--primary-pink) !important; }

/* Radio */
.el-radio-button__inner {
  background: rgba(255,255,255,0.05) !important;
  color: var(--text-secondary) !important;
  border: 1px solid rgba(255,255,255,0.1) !important;
}
.el-radio-button__original-radio:checked + .el-radio-button__inner {
  background: linear-gradient(135deg, #ff6b9d, #c44dff) !important;
  border-color: transparent !important;
  color: white !important;
  box-shadow: 0 2px 10px rgba(255, 107, 157, 0.3) !important;
}

/* Carousel */
.el-carousel__indicator button { background: rgba(255,255,255,0.3) !important; }
.el-carousel__indicator--horizontal.is-active button { background: var(--primary-pink) !important; }
.el-carousel__arrow { background: rgba(255,255,255,0.1) !important; }
.el-carousel__arrow:hover { background: rgba(255,107,157,0.3) !important; }

/* Page Header */
.el-page-header__title { color: var(--text-primary) !important; }
.el-page-header__desc { color: var(--text-secondary) !important; }
.el-page-header__left .el-button { color: var(--primary-pink) !important; }

/* Descriptions */
.el-descriptions { --el-descriptions-title-font-size: 16px !important; }
.el-descriptions__label { color: var(--text-secondary) !important; background: rgba(255,255,255,0.03) !important; }
.el-descriptions__content { color: var(--text-primary) !important; }

/* Empty */
.el-empty__description p { color: var(--text-muted) !important; }

/* Message */
.el-message {
  background: rgba(15, 15, 35, 0.95) !important;
  backdrop-filter: blur(20px);
  border: 1px solid rgba(255,255,255,0.1) !important;
  border-radius: 12px !important;
}

/* Transition */
.fade-enter-active, .fade-leave-active { transition: all 0.3s ease; }
.fade-enter-from { opacity: 0; transform: translateY(15px); }
.fade-leave-to { opacity: 0; transform: translateY(-15px); }

/* 通用工具类 */
.glass-card {
  background: var(--bg-glass);
  backdrop-filter: blur(20px);
  border: var(--border-card);
  border-radius: 16px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.3);
}
.gradient-text {
  background: var(--gradient-main);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}
</style>
