package ## groupId ##.## artifactId ##.service;

## importPageHelper ##
import ## groupId ##.## artifactId ##.entity.## Table ##;
import java.util.List;

public interface ## Table ##Service {
  void create(## Table ## ## table ##);

  List<## Table ##> retrieve(String ## primaryKey ##);

  List<## Table ##> retrieveList();

  ## retrieveListWithPageHelper ##

  void update(## Table ## ## table ##);

  void delete(String ## primaryKey ##);
}
