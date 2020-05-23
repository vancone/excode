package com.mekcone.excrudserver.controller;

import com.mekcone.excrudserver.entity.ProjectRecord;
import com.mekcone.excrudserver.repository.ProjectRepository;
import com.mekcone.webplatform.common.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

@RestController
@RequestMapping("/api/excrud/project")
public class ProjectController {
    @Autowired
    private ProjectRepository projectRepository;

    @GetMapping("/{projectId}")
    public Response retrieve(@PathVariable String projectId) {
        return Response.success(projectRepository.findAll());
    }

    @GetMapping
    public Response retrieveList() {
        return Response.success(projectRepository.findAll());
    }

    @PutMapping
    public Response update(@RequestBody ProjectRecord projectRecord) {
        projectRepository.saveProject(projectRecord);
        return Response.success();
    }

    @GetMapping("/download/{projectId}")
    public Response downloadFile(HttpServletResponse response, @PathVariable String projectId) {
        String downloadFilePath = "D:/Documents/Masterin Redis.pdf";//被下载的文件在服务器中的路径,
        String fileName = "Mastering%20Redis.pdf";//被下载文件的名称

        File file = new File(downloadFilePath);
        if (file.exists()) {
            response.setContentType("application/force-download");// 设置强制下载不打开
            response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);
            byte[] buffer = new byte[1024];
            FileInputStream fileInputStream = null;
            BufferedInputStream bufferedInputStream = null;
            try {
                fileInputStream = new FileInputStream(file);
                bufferedInputStream = new BufferedInputStream(fileInputStream);
                OutputStream outputStream = response.getOutputStream();
                int i = bufferedInputStream.read(buffer);
                while (i != -1) {
                    outputStream.write(buffer, 0, i);
                    i = bufferedInputStream.read(buffer);
                }

                return Response.success();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bufferedInputStream != null) {
                    try {
                        bufferedInputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fileInputStream != null) {
                    try {
                        fileInputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return Response.fail();
    }

    @GetMapping("/test")
    public String test() {
        return "ok2";
    }

    @PostMapping("/login")
    public String fakeLogin() {
        return "{\"code\":0,\"data\":{\"token\":\"admin-token\"}}";
    }

    @GetMapping("/info")
    public String fakeUserInfo(@RequestParam String token) {
        if (token.equals("admin-token")) {
            return "{\"code\":0,\"data\":{\"roles\":[\"admin\"],\"introduction\":\"I am a super administrator\",\"avatar\":\"https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif\",\"name\":\"Super Admin\"}}";
        }
        return "{\"websocket\":true,\"origins\":[\"*:*\"],\"cookie_needed\":false,\"entropy\":191921556}";
    }
}
