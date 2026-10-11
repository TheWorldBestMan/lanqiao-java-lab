import type { KnowledgeStage, ProblemDetail, ProblemSummary, Submission } from './api'

const templates = {
  sum: `import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        long a = in.nextLong();
        long d = in.nextLong();
        long n = in.nextLong();
        // TODO: 输出答案
    }
}`,
  focus: `import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int best = 0, current = 0;
        // TODO: 完成统计
    }
}`,
  window: `import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        long target = in.nextLong();
        long[] tasks = new long[n];
        // TODO: 滑动窗口
    }
}`,
}

export const demoProblems: ProblemDetail[] = [
  { id: 1, slug: 'sequence-sum', title: '等差序列求和', summary: '读取首项、公差和项数，输出等差序列的和。', descriptionMd: '## 题目描述\n给定整数首项 a、公差 d 和项数 n，计算前 n 项之和。\n\n输入为一行三个整数，保证结果在 long 范围内。', difficulty: 'EASY', sourceType: 'ORIGINAL', sourceLabel: 'LanQiao Java Lab 原创', licenseStatus: 'ORIGINAL', contentAvailable: true, timeLimitMs: 1000, memoryLimitMb: 128, templateCode: templates.sum, sampleInput: '1 2 5', sampleOutput: '25', tags: ['基础语法', '数学', '模拟'] },
  { id: 2, slug: 'longest-focus-block', title: '最长专注区间', summary: '在 0/1 学习记录中寻找连续 1 的最长长度。', descriptionMd: '## 题目描述\n给定长度为 n 的 0/1 序列，1 表示该分钟保持专注。输出连续专注分钟数的最大值。', difficulty: 'EASY', sourceType: 'ORIGINAL', sourceLabel: 'LanQiao Java Lab 原创', licenseStatus: 'ORIGINAL', contentAvailable: true, timeLimitMs: 1000, memoryLimitMb: 128, templateCode: templates.focus, sampleInput: '8\n1 1 0 1 1 1 0 1', sampleOutput: '3', tags: ['数组', '遍历', '状态统计'] },
  { id: 3, slug: 'minimum-study-window', title: '最短达标学习窗口', summary: '用滑动窗口找到累计学习量达标的最短连续区间。', descriptionMd: '## 题目描述\n给定 n 天的学习题量和目标 target，找出总题量至少为 target 的最短连续天数；不存在时输出 0。', difficulty: 'MEDIUM', sourceType: 'ORIGINAL', sourceLabel: 'LanQiao Java Lab 原创', licenseStatus: 'ORIGINAL', contentAvailable: true, timeLimitMs: 1500, memoryLimitMb: 256, templateCode: templates.window, sampleInput: '6 11\n1 2 3 4 5 1', sampleOutput: '3', tags: ['数组', '双指针', '滑动窗口'] },
  { id: 4, slug: 'lanqiao-placeholder', title: '蓝桥杯历年真题（授权占位）', summary: '仅保留来源与难度元数据；获得授权后再导入题面。', descriptionMd: '该条目为版权治理演示，不包含原题题面、样例、测试数据或题解。', difficulty: 'MEDIUM', sourceType: 'LANQIAO', sourceLabel: '蓝桥杯历年真题', sourceRef: 'external-id-pending', licenseStatus: 'METADATA_ONLY', contentAvailable: false, timeLimitMs: 1000, memoryLimitMb: 256, templateCode: '', sampleInput: '', sampleOutput: '', tags: ['蓝桥杯', '真题', '待授权'] },
]

export const demoKnowledge: KnowledgeStage[] = [
  { id: 'foundation', title: 'Java 基础', description: '先把输入输出、分支与循环练到熟练', progress: 72, nodes: [
    { id: 'io', title: '快速输入输出', state: 'MASTERED', completed: 8, total: 8 },
    { id: 'flow', title: '分支与循环', state: 'LEARNING', completed: 6, total: 9 },
    { id: 'array', title: '数组与字符串', state: 'LEARNING', completed: 4, total: 10 },
  ] },
  { id: 'core-algorithm', title: '核心算法', description: '蓝桥杯常用模型，强调识别与模板化', progress: 38, nodes: [
    { id: 'sort', title: '排序与查找', state: 'LEARNING', completed: 3, total: 8 },
    { id: 'prefix', title: '前缀和与差分', state: 'READY', completed: 1, total: 8 },
    { id: 'two-pointers', title: '双指针与滑窗', state: 'READY', completed: 1, total: 7 },
  ] },
  { id: 'advanced', title: '进阶冲刺', description: '图论、动态规划与综合模拟', progress: 12, nodes: [
    { id: 'search', title: 'DFS / BFS', state: 'LOCKED', completed: 0, total: 10 },
    { id: 'dp', title: '动态规划', state: 'LOCKED', completed: 0, total: 12 },
    { id: 'graph', title: '最短路与并查集', state: 'LOCKED', completed: 0, total: 9 },
  ] },
]

export const demoOverview = { studyDays: 12, currentStreak: 4, acceptedToday: 0, availableProblems: 4, weeklyGoal: 5, overallProgress: 68, mastery: [62, 74, 58, 46, 35], masteryLabels: ['基础语法', '数组', '模拟', '滑动窗口', '搜索'] }
export const demoExams = [
  { id: 'weekly-basic', title: '基础能力周测', durationMinutes: 60, problemCount: 3, status: 'AVAILABLE', note: '原创模拟题，不对应任何真实赛事' },
  { id: 'lanqiao-format', title: '蓝桥杯赛制适应卷', durationMinutes: 120, problemCount: 6, status: 'COMING_SOON', note: '结构演示；不含未授权真题' },
]

export function staticGet(url: string): unknown {
  if (url === '/problems') return demoProblems.map(({ descriptionMd, templateCode, sampleInput, sampleOutput, timeLimitMs, memoryLimitMb, ...p }) => p as ProblemSummary)
  if (url.startsWith('/problems/')) return demoProblems.find(p => p.slug === url.split('/').pop())
  if (url === '/learning/knowledge-tree') return demoKnowledge
  if (url === '/learning/overview') return demoOverview
  if (url === '/learning/wrong-problems') return []
  if (url === '/exams') return demoExams
  if (url === '/admin/content-audit') return { total: 4, publishable: 3, blocked: 1, byLicense: { ORIGINAL: 3, METADATA_ONLY: 1 } }
  if (url === '/submissions') return []
  throw new Error(`静态演示未提供接口：${url}`)
}

export function staticPost<T>(url: string): T {
  if (url.includes('/sessions')) return { id: crypto.randomUUID(), durationMinutes: 60, problems: demoProblems.slice(0, 3).map(p => ({ title: p.title })) } as T
  if (url === '/submissions') return { id: crypto.randomUUID(), problemId: 0, problemTitle: '演示提交', status: 'JUDGE_UNAVAILABLE', score: 0, message: '当前是 GitHub Pages 静态演示版；连接独立后端后可启用 Judge0。', createdAt: new Date().toISOString() } as Submission as T
  throw new Error(`静态演示未提供接口：${url}`)
}
