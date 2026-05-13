import type { ApiResponse, PortfolioProject } from './types';

async function request<T>(url: string): Promise<T> {
  const response = await fetch(url);
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

