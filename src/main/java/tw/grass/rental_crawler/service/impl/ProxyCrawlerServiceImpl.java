package tw.grass.rental_crawler.service.impl;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import tw.grass.rental_crawler.entity.Proxy;
import tw.grass.rental_crawler.service.ProxyCrawlerService;

@Service
public class ProxyCrawlerServiceImpl implements ProxyCrawlerService {

    Logger log = LoggerFactory.getLogger(ProxyCrawlerServiceImpl.class);

    @Override
    public List<Proxy> getProxies() throws IOException {
        String url = "https://www.free-proxy-list.net/";

        List<Proxy> proxies = new ArrayList<>();
        Document doc = Jsoup.connect(url).get();
        // 找到代理表格的 tbody 部分
        Element tableBody = doc.select("table.table tbody").first();

        if (tableBody != null) {

            // 取得表格中的每一行
            Elements rows = tableBody.select("tr");
            // 遍歷每一行，並擷取數據
            for (Element row : rows) {
                Elements cells = row.select("td");

                // 如果列數符合表格的預期
                if (cells.size() >= 8) {
                    String ipAddress = cells.get(0).text();
                    String port = cells.get(1).text();
                    String country = cells.get(3).text();
                    String anonymity = cells.get(4).text();
                    String google = cells.get(5).text();
                    String https = cells.get(6).text();
                    String lastChecked = cells.get(7).text();
                    if ("yes".equals(google) || "yes".equals(https))
                        proxies.add(new Proxy(ipAddress, Integer.parseInt(port)));

                }
            }
        } else {
            System.out.println("表格找不到！");
        }

        log.info("web say google ok or https ok proxy count is :{}", proxies.size());
        List<Proxy> collect = proxies.parallelStream().filter(pro -> isProxyValid(pro.getIp(), pro.getPort()))
                .collect(Collectors.toList());

        log.info("ok proxy count is :{}", collect.size());
        return collect;
    }

    public boolean isProxyValid(String ip, int port) {
        try {
            java.net.Proxy proxy = new java.net.Proxy(java.net.Proxy.Type.HTTP, new InetSocketAddress(ip, port));
            URL url = new URL("https://rent.591.com.tw/list");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection(proxy);
            connection.setConnectTimeout(5000); // 5 seconds timeout
            connection.connect();

            boolean b = connection.getResponseCode() == 200;
            log.info("isProxyValid:{}", b ? "OK" : "Fail");
            log.info("ip:{}:{}", ip, String.valueOf(port));

            return b;
        } catch (IOException e) {

            log.info("isProxyValid:{}", "timeout");
            return false;
        }
    }

}
