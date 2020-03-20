package ## groupId ##.## artifactId ##.controller;

import ## groupId ##.## artifactId ##.entity.## Table ##;
import ## groupId ##.## artifactId ##.service.## Table ##Service;
import ## groupId ##.## artifactId ##.util.ResultVOUtil;
import ## groupId ##.## artifactId ##.VO.ResultVO;

## importPageHelper ##
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

## swagger2Tag ##
@RestController
@RequestMapping("/## table ##")
public class ## Table ##Controller {
  @Autowired private ## Table ##Service ## table ##Service;

  ## swagger2ApiOperation ##
  @PostMapping
  public ResultVO create(@RequestBody ## Table ## ## table ##) {
    ## table ##Service.create(## table ##);
    return ResultVOUtil.success();
  }

  ## swagger2ApiOperation ##
  @GetMapping("/{## primaryKey ##}")
  public ResultVO retrieve(@PathVariable String ## primaryKey ##) {
    List<## Table ##> ## table ##List = ## table ##Service.retrieve(## primaryKey ##);
    return ResultVOUtil.success(## table ##List);
  }

  ## swagger2ApiOperation ##
  ## retrieveListMethod ##

  ## swagger2ApiOperation ##
  @PutMapping
  public ResultVO update(@RequestBody ## Table ## ## table ##) {
    ## table ##Service.update(## table ##);
    return ResultVOUtil.success();
  }

  ## swagger2ApiOperation ##
  @DeleteMapping("/{## primaryKey ##}")
  public ResultVO delete(@PathVariable String ## primaryKey ##) {
    ## table ##Service.delete(## primaryKey ##);
    return ResultVOUtil.success();
  }
}
