package com.zhanghanlun.upyun.Service;


import com.upyun.FormUploader;
import com.upyun.Params;
import com.upyun.Result;
import com.zhanghanlun.upyun.Entity.ResultV0;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.*;

@Service
public class UpyunUploadService {

    private static final String BUCKET_NAME = "***";

    private static final String OPERATOR_NAME = "***";

    private static final String OPERATOR_PWD = "***";

    private static final Logger logger = LoggerFactory.getLogger(UpyunUploadService.class);

    public ResultV0 upload(String filePath, String fileType) {
        String savePath = getPath() + getFileName(fileType);
        FormUploader formUploader = new FormUploader(BUCKET_NAME, OPERATOR_NAME, OPERATOR_PWD);
        Map<String, Object> paramsMap = new HashMap();
        File file = new File(filePath);
        paramsMap.put(Params.SAVE_KEY, savePath);
        Result result = new Result();
        try {
            result = formUploader.upload(paramsMap, file);
        } catch (NoSuchAlgorithmException e) {
            logger.error("没有算法", e, e.getMessage());
        } catch (SignatureException e) {
            logger.error("签名错误", e, e.getMessage());
        } catch (InvalidKeyException e) {
            logger.error("key无效", e, e.getMessage());
        }
        ResultV0 resultV0 = new ResultV0();
        resultV0.setCode(result.getCode());
        resultV0.setFlag(result.isSucceed());
        resultV0.setPath(savePath);
        resultV0.setMsg(result.getMsg());
        return resultV0;
    }

    private String getFileName(String fileType) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        String fileName = calendar.get(Calendar.YEAR) + "_"
                + String.valueOf(calendar.get(Calendar.MONTH) + 1)
                + calendar.get(Calendar.DAY_OF_MONTH) + 1
                + calendar.get(Calendar.HOUR_OF_DAY)
                + getRandom() + "." + fileType;
        return fileName;
    }

    private String getPath(){
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String path = "/blog/"+calendar.get(Calendar.YEAR)+"/";
        int month = calendar.get(Calendar.MONTH) + 1;
        if (month>=10){
            path = path + month + "/";
        }else{
            path = path + "0" + month + "/";
        }
        return path;
    }

    private String getRandom() {
        Random random = new Random();
        int num = random.nextInt(1000);
        return String.valueOf(num);
    }
    
}
