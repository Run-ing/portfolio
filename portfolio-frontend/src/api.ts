import type { ApiResponse, PortfolioProject } from './types';

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL?.replace(/\/$/, '') ?? '';

function buildApiUrl(path: string): string {
  return `${API_BASE_URL}${path}`;
}

async function request<T>(url: string): Promise<T> {
  const response = await fetch(buildApiUrl(url));
  if (!response.ok) {
    throw new Error(`HTTP ${response.status}`);
  }

  const body = (await response.json()) as ApiResponse<T>;
  if (body.code !== 200) {
    throw new Error(body.message || '请求失败');
  }
  return body.data;
}

export function listProjects(): Promise<PortfolioProject[]> {
  return request<PortfolioProject[]>('/api/portfolio/projects');
}

export function getProject(id: number): Promise<PortfolioProject> {
  return request<PortfolioProject>(`/api/portfolio/projects/${id}`);
}
