[CmdletBinding()]
param(
    [ValidatePattern('^[A-Za-z0-9._-]+$')]
    [string]$RepositoryName = 'lanqiao-java-lab',

    [string]$Description = '面向蓝桥杯备赛的 Java 学习与算法训练平台',

    [switch]$SkipBuild
)

$ErrorActionPreference = 'Stop'
$projectRoot = Split-Path -Parent $PSScriptRoot
$frontendRoot = Join-Path $projectRoot 'frontend'

function Invoke-Checked {
    param([string]$Name, [scriptblock]$Action)
    Write-Host "`n==> $Name" -ForegroundColor Cyan
    & $Action
    if ($LASTEXITCODE -ne 0) { throw "$Name 失败，退出码：$LASTEXITCODE" }
}

if (-not (Get-Command git -ErrorAction SilentlyContinue)) { throw '未找到 Git，请先安装 Git for Windows。' }
if (-not (Get-Command gh -ErrorAction SilentlyContinue)) { throw '未找到 GitHub CLI，请先安装并运行 gh auth login。' }

Invoke-Checked '检查 GitHub 登录状态' { gh auth status }
$owner = (gh api user --jq '.login').Trim()
if (-not $owner) { throw '无法读取 GitHub 用户名。' }
$fullName = "$owner/$RepositoryName"

if (-not $SkipBuild) {
    Invoke-Checked '安装前端依赖' { Push-Location $frontendRoot; try { npm ci } finally { Pop-Location } }
    $env:VITE_BASE = "/$RepositoryName/"
    try {
        Invoke-Checked '构建手机与桌面端静态站点' { Push-Location $frontendRoot; try { npm run build } finally { Pop-Location } }
    } finally {
        Remove-Item Env:VITE_BASE -ErrorAction SilentlyContinue
    }
}

Push-Location $projectRoot
try {
    if (-not (Test-Path (Join-Path $projectRoot '.git'))) {
        Invoke-Checked '初始化 Git 仓库' { git init -b main }
    }

    $gitName = git config user.name
    $gitEmail = git config user.email
    if (-not $gitName) { git config user.name $owner }
    if (-not $gitEmail) { git config user.email "$owner@users.noreply.github.com" }

    git add --all
    git diff --cached --quiet
    if ($LASTEXITCODE -ne 0) {
        Invoke-Checked '提交网站代码' { git commit -m 'Deploy LanQiao Java Lab' }
    } else {
        Write-Host '没有新的代码需要提交。' -ForegroundColor DarkGray
    }

    gh repo view $fullName --json name 2>$null | Out-Null
    if ($LASTEXITCODE -ne 0) {
        Invoke-Checked '创建公开 GitHub 仓库并推送' {
            gh repo create $fullName --public --description $Description --source $projectRoot --remote origin --push
        }
    } else {
        $origin = git remote get-url origin 2>$null
        if (-not $origin) { git remote add origin "https://github.com/$fullName.git" }
        Invoke-Checked '推送到现有 GitHub 仓库' { git push -u origin main }
    }

    gh api "repos/$fullName/pages" 2>$null | Out-Null
    if ($LASTEXITCODE -ne 0) {
        Invoke-Checked '启用 GitHub Pages' { gh api --method POST "repos/$fullName/pages" -f build_type=workflow | Out-Null }
    } else {
        Invoke-Checked '设置 Pages 使用自动部署工作流' { gh api --method PUT "repos/$fullName/pages" -f build_type=workflow | Out-Null }
    }

    Write-Host "`n发布任务已提交。首次部署通常需要 1–3 分钟。" -ForegroundColor Green
    Write-Host "网站地址：https://$($owner.ToLower()).github.io/$RepositoryName/"
    Write-Host "部署进度：https://github.com/$fullName/actions"
} finally {
    Pop-Location
}

