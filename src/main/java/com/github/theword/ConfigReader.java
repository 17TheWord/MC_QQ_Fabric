package com.github.theword;

import org.apache.commons.io.FileUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static com.github.theword.MCQQ.LOGGER;

public class ConfigReader {
    public static Map<String, Object> config() {
        // 检查配置文件是否存在，如果不存在则从资源文件中复制过去
        Path configMapFilePath = Paths.get("./mods", "mcqq", "config.yml");

        Map<String, Object> configMap;
        if (!Files.exists(configMapFilePath)) {
            try {
                InputStream inputStream = MCQQ.class.getClassLoader().getResourceAsStream("config.yml");
                assert inputStream != null;
                FileUtils.copyInputStreamToFile(inputStream, configMapFilePath.toFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 读取配置文件
        try {
            Yaml yaml = new Yaml();
            Reader reader = Files.newBufferedReader(configMapFilePath);
            configMap = yaml.load(reader);
            return configMap;
        } catch (IOException e) {
            LOGGER.info("读取配置文件失败，将采用默认值");
            configMap = new HashMap<>();
            configMap.put("enable_mc_qq", true);
            configMap.put("enable_reconnect_msg", true);
            configMap.put("websocket_url", "ws://127.0.0.1:8080/mcqq");
            configMap.put("say_way", " 说：");
            configMap.put("command_message", false);
            configMap.put("death_message", true);
            configMap.put("join_quit", true);
            configMap.put("server_name", "Server");
            configMap.put("log_local", ".\\logs\\");
            configMap.put("log_name", "latest.log");
            return configMap;
        }
    }
}
