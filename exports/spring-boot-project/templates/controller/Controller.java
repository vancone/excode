package __groupId__.__artifactId__.controller;

import __groupId__.__artifactId__.entity.__Table__;
import __groupId__.__artifactId__.service.__Table__Service;
import __groupId__.__artifactId__.util.ResultVOUtil;
import __groupId__.__artifactId__.VO.ResultVO;

import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = { "__Table__" })
@RestController
@RequestMapping("/__table__")
public class __Table__Controller {
  @Autowired private __Table__Service __table__Service;

  @ApiOperation(value = "__createApiOperation__")
  @PostMapping
  public ResultVO create(@RequestBody __Table__ __table__) {
    __table__Service.create(__table__);
    return ResultVOUtil.success();
  }

  @ApiOperation(value = "__retrieveApiOperation__")
  @GetMapping("/{__primaryKey__}")
  public ResultVO retrieve(@PathVariable String __primaryKey__) {
    List<__Table__> __table__List = __table__Service.retrieve(__primaryKey__);
    return ResultVOUtil.success(__table__List);
  }

  @ApiOperation(value = "__retrieveListApiOperation__")
  @GetMapping
  public ResultVO retrieveList() {
    List<__Table__> __table__List = __table__Service.retrieveList();
    return ResultVOUtil.success(__table__List);
  }

  @ApiOperation(value = "__retrieveListApiOperation__")
  @GetMapping
  public ResultVO retrieveList(
      @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
      @RequestParam(value = "pageSize", defaultValue = "5") int pageSize) {
    PageInfo<__Table__> __table__List = __table__Service.retrieveList(pageNo, pageSize);
    return ResultVOUtil.success(__table__List);
  }

  @ApiOperation(value = "__updateListApiOperation__")
  @PutMapping
  public ResultVO update(@RequestBody __Table__ __Table__) {
    __table__Service.update(__Table__);
    return ResultVOUtil.success();
  }

  @ApiOperation(value = "__deleteListApiOperation__")
  @DeleteMapping("/{__primaryKey__}")
  public ResultVO delete(@PathVariable String __primaryKey__) {
    __table__Service.delete(__primaryKey__);
    return ResultVOUtil.success();
  }
}
