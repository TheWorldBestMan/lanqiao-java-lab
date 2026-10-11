import axios from 'axios'
import type { AxiosResponse } from 'axios'
import { staticGet, staticPost } from './demoData'

const apiBase = import.meta.env.VITE_API_BASE_URL as string | undefined
const staticDemo = import.meta.env.PROD && !apiBase
const http = axios.create({ baseURL: apiBase ? `${apiBase.replace(/\/$/, '')}/api/v1` : '/api/v1', timeout: 12_000 })

export const api = {
  async get<T>(url: string): Promise<AxiosResponse<T>> {
    if (!staticDemo) return http.get<T>(url)
    return { data: staticGet(url) as T } as AxiosResponse<T>
  },
  async post<T>(url: string, body?: unknown): Promise<AxiosResponse<T>> {
    if (!staticDemo) return http.post<T>(url, body)
    return { data: staticPost<T>(url) } as AxiosResponse<T>
  },
}

export type Difficulty = 'EASY' | 'MEDIUM' | 'HARD'
export type LicenseStatus = 'ORIGINAL' | 'AUTHORIZED' | 'LINK_ONLY' | 'METADATA_ONLY' | 'RESTRICTED'

export interface ProblemSummary {
  id: number
  slug: string
  title: string
  summary: string
  difficulty: Difficulty
  sourceType: string
  sourceLabel: string
  sourceRef?: string
  licenseStatus: LicenseStatus
  contentAvailable: boolean
  tags: string[]
}

export interface ProblemDetail extends ProblemSummary {
  descriptionMd: string
  timeLimitMs: number
  memoryLimitMb: number
  templateCode: string
  sampleInput: string
  sampleOutput: string
}

export interface Submission {
  id: string
  problemId: number
  problemTitle: string
  status: string
  score?: number
  runtimeMs?: number
  memoryKb?: number
  message?: string
  createdAt: string
}

export interface KnowledgeNode { id: string; title: string; state: string; completed: number; total: number }
export interface KnowledgeStage { id: string; title: string; description: string; progress: number; nodes: KnowledgeNode[] }

export const difficultyText: Record<Difficulty, string> = { EASY: '简单', MEDIUM: '中等', HARD: '困难' }
export const statusText: Record<string, string> = {
  ACCEPTED: '通过', WRONG_ANSWER: '答案错误', COMPILE_ERROR: '编译错误', RUNTIME_ERROR: '运行错误',
  TIME_LIMIT_EXCEEDED: '超时', RUNNING: '判题中', QUEUED: '排队中', INTERNAL_ERROR: '系统错误',
  JUDGE_UNAVAILABLE: '判题服务未配置',
}

