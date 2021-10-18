package top.doraemonqwq.dora.dao.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import top.doraemonqwq.dora.entity.pojo.GamePojo;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface GameMapper {
    /**
     * 查询所有的游戏资源
     * @return 返回全部mmd资源的list
     */
    List<GamePojo> selectGames();

    /**
     * 通过模糊查询游戏的名称，进行筛选的游戏资源
     * @param text 需要进行模糊查询的文本
     * @return 返回筛选后的mmd资源的list
     */
    List<GamePojo> selectGamesByFuzzyText(@Param("text") String text);

    /**
     * 通过游戏的id查询一个游戏资源
     * @param id 需要查询的id
     * @return 返回查询到的游戏资源
     */
    GamePojo selectGameById(@Param("id") Integer id);

    /**
     * 添加一个游戏资源
     * @param gamePojo 需要添加的游戏的实体类
     * @return 返回成功数
     */
    int addGame(GamePojo gamePojo);

    /**
     * 更新一个游戏资源
     * @param map 更新的内容
     * @return 返回成功数
     */
    int updateGame(Map<String,Object> map);

    /**
     * 删除一个游戏资源
     * @param id 需要删除的游戏的id
     * @return 返回成功数
     */
    int delGame(@Param("id") Integer id);

}
