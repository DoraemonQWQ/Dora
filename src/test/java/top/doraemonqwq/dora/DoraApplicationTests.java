package top.doraemonqwq.dora;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.jwt.JWT;
import org.apache.ibatis.annotations.Mapper;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import top.doraemonqwq.dora.constant.RoleConstants;
import top.doraemonqwq.dora.constant.SecurityConstants;
import top.doraemonqwq.dora.dto.UserDTO;
import top.doraemonqwq.dora.entity.pojo.User;
import top.doraemonqwq.dora.entity.security.Role;
import top.doraemonqwq.dora.security.pojo.JwtUser;
import top.doraemonqwq.dora.security.pojo.ResponseResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@SpringBootTest
@MapperScan("top.doraemonqwq.dora.dao.mapper")
class DoraApplicationTests {

    @Test
    void contextLoads() {
//        ArrayList<String> roles =
//                null;
//                new ArrayList<>();
//        roles.add("r18");
//        roles.add("admin");


//        List<SimpleGrantedAuthority> authorities =
//        Objects.isNull(roles) ? Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")) :
//                roles.stream()
//                        .map(SimpleGrantedAuthority::new)
//                        .collect(Collectors.toList());
//
//        System.out.println(authorities);


//        List<String> list = new ArrayList<>();
//        list.add(RoleConstants.ROLE_USER_DEFAULT);
//        UserDTO userDTO = new UserDTO();
//        userDTO.setUsername("test");
//        userDTO.setRoles(list);
//        userDTO.setLastLoginTime("2021-09-10");
//        userDTO.setEmail("123123@qq.com");
//
//        JwtUser jwtUser = JwtUser.create().setUserDTO(userDTO).setToken("token");
//        JSON json = JSONUtil.parse(jwtUser);
//
//        System.out.println(json);
//
//        JwtUser jwtUser1 = json.toBean(JwtUser.class);
//
//        System.out.println("token = " + jwtUser1.getToken() + ", userDTO = " + jwtUser1.getUserDTO());

//        StringBuilder token = new StringBuilder("token");
//        token.insert(0, SecurityConstants.TOKEN_PREFIX);
//        System.out.println(token.toString());

//        String dateStr = DateUtil.date().toDateStr();
//        System.out.println(dateStr);

//        System.out.println(JwtUser.create().setHeader(SecurityConstants.TOKEN_HEADER).toString());

//        String aaa = ResponseResult.create().ok("aaa", "/api/test");
//        System.out.println(aaa);

//        List<User> users = new ArrayList<>();
//
//        users.add(new User("admin","123","123@qq.com","2021-09-09","2021-09-09"));
//        users.add(new User("admin1","123","123@qq.com","2021-09-09","2021-09-09"));
//
//        String json = JSONUtil.parse(users).toString();
////        System.out.println(json);
//        List<User> users1 = new ArrayList<>();
//        users1 = JSONUtil.parseArray(json).toList(User.class);
//        System.out.println(users1);

//        String substring = this.getClass().getClassLoader().getResource("").getPath().substring(1);
//        System.out.println(substring);
    }

}
