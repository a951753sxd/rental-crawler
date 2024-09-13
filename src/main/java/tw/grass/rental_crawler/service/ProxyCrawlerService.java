package tw.grass.rental_crawler.service;

import java.io.IOException;
import java.util.List;

import tw.grass.rental_crawler.entity.Proxy;

public interface ProxyCrawlerService {
    List<Proxy> getProxies() throws IOException;
}
