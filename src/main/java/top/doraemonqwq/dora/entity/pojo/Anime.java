package top.doraemonqwq.dora.entity.pojo;

import lombok.Data;

@Data
public class Anime {

    /**
     * 动漫id
     */
    private Integer id;

    /**
     * 动漫名称
     */
    private String name;

    /**
     * 动漫集数
     */
    private Integer quanity;

    /**
     * 动漫地区
     */
    private String region;

    /**
     * 动漫简介
     */
    private String introduction;

    /**
     * 动漫年份
     */
    private String years;

    /**
     * 动漫是否完结 数据库中存放int数据，0代表为未完结，1代表完结
     */
    private boolean end;

    public Anime(String name, Integer quanity, String region, String introduction, String years, boolean end) {
        this.name = name;
        this.quanity = quanity;
        this.region = region;
        this.introduction = introduction;
        this.years = years;
        this.end = end;
    }

    public Anime(Integer id, String name, Integer quanity, String region, String introduction, String years, int end) {
        this.id = id;
        this.name = name;
        this.quanity = quanity;
        this.region = region;
        this.introduction = introduction;
        this.years = years;
        this.end = end != 0;
    }

    public Anime() {
    }

    public Integer getEnd() {
        return end ? 1 : 0;
    }

    public void setEnd(Integer end) {
        this.end = end != 0;
    }

    @Override
    public String toString() {
        return "Anime{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", quanity=" + quanity +
                ", region='" + region + '\'' +
                ", introduction='" + introduction + '\'' +
                ", years='" + years + '\'' +
                ", end=" + end +
                '}';
    }
}
