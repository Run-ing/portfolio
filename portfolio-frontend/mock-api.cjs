const http = require('node:http');

const projects = [
  {
    id: 1,
    name: 'AI 聊天助手',
    description: '基于大模型的在线聊天工具，支持多轮对话和提示词模板。',
    coverUrl: '/covers/ai-chat.png',
    techStack: ['Vue3', 'Spring Boot', 'MySQL'],
    tags: ['AI', '工具', '全栈'],
    deployType: 'EXTERNAL',
    projectUrl: 'https://example.com/chat',
    sourceUrl: 'https://github.com/example/ai-chat',
    status: 'ONLINE',
    recommend: true
  },
  {
    id: 2,
    name: '数据看板',
    description: '面向日常运营指标的轻量级可视化看板。',
    coverUrl: '/covers/dashboard.png',
    techStack: ['Vue3', 'ECharts', 'Spring Boot'],
    tags: ['数据', '可视化', '工具'],
    deployType: 'LOCAL',
    projectUrl: '/apps/dashboard',
    status: 'ONLINE',
    recommend: true
  },
  {
    id: 3,
    name: '接口管理器',
    description: '用于沉淀接口文档、调试记录和环境变量的内部工具。',
    coverUrl: '/covers/api-manager.png',
    techStack: ['TypeScript', 'Spring Boot', 'MyBatis-Plus'],
    tags: ['后端', '效率', '工具'],
    deployType: 'EXTERNAL',
    projectUrl: 'https://example.com/api-manager',
    status: 'DEVELOPING',
    recommend: false
  }
];

const server = http.createServer((req, res) => {
  res.setHeader('Content-Type', 'application/json; charset=utf-8');
  res.setHeader('Access-Control-Allow-Origin', '*');

  if (req.url === '/api/portfolio/projects') {
    res.end(JSON.stringify({ code: 200, message: 'success', data: projects }));
    return;
  }

  const match = req.url && req.url.match(/^\/api\/portfolio\/projects\/(\d+)$/);
  if (match) {
    const project = projects.find((item) => item.id === Number(match[1]));
    if (project) {
      res.end(JSON.stringify({ code: 200, message: 'success', data: project }));
    } else {
      res.statusCode = 404;
      res.end(JSON.stringify({ code: 404, message: 'project not found' }));
    }
    return;
  }

  res.statusCode = 404;
  res.end(JSON.stringify({ code: 404, message: 'not found' }));
});

server.listen(9090, () => {
  console.log('mock api ready at http://localhost:9090');
});
