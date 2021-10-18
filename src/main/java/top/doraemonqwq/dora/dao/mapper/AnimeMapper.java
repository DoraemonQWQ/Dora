package top.doraemonqwq.dora.dao.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import top.doraemonqwq.dora.entity.pojo.Anime;

import java.util.List;
import java.util.Map;

/**
 * @author Doraemon
 * Anime表的Mapper接口
 */
@Mapper
@Repository
public interface AnimeMapper {

    /**
     * 查询所有的动漫资源
     * @return 返回全部动漫资源的list
     */
    List<Anime> selectAnimes();

    /**
     * 通过年份查询符合的动漫资源
     * @param years 需要指定筛选的年份
     * @return 返回筛选后的动漫资源的list
     */
    List<Anime> selectAnimesByYears(@Param("years") String years);

    /**
     * 通过模糊查询动漫的名称，进行筛选的动漫资源
     * @param text 需要进行模糊查询的文本
     * @return 返回筛选后的动漫资源的list
     */
    List<Anime> selectAnimesByFuzzyText(@Param("text") String text);

    /**
     * 通过动漫的id查询一个动漫资源
     * @param id 需要查询的id
     * @return 返回查询到的动漫资源
     */
    Anime selectAnimeById(@Param("id") Integer id);

    /**
     * 添加一个动漫资源
     * @param anime 需要添加的动漫的实体类
     * @return 返回成功数
     */
    int addAnime(Anime anime);

    /**
     * 更新一个动漫资源
     * @param map 更新的内容
     * @return 返回成功数
     */
    int updateAnime(Map<String,Object> map);

    /**
     * 删除一个动漫资源
     * @param id 需要删除的动漫的id
     * @return 返回成功数
     */
    int delAnime(@Param("id") Integer id);

}
