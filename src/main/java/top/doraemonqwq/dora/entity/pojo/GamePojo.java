package top.doraemonqwq.dora.entity.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Game表的实体类
 * @author Doraemon
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GamePojo {

    /**
     * 游戏id
     */
    private Integer id;

    /**
     * 游戏名称
     */
    private String name;

    /**
     * 游戏简介
     */
    private String introduction;

    /**
     * 文件路径
     */
    private String url;

    public GamePojo(String name, String introduction, String url) {
        this.name = name;
        this.introduction = introduction;
        this.url = url;
    }

}
