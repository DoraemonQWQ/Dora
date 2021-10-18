package top.doraemonqwq.dora.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.doraemonqwq.dora.entity.pojo.GamePojo;
import top.doraemonqwq.dora.entity.pojo.ImagePojo;

import java.util.List;

/**
 * mmd的传输数据对象
 * @author Doraemon
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("GameDTO -- 游戏的传输数据对象")
public class GameDTO {
    @ApiModelProperty("游戏的id")
    private Integer id;
    @ApiModelProperty("游戏的名字")
    private String name;
    @ApiModelProperty("游戏的简介")
    private String introduction;
    @ApiModelProperty("游戏的下载量")
    private Integer clickVolume;
    @ApiModelProperty("游戏拥有的图片")
    private List<ImagePojo> images;

    public GameDTO(GamePojo gamePojo) {
        this.id = gamePojo.getId();
        this.name = gamePojo.getName();
        this.introduction = gamePojo.getIntroduction();
    }

}
