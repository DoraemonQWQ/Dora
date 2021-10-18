package top.doraemonqwq.dora.entity.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Doraemon
 * video表的实体类
 */
@Data
@NoArgsConstructor
public class VideoPojo {
    private Integer id;
    private String name;
    private String url;

    public VideoPojo(String name, String url) {
        this.name = name;
        this.url = url;
    }
}
