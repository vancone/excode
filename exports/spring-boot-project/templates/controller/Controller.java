package ## groupId ##.## artifactId ##.controller;

import ## groupId ##.## artifactId ##.model.## Table ##;
import ## groupId ##.## artifactId ##.model.RestResponse;
import ## groupId ##.## artifactId ##.service.## Table ##Service;
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
  public RestResponse create(@RequestBody ## Table ## ## table ##) {
    if (## table ##Service.create(## table ##)) {
      return RestResponse.success();
    }
    return RestResponse.fail(1, "Create ## table ## failed");
  }

  ## swagger2ApiOperation ##
  @GetMapping("/{## primaryKey ##}")
  public RestResponse retrieve(@PathVariable String ## primaryKey ##) {
    List<## Table ##> ## table ##List = ## table ##Service.retrieve(## primaryKey ##);
    if (## table ##List == null) {
      return RestResponse.fail(1, "Retrieve data failed");
    }
    return RestResponse.success(## table ##List);
  }

  ## swagger2ApiOperation ##
  @GetMapping
  public RestResponse retrieveAll() {
    List<## Table ##> ## table ##List = ## table ##Service.retrieveAll();
    if (## table ##List == null) {
      return RestResponse.fail(1, "Retrieve data failed");
    }
    return RestResponse.success(## table ##List);
  }

  ## swagger2ApiOperation ##
  @PutMapping
  public RestResponse update(@RequestBody ## Table ## ## table ##) {
    if (## table ##Service.update(## table ##)) {
      return RestResponse.success();
    }
    return RestResponse.fail(1, "Update ## table ## failed");
  }

  ## swagger2ApiOperation ##
  @DeleteMapping("/{## primaryKey ##}")
  public RestResponse delete(@PathVariable String ## primaryKey ##) {
    if (## table ##Service.delete(## primaryKey ##)) {
      return RestResponse.success();
    }
    return RestResponse.fail(1, "Delete ## table ## failed");
  }
}
