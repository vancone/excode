package __groupId__.__artifact.id__.service;

import __groupId__.__artifact.id__.entity.__Table__;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author ExCode
 * @since __date__
 */
public interface __Table__Service {
  void create(__Table__ __table__);

  __Table__ retrieve(String __primaryKey__);

  List<__Table__> retrieveList();

  Page<__Table__> retrievePage(int pageNo, int pageSize);

  void update(__Table__ __table__);

  void delete(String __primaryKey__);
}
