package __groupId__.__artifactId__.service;

import __groupId__.__artifactId__.entity.__Table__;

import com.github.pagehelper.PageInfo;
import java.util.List;

/**
 * @author ExCode
 * @date __date__
 */
public interface __Table__Service {
  void create(__Table__ __table__);

  List<__Table__> retrieve(String __primaryKey__);

  List<__Table__> retrieveList();

  PageInfo<__Table__> retrieveList(int pageNo, int pageSize);

  void update(__Table__ __table__);

  void delete(String __primaryKey__);
}
