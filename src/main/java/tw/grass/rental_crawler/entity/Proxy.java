package tw.grass.rental_crawler.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Proxy {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String ip;
    private int port;
    private boolean active;

    public Proxy() {
    }

    public Proxy(String ip, int port) {
        this.ip = ip;
        this.port = port;
        this.active = true;
    }
}
