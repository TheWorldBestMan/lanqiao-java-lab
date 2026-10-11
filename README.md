# LanQiao Java Lab

面向大一新生的蓝桥杯 Java 学习与训练平台 MVP。项目采用 Vue 3 + TypeScript + Element Plus + Tailwind CSS，后端使用 Spring Boot、MySQL 与 Redis，并通过 Judge0 隔离执行不可信 Java 代码。

## 当前实现

- 课程知识树与阶段进度
- 带难度、来源和授权状态的题库
- Monaco Editor 在线 Java 编码工作台
- 安全的 Judge0 服务端代理（默认关闭，不会在本机执行代码）
- 提交记录、错题本、模拟考试、学习分析页面
- 后台题目治理页面，突出版权与可发布状态
- MySQL/Flyway 数据模型、Redis 提交限流接口和 Docker Compose 开发依赖

## 快速开始

### 无 Docker 演示模式

只想先打开界面时，可用内存数据库启动后端；关闭服务后演示提交记录会清空：

```bash
cd backend
mvn spring-boot:run -Dspring-boot.run.profiles=demo
```

然后在另一个终端启动前端：

```bash
cd frontend
npm install
npm run dev
```

### 1. 基础服务

安装 Docker 后运行：

```bash
docker compose up -d mysql redis
```

### 2. 后端

```bash
cd backend
mvn spring-boot:run
```

服务地址为 `http://localhost:8080`。健康检查：`GET /actuator/health`。

### 3. 前端

```bash
cd frontend
npm install
npm run dev
```

打开 `http://localhost:5173`。

## Judge0 配置

默认 `JUDGE0_ENABLED=false`，提交会被安全地标记为“判题服务未配置”，不会退化为本机执行。连接自托管 Judge0 时设置：

```env
JUDGE0_ENABLED=true
JUDGE0_BASE_URL=http://your-private-judge0:2358
JUDGE0_JAVA_LANGUAGE_ID=91
JUDGE0_AUTH_TOKEN=optional-token
```

生产环境必须将 Judge0 部署在独立网络与独立主机上，禁用回调 URL，限制出站访问、进程数、CPU、内存和磁盘，并在网关层再做速率限制。

当前 MVP 使用每道原创题的公开样例验证 Judge0 链路；正式上线前应将加密隐藏测试点放入独立判题数据服务，并改为异步逐测试点评测，不能把“样例通过”视为完整 AC。

## 一键发布到 GitHub Pages

Windows 上安装 Git 与 GitHub CLI，并执行一次 `gh auth login`，之后在项目根目录运行：

```powershell
powershell -ExecutionPolicy Bypass -File .\scripts\publish-to-github.ps1
```

脚本会创建公开仓库、推送源码、启用 Pages，并由 GitHub Actions 自动发布。公开版兼容手机和桌面浏览器；没有配置 `VITE_API_BASE_URL` 时自动使用只读演示数据。若后续已部署独立后端，可在 GitHub 仓库的 Actions Variables 中设置 `VITE_API_BASE_URL`。

## 版权原则

仓库只内置原创演示题。蓝桥杯真题、LeetCode 和企业笔试题仅可保存必要的来源元数据；在获得授权前，不导入或展示完整题面、测试数据与官方题解。详见 [阶段计划](docs/IMPLEMENTATION_PLAN.md)。

