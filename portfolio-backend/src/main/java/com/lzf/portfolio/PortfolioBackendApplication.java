package com.lzf.portfolio;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 作品集后端启动入口。
 *
 * <p>{@code @MapperScan} 用于让 MyBatis-Plus 自动发现 mapper 包下的数据库访问接口。</p>
 */
@MapperScan("com.lzf.portfolio.mapper")
@SpringBootApplication
public class PortfolioBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(PortfolioBackendApplication.class, args);
    }
}
