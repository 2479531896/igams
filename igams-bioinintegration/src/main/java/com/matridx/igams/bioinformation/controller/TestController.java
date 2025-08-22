package com.matridx.igams.bioinformation.controller;

import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.bioinformation.dao.entities.MngsmxjgDto;
import com.matridx.igams.bioinformation.service.svcinterface.IMngsmxjgService;
import com.matridx.igams.common.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.*;
import java.util.List;

@Controller
@RequestMapping("/bio")
public class TestController extends BaseController {

    @Autowired
    IMngsmxjgService mngsmxjgService;

    @RequestMapping("/pagedataJs")
    public String testPyPlk() {
        MngsmxjgDto mngsmxjgDto=new MngsmxjgDto();
        //基础数据195770A431A3445C9055D396F29783BA是特检
        mngsmxjgDto.setSjqf("195770A431A3445C9055D396F29783BA");
        List<MngsmxjgDto>list=mngsmxjgService.getMxjgByTjAndNotREM(mngsmxjgDto);
        Process proc;
        String[] args1 = new String[]{"python", "C:\\Users\\DELL\\Desktop\\demo.py", "D:\\java\\", JSONObject.toJSONString(list)};
        FileWriter fw = null;
        try
        {
            File file = new File("C:\\Users\\DELL\\Desktop\\cs.txt");
            if (!file.exists())
            {
                file.createNewFile();
            }
            fw = new FileWriter("C:\\Users\\DELL\\Desktop\\cs.txt");
            fw.write(JSONObject.toJSONString(list));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if (fw != null) {
                    fw.close();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }

        try {
            proc = Runtime.getRuntime().exec(args1);// 执行py文件
            //用输入输出流来截取结果
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            in.close();
            proc.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
