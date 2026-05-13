<script setup lang="ts">
import { computed, onMounted, ref } from 'vue';
import { ArrowUpRight, Code2, Database, ExternalLink, Github, Loader2, RefreshCw, Server } from 'lucide-vue-next';
import { getProject, listProjects } from './api';
import type { PortfolioProject, ProjectStatus } from './types';

const projects = ref<PortfolioProject[]>([]);
const selectedProject = ref<PortfolioProject | null>(null);
const activeStatus = ref<ProjectStatus | 'ALL'>('ALL');
const loading = ref(true);
const detailLoading = ref(false);
const error = ref('');

const statusText: Record<ProjectStatus, string> = {
  ONLINE: '运行中',
  DEVELOPING: '开发中',
  OFFLINE: '已下线'
};

const deployText = {
  LOCAL: '当前服务器',
  EXTERNAL: '外部地址'
};

const filteredProjects = computed(() => {
  if (activeStatus.value === 'ALL') {
    return projects.value;
  }
  return projects.value.filter((project) => project.status === activeStatus.value);
});

const recommendedProjects = computed(() => projects.value.filter((project) => project.recommend));

async function loadProjects() {
  loading.value = true;
  error.value = '';
  try {
    projects.value = await listProjects();
    selectedProject.value = projects.value[0] ?? null;
  } catch (err) {
    error.value = err instanceof Error ? err.message : '作品列表加载失败';
  } finally {
    loading.value = false;
  }
}

async function selectProject(project: PortfolioProject) {
  selectedProject.value = project;
  detailLoading.value = true;
  try {
    selectedProject.value = await getProject(project.id);
  } catch {
    selectedProject.value = project;
  } finally {
    detailLoading.value = false;
  }
}

function openProject(project: PortfolioProject) {
  window.open(project.projectUrl, '_blank', 'noopener,noreferrer');
}

onMounted(loadProjects);
</script>

<template>
  <main class="app-shell">
    <section class="topbar">
      <div>
        <p class="eyebrow">LZF Studio</p>
        <h1>个人在线作品集</h1>
      </div>
      <button class="icon-button" type="button" title="刷新作品" @click="loadProjects">
        <RefreshCw :size="18" />
      </button>
    </section>

    <section class="summary-band">
      <div class="summary-copy">
        <p>集中展示已上线、开发中和托管在当前服务器的项目。</p>
        <div class="summary-actions">
          <button
            v-for="status in ['ALL', 'ONLINE', 'DEVELOPING']"
            :key="status"
            class="segment-button"
            :class="{ active: activeStatus === status }"
            type="button"
            @click="activeStatus = status as ProjectStatus | 'ALL'"
          >
            {{ status === 'ALL' ? '全部' : statusText[status as ProjectStatus] }}
          </button>
        </div>
      </div>
      <div class="metrics">
        <div>
          <span>{{ projects.length }}</span>
          <p>作品</p>
        </div>
        <div>
          <span>{{ recommendedProjects.length }}</span>
          <p>推荐</p>
        </div>
        <div>
          <span>{{ projects.filter((item) => item.deployType === 'LOCAL').length }}</span>
          <p>本机部署</p>
        </div>
      </div>
    </section>

    <section class="content-grid">
      <div class="project-list">
        <div v-if="loading" class="empty-state">
          <Loader2 class="spin" :size="22" />
          <span>加载作品中</span>
        </div>

        <div v-else-if="error" class="empty-state error-state">
          <span>{{ error }}</span>
          <button type="button" @click="loadProjects">重试</button>
        </div>

        <article
          v-for="project in filteredProjects"
          v-else
          :key="project.id"
          class="project-card"
          :class="{ selected: selectedProject?.id === project.id }"
          @click="selectProject(project)"
        >
          <img :src="project.coverUrl || '/covers/fallback.png'" :alt="project.name" />
          <div class="card-body">
            <div class="card-title-row">
              <h2>{{ project.name }}</h2>
              <span :class="['status-pill', project.status.toLowerCase()]">{{ statusText[project.status] }}</span>
            </div>
            <p>{{ project.description }}</p>
            <div class="chip-row">
              <span v-for="tech in project.techStack" :key="tech">{{ tech }}</span>
            </div>
          </div>
        </article>

        <div v-if="!loading && !error && filteredProjects.length === 0" class="empty-state">
          <span>当前筛选条件下暂无作品</span>
        </div>
      </div>

      <aside class="detail-panel" :class="{ muted: !selectedProject }">
        <div v-if="selectedProject" class="detail-inner">
          <div class="detail-hero">
            <img :src="selectedProject.coverUrl || '/covers/fallback.png'" :alt="selectedProject.name" />
            <span v-if="detailLoading" class="detail-loading">
              <Loader2 class="spin" :size="16" />
            </span>
          </div>

          <div class="detail-heading">
            <div>
              <p>{{ deployText[selectedProject.deployType] }}</p>
              <h2>{{ selectedProject.name }}</h2>
            </div>
            <button class="primary-icon-button" type="button" title="访问作品" @click="openProject(selectedProject)">
              <ArrowUpRight :size="20" />
            </button>
          </div>

          <p class="detail-description">{{ selectedProject.description }}</p>

          <div class="detail-actions">
            <a class="action-link primary" :href="selectedProject.projectUrl" target="_blank" rel="noreferrer">
              <ExternalLink :size="17" />
              访问作品
            </a>
            <a
              v-if="selectedProject.sourceUrl"
              class="action-link"
              :href="selectedProject.sourceUrl"
              target="_blank"
              rel="noreferrer"
            >
              <Github :size="17" />
              源码
            </a>
          </div>

          <dl class="detail-meta">
            <div>
              <dt><Code2 :size="16" /> 技术栈</dt>
              <dd>{{ selectedProject.techStack.join(' / ') || '未配置' }}</dd>
            </div>
            <div>
              <dt><Database :size="16" /> 标签</dt>
              <dd>{{ selectedProject.tags.join(' / ') || '未配置' }}</dd>
            </div>
            <div>
              <dt><Server :size="16" /> 访问地址</dt>
              <dd>{{ selectedProject.projectUrl }}</dd>
            </div>
          </dl>
        </div>

        <div v-else class="empty-state">
          <span>选择一个作品查看详情</span>
        </div>
      </aside>
    </section>
  </main>
</template>

