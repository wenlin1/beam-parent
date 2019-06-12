package com.hsshy.beam.config;
import com.hsshy.beam.sys.service.IClienteleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import javax.sql.DataSource;
import java.util.Properties;

/**
 * 定时任务配置
 *
 * @author Mark sunlightcs@gmail.com
 * @since 2.0.0 2017-04-20
 */
@Configuration
@EnableScheduling
public class ScheduleConfig {
    @Autowired
    private IClienteleService clienteleService;
    //cron="0 0/2 * * * ?"
    @Scheduled(cron = "0 0/2 * * * ?") // 每10分钟执行一次
    public void getToken() {
        clienteleService.removeClientele();
        System.out.println("定时任务开启");
    }
}
