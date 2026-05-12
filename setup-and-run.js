#!/usr/bin/env node
/**
 * 一键启动脚本 - 数据结构学习平台（代码执行模块）
 * 用法: node setup-and-run.js
 *
 * 核心流程：
 *   1. 检查前置工具（docker / node / npm / mvn / curl）
 *   2. 启动 Piston Docker 容器
 *   3. 从 GitHub Release 下载预打包的语言包并注入容器（避免在线安装失败）
 *   4. 安装前端依赖
 *   5. 后端编译
 *   6. 同时启动后端和前端
 */

const { execFileSync, spawn } = require('child_process')
const path = require('path')
const fs = require('fs')
const crypto = require('crypto')
const os = require('os')

const ROOT = path.resolve(__dirname)
const FRONTEND_DIR = path.join(ROOT, 'frontend')
const BACKEND_PORT = 8080
const FRONTEND_PORT = 5173

// ─── 语言包配置（托管在 GitHub Release）────────────────────
const PISTON_PACKAGES = {
  url: 'https://github.com/tzy7777/Learning-Platform/releases/download/piston-packages-v1/piston-packages.tar.gz',
  sha256: 'ce998b528311408ed091192052e90ce9add6ae731d0741c967e86b4d5b17a924',
  // 解压后容器内应该出现的目录，用于判断"是否已注入"
  expectedLanguages: ['c', 'c++', 'python']
}

// ─── 工具函数 ────────────────────────────────────────────
function run(cmd, args, opts = {}) {
  console.log(`\n▶ ${cmd} ${args.join(' ')}`)
  execFileSync(cmd, args, { stdio: 'inherit', ...opts })
}

function tryRun(cmd, args, opts = {}) {
  try { return execFileSync(cmd, args, { encoding: 'utf8', ...opts }).trim() }
  catch { return null }
}

function checkCommand(cmd) {
  return tryRun('which', [cmd]) !== null
}

function header(text) {
  console.log('\n' + '='.repeat(50))
  console.log('  ' + text)
  console.log('='.repeat(50))
}

function sha256File(filePath) {
  const hash = crypto.createHash('sha256')
  hash.update(fs.readFileSync(filePath))
  return hash.digest('hex')
}

function getPortOwner(port) {
  return tryRun('lsof', ['-nP', `-iTCP:${port}`, '-sTCP:LISTEN'])
}

function ensurePortFree(port, serviceName) {
  const owner = getPortOwner(port)
  if (!owner) return

  console.error(`\n❌ ${serviceName} 启动失败：端口 ${port} 已被占用`)
  console.error('\n占用进程：')
  console.error(owner)
  console.error(`\n请先停止上面的进程，或修改 ${serviceName} 使用的端口后再重新运行脚本。`)
  process.exit(1)
}

function waitForPort(port, childProcess, timeoutMs, serviceName) {
  return new Promise((resolve, reject) => {
    const startedAt = Date.now()
    const timer = setInterval(() => {
      if (getPortOwner(port)) {
        clearInterval(timer)
        resolve()
        return
      }

      if (childProcess.exitCode !== null) {
        clearInterval(timer)
        reject(new Error(`${serviceName} 进程已退出，未能监听端口 ${port}`))
        return
      }

      if (Date.now() - startedAt > timeoutMs) {
        clearInterval(timer)
        reject(new Error(`${serviceName} 启动超时，${Math.round(timeoutMs / 1000)} 秒内未监听端口 ${port}`))
      }
    }, 1000)
  })
}

// ─── Step 1: 检查前置工具 ────────────────────────────────
header('Step 1: 检查前置工具')

const required = ['docker', 'node', 'npm', 'mvn', 'curl', 'tar', 'lsof']
const missing = required.filter(cmd => !checkCommand(cmd))
if (missing.length > 0) {
  console.error('\n❌ 缺少以下工具，请先安装：')
  missing.forEach(cmd => {
    const hint = {
      docker: '  → https://docs.docker.com/get-docker/',
      node:   '  → https://nodejs.org/ 或 brew install node',
      npm:    '  → 随 Node.js 一起安装',
      mvn:    '  → brew install maven 或 https://maven.apache.org/download.cgi',
      curl:   '  → macOS 自带，如缺失 brew install curl',
      tar:    '  → macOS 自带，正常不会缺',
      lsof:   '  → macOS 自带，正常不会缺'
    }[cmd] || ''
    console.error(`  - ${cmd}${hint ? '\n' + hint : ''}`)
  })
  process.exit(1)
}
console.log('✅ 所有前置工具已就绪')

// ─── Step 2: 启动 Piston（代码执行引擎）────────────────────
header('Step 2: 启动 Piston 代码执行引擎')

const composeFile = path.join(ROOT, 'docker-compose.yml')
if (!fs.existsSync(composeFile)) {
  console.error('❌ 找不到 docker-compose.yml，请确认在项目根目录运行此脚本')
  process.exit(1)
}

// 确认 Docker 守护进程在运行
if (tryRun('docker', ['info']) === null) {
  console.error('❌ Docker 守护进程未运行，请先启动 Docker Desktop')
  process.exit(1)
}

console.log('正在启动 Docker 容器...')
run('docker', ['compose', '-f', composeFile, 'up', '-d'])

// 等待 Piston 就绪
console.log('\n等待 Piston 启动（最多 30 秒）...')
let pistonReady = false
for (let i = 0; i < 30; i++) {
  const result = tryRun('curl', ['-sf', 'http://localhost:2000/api/v2/runtimes'])
  if (result !== null) { pistonReady = true; break }
  execFileSync('sleep', ['1'])
  process.stdout.write('.')
}
console.log('')
if (!pistonReady) {
  console.error('❌ Piston 启动超时，请检查 Docker 是否运行正常')
  process.exit(1)
}
console.log('✅ Piston 已就绪 (http://localhost:2000)')

// ─── Step 3: 注入预打包的语言包 ───────────────────────────
header('Step 3: 检查并注入 Piston 语言包')

function getInstalledLanguages() {
  const raw = tryRun('curl', ['-sf', 'http://localhost:2000/api/v2/runtimes'])
  if (!raw) return []
  try { return JSON.parse(raw).map(r => r.language) }
  catch { return [] }
}

const installed = getInstalledLanguages()
const needInstall = PISTON_PACKAGES.expectedLanguages.filter(l => !installed.includes(l))

if (needInstall.length === 0) {
  console.log(`✅ 所有语言包已就绪：${PISTON_PACKAGES.expectedLanguages.join(', ')}`)
} else {
  console.log(`⚠️  缺少语言包：${needInstall.join(', ')}，开始从 GitHub Release 下载注入...`)

  const tmpDir = fs.mkdtempSync(path.join(os.tmpdir(), 'piston-pkg-'))
  const tarPath = path.join(tmpDir, 'piston-packages.tar.gz')

  // 3.1 下载
  console.log(`\n📥 从 ${PISTON_PACKAGES.url} 下载语言包 (~636MB)...`)
  run('curl', ['-L', '--fail', '--progress-bar', '-o', tarPath, PISTON_PACKAGES.url])

  // 3.2 校验 SHA256
  console.log('\n🔍 校验 SHA256...')
  const actualSha = sha256File(tarPath)
  if (actualSha !== PISTON_PACKAGES.sha256) {
    console.error(`❌ SHA256 校验失败`)
    console.error(`   期望: ${PISTON_PACKAGES.sha256}`)
    console.error(`   实际: ${actualSha}`)
    process.exit(1)
  }
  console.log('✅ SHA256 校验通过')

  // 3.3 解压到临时目录
  console.log('\n📦 解压语言包...')
  run('tar', ['-xzf', tarPath, '-C', tmpDir])
  const extractedDir = path.join(tmpDir, 'piston-packages')
  if (!fs.existsSync(extractedDir)) {
    console.error('❌ 解压后未找到 piston-packages 目录，tarball 结构异常')
    process.exit(1)
  }

  // 3.4 注入到容器（复制每个语言子目录）
  console.log('\n🚚 注入到 Piston 容器...')
  for (const lang of fs.readdirSync(extractedDir)) {
    const src = path.join(extractedDir, lang)
    run('docker', ['cp', src, `piston:/piston/packages/`])
    console.log(`   ✔ 已注入 ${lang}`)
  }

  // 3.5 修正容器内文件所有权（Piston 容器内 uid 为 piston）
  console.log('\n🔧 修正容器内文件权限...')
  tryRun('docker', ['exec', '-u', 'root', 'piston', 'chown', '-R', 'piston:piston', '/piston/packages'])

  // 3.6 重启 Piston 让它重新扫描语言包
  console.log('\n🔄 重启 Piston 容器以加载语言包...')
  run('docker', ['restart', 'piston'])

  // 3.7 等待就绪并验证
  console.log('等待 Piston 重启就绪（最多 30 秒）...')
  let ready = false
  for (let i = 0; i < 30; i++) {
    const result = tryRun('curl', ['-sf', 'http://localhost:2000/api/v2/runtimes'])
    if (result !== null) { ready = true; break }
    execFileSync('sleep', ['1'])
    process.stdout.write('.')
  }
  console.log('')
  if (!ready) {
    console.error('❌ Piston 重启超时')
    process.exit(1)
  }

  const finalInstalled = getInstalledLanguages()
  const stillMissing = PISTON_PACKAGES.expectedLanguages.filter(l => !finalInstalled.includes(l))
  if (stillMissing.length > 0) {
    console.error(`❌ 注入后仍缺少语言包：${stillMissing.join(', ')}`)
    console.error(`   当前已注册：${finalInstalled.join(', ')}`)
    process.exit(1)
  }
  console.log(`✅ 语言包注入完成：${finalInstalled.join(', ')}`)

  // 3.8 清理临时文件
  try { fs.rmSync(tmpDir, { recursive: true, force: true }) } catch {}
}

// ─── Step 4: 安装前端依赖 ─────────────────────────────────
header('Step 4: 安装前端依赖 (npm install)')

if (!fs.existsSync(path.join(FRONTEND_DIR, 'node_modules'))) {
  run('npm', ['install'], { cwd: FRONTEND_DIR })
} else {
  console.log('✅ node_modules 已存在，跳过（如需重装请删除 frontend/node_modules）')
}

// ─── Step 5: 后端编译检查 ─────────────────────────────────
header('Step 5: 后端编译 (mvn compile)')
run('mvn', ['compile', '-q'], { cwd: ROOT })
console.log('✅ 后端编译成功')

// ─── Step 6: 启动后端 & 前端 ──────────────────────────────
header('Step 6: 启动后端和前端（Ctrl+C 退出）')

async function startServices() {
  ensurePortFree(BACKEND_PORT, '后端')
  ensurePortFree(FRONTEND_PORT, '前端')

  console.log('\n🚀 启动 Spring Boot 后端...')
  const backend = spawn('mvn', ['spring-boot:run'], {
    cwd: ROOT,
    stdio: ['ignore', 'pipe', 'pipe']
  })

  backend.stdout.on('data', d => {
    const s = d.toString()
    if (s.includes('Started') || s.includes('ERROR') || s.includes('Exception')) process.stdout.write(s)
  })
  backend.stderr.on('data', d => process.stderr.write(d.toString()))

  try {
    console.log(`等待后端监听端口 ${BACKEND_PORT}（最多 60 秒）...`)
    await waitForPort(BACKEND_PORT, backend, 60000, '后端')
    console.log(`✅ 后端已监听：http://localhost:${BACKEND_PORT}`)
  } catch (err) {
    backend.kill()
    console.error(`\n❌ ${err.message}`)
    process.exit(1)
  }

  console.log('\n🚀 启动前端开发服务器...')
  const frontend = spawn('npm', ['run', 'dev', '--', '--strictPort'], {
    cwd: FRONTEND_DIR,
    stdio: 'inherit'
  })

  try {
    console.log(`等待前端监听端口 ${FRONTEND_PORT}（最多 30 秒）...`)
    await waitForPort(FRONTEND_PORT, frontend, 30000, '前端')
  } catch (err) {
    backend.kill()
    frontend.kill()
    console.error(`\n❌ ${err.message}`)
    process.exit(1)
  }

  // 优雅退出
  process.on('SIGINT', () => {
    console.log('\n\n正在停止服务...')
    backend.kill()
    frontend.kill()
    process.exit(0)
  })

  backend.on('close', code => {
    frontend.kill()
    process.exit(code)
  })

  frontend.on('close', code => {
    backend.kill()
    process.exit(code)
  })

  console.log('\n' + '='.repeat(50))
  console.log('  ✅ 全部服务启动完毕！')
  console.log(`  前端：http://localhost:${FRONTEND_PORT}`)
  console.log(`  后端：http://localhost:${BACKEND_PORT}`)
  console.log(`  测试页面：http://localhost:${FRONTEND_PORT}/exercise/1`)
  console.log('  Piston：http://localhost:2000')
  console.log('  按 Ctrl+C 停止所有服务')
  console.log('='.repeat(50) + '\n')
}

startServices()
