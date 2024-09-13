package tw.grass.rental_crawler.service;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import tw.grass.rental_crawler.entity.Proxy;
import tw.grass.rental_crawler.repositories.ProxyRepository;

@Service
public class UpdateProxyScheduler {

    @Autowired
    ProxyCrawlerService proxyCrawlerService;
    
    @Autowired
    ProxyRepository proxyRepository;

    @PostConstruct
    public void runOnceAtStartup() {
        updateProxySchedule();
    }

    @Scheduled(cron = "${proxy.update.schedule}")
    public void scheduledCrawl() throws Exception {
        updateProxySchedule();
    }

    private void updateProxySchedule() {
        List<Proxy> proxies = Collections.emptyList();
        try {
            proxies = proxyCrawlerService.getProxies();
        } catch (IOException e) {
            e.printStackTrace();
        }
        proxyRepository.saveAll(proxies);
    }

}