package top.doraemonqwq.dora.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.doraemonqwq.dora.entity.pojo.Anime;
import top.doraemonqwq.dora.entity.pojo.ImagePojo;

import java.util.List;

/**
 * 用户主页的传输数据对象
 * @author Doraemon
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserHomeDTO {
    private String name;
    private String introduction;
    // 注册时间
    private String creationDate;
    // 最后登录时间
    private String lastLoginTime;
    private List<Anime> animes = null;
    private List<ImagePojo> imagePojos;
}
