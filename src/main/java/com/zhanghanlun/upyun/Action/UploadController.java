package com.zhanghanlun.upyun.Action;

import com.zhanghanlun.upyun.Entity.ResultV0;
import com.zhanghanlun.upyun.Service.UpyunUploadService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@RestController
public class UploadController {

    @Resource
    private UpyunUploadService upyunUploadService;

    private static final String pathPrefix = "/www/server/tempdata/";

    private static final String urlPrefix = "https://upyun.zhanghanlun.com";

    @RequestMapping("/upload")
    public Map<String, Object> upload(@RequestParam("file") MultipartFile file) {
        Map<String, Object> result = new HashMap<>();
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                Path path = Paths.get(pathPrefix + file.getOriginalFilename());
                Files.write(path, bytes);
                String fileType = getFileType(file.getOriginalFilename());
                ResultV0 res = upyunUploadService.upload(path.toString(), fileType);
                if (res.isFlag()){
                    result.put("msg",urlPrefix + res.getPath());
                    result.put("flag",res.isFlag());
                }else{
                    result.put("msg",res.getMsg());
                    result.put("flag",false);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            result.put("msg", "文件为空");
            result.put("flag", false);
        }
        return result;
    }

    private String getFileType(String originalFileName) {
        String[] strs = originalFileName.split("\\.");
        return strs[strs.length - 1];
    }
}
