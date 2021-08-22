package __groupId__.__artifactId__.service.impl;

import __groupId__.__artifactId__.entity.__Table__;
import __groupId__.__artifactId__.repository.__Table__Repository;
import __groupId__.__artifactId__.service.__Table__Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author ExCode
 * @since __date__
 */
@Service
public class __Table__ServiceImpl implements __Table__Service {
  @Autowired
  private __Table__Repository __Table__Repository;

  @Override
  public void create(__Table__ __Table__) {
    __Table__Repository.save(__Table__);
  }

  @Override
  public __Table__ retrieve(String id) {
    Optional<__Table__> optional = __Table__Repository.findById(id);
    return optional.orElse(null);
  }

  @Override
  public List<__Table__> retrieveList() {
    return __Table__Repository.findAll();
  }

  @Override
  public Page<__Table__> retrievePage(int pageNo, int pageSize) {
    return __Table__Repository.findAll(PageRequest.of(pageNo, pageSize));
  }

  @Override
  public void update(__Table__ __Table__) {
    __Table__Repository.save(__Table__);
  }

  @Override
  public void delete(String id) {
    Optional<__Table__> optional = __Table__Repository.findById(id);
    optional.ifPresent(__Table__ -> __Table__Repository.delete(__Table__));
  }
}
