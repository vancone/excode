package __groupId__.__artifactId__.service.impl;

import __groupId__.__artifactId__.entity.__Table__;
import __groupId__.__artifactId__.mapper.__Table__Mapper;
import __groupId__.__artifactId__.service.__Table__Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ExCode
 * @date __date__
 */
@Service
public class __Table__ServiceImpl implements __Table__Service {
  @Autowired
  private __Table__Mapper __table__Mapper;

  @Override
  public void create(__Table__ __table__) {
    __table__Mapper.create(__table__);
  }

  @Override
  public List<__Table__> retrieve(String __primaryKey__) {
    List<__Table__> __table__List = __table__Mapper.retrieve(__primaryKey__);
    return __table__List;
  }

  @Override
  public List<__Table__> retrieveList() {
    List<__Table__> __table__List = __table__Mapper.retrieveList();
    return __table__List;
  }

  @Override
  public PageInfo<__Table__> retrieveList(int pageNo, int pageSize) {
    PageHelper.startPage(pageNo, pageSize);
    List<__Table__> __table__List = retrieveList();
    PageInfo<__Table__> pageInfo = new PageInfo<>(__table__List);
    return pageInfo;
  }

  @Override
  public void update(__Table__ __table__) {
    __table__Mapper.update(__table__);
  }

  @Override
  public void delete(String __primaryKey__) {
    __table__Mapper.delete(__primaryKey__);
  }
}
