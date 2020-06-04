package __groupId__.__artifactId__.controller;

import __groupId__.__artifactId__.entity.__Table__;
import __groupId__.__artifactId__.service.__Table__Service;

import com.github.pagehelper.PageInfo;
import com.mekcone.webplatform.common.model.Response;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/__table__")
public class __Table__Controller {
  @Autowired private __Table__Service __table__Service;

  @PostMapping
  public Response create(@RequestBody __Table__ __table__) {
    __table__Service.create(__table__);
    return Response.success();
  }

  @GetMapping("/{__primaryKey__}")
  public Response retrieve(@PathVariable String __primaryKey__) {
    List<__Table__> __table__List = __table__Service.retrieve(__primaryKey__);
    return Response.success(__table__List);
  }

  @GetMapping
  public Response retrieveList(
      @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
      @RequestParam(value = "pageSize", defaultValue = "5") int pageSize) {
    PageInfo<__Table__> __table__List = __table__Service.retrieveList(pageNo, pageSize);
    return Response.success(__table__List);
  }

  @PutMapping
  public Response update(@RequestBody __Table__ __Table__) {
    __table__Service.update(__Table__);
    return Response.success();
  }

  @DeleteMapping("/{__primaryKey__}")
  public Response delete(@PathVariable String __primaryKey__) {
    __table__Service.delete(__primaryKey__);
    return Response.success();
  }
}
