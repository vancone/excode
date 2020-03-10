package ## groupId ##.## artifactId ##.mapper;

import ## groupId ##.## artifactId ##.model.## Table ##;
import java.util.List;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Service;

@Service
public interface ## Table ##Mapper {
  @Insert("INSERT INTO ## table ##(## columnKeys ##) VALUES(## columnTags ##)")
  @Options(useGeneratedKeys = true, keyProperty = "## primaryKey ##", keyColumn = "## primary_key ##")
  void create(## Table ## ## table ##);

  @Select("SELECT * FROM ## table ## WHERE cart_id = #{cartId}")
  @Results({
    ## resultAnnotations ##
  })
  List<## Table ##> retrieve(String ## primaryKey ##);

  @Select("SELECT * FROM ## table ##")
  @Results({
    ## resultAnnotations ##
  })
  List<## Table ##> retrieveAll();

  @Update("UPDATE ## table ## SET ## columnKeyTagExpressions ## WHERE ## primary_key ## = #{## primaryKey ##}")
  void update(## Table ## ## table ##);

  @Delete("DELETE FROM ## table ## WHERE ## primary_key ## = #{## primaryKey ##}")
  void delete(String ## primaryKey ##);
}
