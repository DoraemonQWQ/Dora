package top.doraemonqwq.dora.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.doraemonqwq.dora.entity.pojo.Anime;
import top.doraemonqwq.dora.entity.pojo.ImagePojo;
import top.doraemonqwq.dora.entity.pojo.VideoPojo;

import java.util.List;

/**
 * Anime的传输数据对象
 * @author Doraemon
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("AnimeDTO -- 动漫资源的传输对象")
public class AnimeDTO {
    @ApiModelProperty("动漫的id")
    private Integer id;
    @ApiModelProperty("动漫的名字")
    private String name;
    @ApiModelProperty("动漫的总集数")
    private Integer quanity;
    @ApiModelProperty("动漫的地区")
    private String region;
    @ApiModelProperty("动漫的简介")
    private String introduction;
    @ApiModelProperty("动漫的完结状态")
    private boolean end;
    @ApiModelProperty("动漫的年份")
    private String years;
    @ApiModelProperty("动漫的观看量")
    private Integer clickVolume;
    @ApiModelProperty("动漫拥有的图片")
    private List<ImagePojo> images;
    @ApiModelProperty("动漫的视频源")
    private List<VideoPojo> videos;

    public AnimeDTO(Anime anime) {
        this.id = anime.getId();
        this.name = anime.getName();
        this.quanity = anime.getQuanity();
        this.region = anime.getRegion();
        this.introduction = anime.getIntroduction();
        this.end = anime.getEnd() > 0;
        this.years = anime.getYears();
    }

}
