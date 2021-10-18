package top.doraemonqwq.dora;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@SpringBootTest
@MapperScan("top.doraemonqwq.dora.dao.mapper")
class DoraApplicationTests {

    @SneakyThrows
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

//        System.out.println(ResourceUrlConstants.VIDEO_PATH);
//        System.out.println(ResourceUrlConstants.IMAGE_PATH);
//        System.out.println(SecurityConstants.EXPIRATION_TIME);
//        System.out.println(SecurityConstants.EXPIRATION_REMEMBER_TIME);

//        ArrayList<String> strings = new ArrayList<>();
//        strings.add("1");
//        strings.add("2");
//        strings.add("3");
//        ArrayList<List<String>> allStrings = new ArrayList<>();
//        allStrings.add(strings);
//        allStrings.add(strings);
//        for (List<String> allString : allStrings) {
//            for (String s : allString) {
//                if (s.equals("2")){
//                    System.out.println("找到了");
//                    break;
//                }
//                System.out.println(s);
//            }
//        }

//        String pathStr = "C:\\Users\\Doraemon\\Desktop\\开发路径\\images\\IMG_8751.JPG";
//
//        Path path = Paths.get(pathStr);
//        try {
//            String s = Files.probeContentType(path);
//            System.out.println(s);
//            System.out.println(path.toString());
//            System.out.println(path.getFileName());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        Files.write(Path.of("C:\\Users\\Doraemon\\Desktop\\开发路径\\logs\\test.log"), "测试".getBytes(StandardCharsets.UTF_8));
    }

    @Test
    void fileTest() {
        try {

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH-mm-ss");

            String logPath = "C:/Users/Doraemon/Desktop/开发路径/logs/user_log_" + LocalDate.now().format(dateFormatter);
            File pathFile = new File(logPath);
            if (!pathFile.isDirectory()) {
                // 文件夹不存在，创建一个新的文件夹
                pathFile.mkdir();
            }
            // 在目标文件夹下写入log文件
            File logFile = new File(logPath + "/operate-log.log");
            if (!logFile.isFile()) {
                // 当前日志不存在，创建一个新的日志
                if (logFile.createNewFile()) {
                    log.info("创建日志文件成功");
                } else {
                    log.error("创建日志文件失败");
                }
            } else if(logFile.length() >= 1000 * 10) {
                // 如果存在，并且文件大小已经又100m了，那么重命名并创建一个新的文件
                if (logFile.renameTo(new File(logPath + "/operate-log-" + LocalTime.now().format(timeFormatter) + ".log"))) {
                    log.info("用户操作日志文件重命名成功");
                    // 重新创建一个文件
                    if (logFile.createNewFile()) {
                        log.info("再次创建日志文件成功");
                    } else {
                        log.error("再次创建日志文件失败");
                    }

                } else {
                    log.error("用户操作日志文件重命名失败");
                }
            }
            try (FileWriter fileWriter = new FileWriter(logFile, true);
                 PrintWriter printWriter = new PrintWriter(fileWriter)) {
                printWriter.println("测试");
                printWriter.flush();
                fileWriter.flush();
            }


        } catch (IOException e) {
            log.error("创建用户操作日志文件失败",e);
        }
    }

    @Test
    void test() {

//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        Integer i = null;
//        if (authentication == null) {
//            System.out.println("Security上下文不存在");
//            System.out.println(i);
//            return;
//        }
//        Object principal = authentication.getPrincipal();
//        System.out.println(principal == null);


//        HashMap<String, List<Object>> map = new HashMap<>();
//
//        ArrayList<String> strings = new ArrayList<>();
//        strings.add("测");
//
//        map.put("key", Collections.singletonList(strings));
//
//        System.out.println(JSONUtil.parse(map).toString());

        String path = "C:/Users/Doraemon/Desktop/开发路径/logs";

        if (!path.endsWith("/")) {
            path += "/";
        }

        System.out.println(path);


    }

}
