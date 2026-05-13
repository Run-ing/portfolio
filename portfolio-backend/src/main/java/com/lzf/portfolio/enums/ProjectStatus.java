package com.lzf.portfolio.enums;

/**
 * 作品状态。
 */
public enum ProjectStatus {
    /**
     * 已上线，可在前台展示并访问。
     */
    ONLINE,

    /**
     * 开发中，可在前台展示，但表示功能仍在建设。
     */
    DEVELOPING,

    /**
     * 已下线，前台列表默认不展示。
     */
    OFFLINE
}
