package top.doraemonqwq.dora.entity.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MmdPojo {

    /**
     * mmd的id
     */
    private Integer id;

    /**
     * mmd的名字
     */
    private String name;

    /**
     * mmd的简介
     */
    private String introduction;

    public MmdPojo(String name, String introduction) {
        this.name = name;
        this.introduction = introduction;
    }

}
