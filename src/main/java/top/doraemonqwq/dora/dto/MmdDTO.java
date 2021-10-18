package top.doraemonqwq.dora.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.doraemonqwq.dora.entity.pojo.ImagePojo;
import top.doraemonqwq.dora.entity.pojo.MmdPojo;
import top.doraemonqwq.dora.entity.pojo.VideoPojo;

import java.util.List;

/**
 * mmd的传输数据对象
 * @author Doraemon
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("MmdDTO -- mmd的传输数据对象")
public class MmdDTO {
    @ApiModelProperty("mmd的id")
    private Integer id;
    @ApiModelProperty("mmd的名字")
    private String name;
    @ApiModelProperty("mmd的简介")
    private String introduction;
    @ApiModelProperty("mmd的观看量")
    private Integer clickVolume;
    @ApiModelProperty("mmd拥有的图片")
    private List<ImagePojo> images;
    @ApiModelProperty("mmd的视频源")
    private List<VideoPojo> videos;

    public MmdDTO(MmdPojo mmdPojo) {
        this.id = mmdPojo.getId();
        this.name = mmdPojo.getName();
        this.introduction = mmdPojo.getIntroduction();
    }

}
