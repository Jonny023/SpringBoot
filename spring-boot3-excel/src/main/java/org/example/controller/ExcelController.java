package org.example.controller;

import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.example.domain.entity.SysUser;
import org.example.domain.request.UserPageReq;
import org.example.domain.vo.R;
import org.example.domain.vo.UserVO;
import org.example.excel.annotation.ExcelExport;
import org.example.excel.annotation.ExcelImport;
import org.example.excel.handler.export.ExportFileNameHandler;
import org.example.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/excel")
public class ExcelController {

    @Resource
    private UserService userService;

    /**
     * 通用excel导出
     *
     * @return 用户信息.xlsx
     */
    @GetMapping("/user/export")
    @ExcelExport(fileName = "用户信息", dataClass = UserVO.class, fileNameHandler = ExportFileNameHandler.class)
    public List<UserVO> excelExport() {
        return userService.list();
    }

    @PostMapping("/user/import")
    public R<List<UserVO>> importOutsourcingDetail(
            @ExcelImport(name = "file", maxRow = 10000, dataClass = UserVO.class)
            List<UserVO> list
    ) {
        return R.ok(list);
    }

    @PostMapping("/user/list")
    public R<PageInfo<SysUser>> page(@RequestBody UserPageReq req) {
        return R.ok(userService.page(req));
    }

    @GetMapping("/user/batchExport")
    public R<PageInfo<SysUser>> batchExport() {
        userService.exportUsers();
        return R.ok();
    }
}