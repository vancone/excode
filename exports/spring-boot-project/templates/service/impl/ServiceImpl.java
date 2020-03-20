package ## groupId ##.## artifactId ##.service.impl;

## importPageHelper ##
import ## groupId ##.## artifactId ##.entity.## Table ##;
import ## groupId ##.## artifactId ##.mapper.## Table ##Mapper;
import ## groupId ##.## artifactId ##.service.## Table ##Service;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ## Table ##ServiceImpl implements ## Table ##Service {
  @Autowired private ## Table ##Mapper ## table ##Mapper;

  @Override
  public void create(## Table ## ## table ##) {
    ## table ##Mapper.create(## table ##);
  }

  @Override
  public List<## Table ##> retrieve(String ## primaryKey ##) {
    List<## Table ##> ## table ##List = ## table ##Mapper.retrieve(## primaryKey ##);
    return ## table ##List;
  }

  @Override
  public List<## Table ##> retrieveList() {
    List<## Table ##> ## table ##List = ## table ##Mapper.retrieveAll();
    return ## table ##List;
  }

  ## retrieveListWithPageHelper ##

  @Override
  public void update(## Table ## ## table ##) {
    ## table ##Mapper.update(## table ##);
  }

  @Override
  public void delete(String ## primaryKey ##) {
    ## table ##Mapper.delete(## primaryKey ##);
  }
}
