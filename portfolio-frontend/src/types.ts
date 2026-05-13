export type DeployType = 'LOCAL' | 'EXTERNAL';
export type ProjectStatus = 'ONLINE' | 'DEVELOPING' | 'OFFLINE';

export interface PortfolioProject {
  id: number;
  name: string;
  description: string;
  coverUrl: string;
  techStack: string[];
  tags: string[];
  deployType: DeployType;
  projectUrl: string;
  sourceUrl?: string;
  status: ProjectStatus;
  recommend: boolean;
  sortOrder?: number;
  createdAt?: string;
  updatedAt?: string;
}

export interface ApiResponse<T> {
  code: number;
  message: string;
  data: T;
}

