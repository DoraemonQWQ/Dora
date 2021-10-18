package top.doraemonqwq.dora.entity.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Doraemon
 * images表的实体类
 */
@Data
@NoArgsConstructor
public class ImagePojo {
    private Integer id;
    private String name;
    private String url;

    public ImagePojo(String name, String url) {
        this.name = name;
        this.url = url;
    }
}
