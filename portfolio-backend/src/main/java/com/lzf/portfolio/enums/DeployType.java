package com.lzf.portfolio.enums;

/**
 * 作品部署类型。
 */
public enum DeployType {
    /**
     * 当前服务器部署的作品，通常通过 Nginx 的 /apps 路径代理访问。
     */
    LOCAL,

    /**
     * 外部地址部署的作品，例如 GitHub Pages、Vercel 或其他独立域名。
     */
    EXTERNAL
}
