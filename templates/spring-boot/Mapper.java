package __groupId__.__artifactId__.mapper;

import __groupId__.__artifactId__.entity.__Table__;
import java.util.List;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Service;

/**
 * @author ExCode
 * @since __date__
 */
@Service
public interface __Table__Mapper {
  @Insert("")
  @Options(useGeneratedKeys = true, keyProperty = "__primaryKey__", keyColumn = "__primary_key__")
  void create(__Table__ __table__);

  @Select("SELECT * FROM __table__ WHERE __primary_key__ = #{__primaryKey__}")
  @Results({})
  List<__Table__> retrieve(String __primaryKey__);

  @Select("SELECT * FROM __table__")
  @Results({
    
  })
  List<__Table__> retrieveList();

  @Update("UPDATE __table__ SET __columnKeyTagExpressions__ WHERE __primary_key__ = #{__primaryKey__}")
  void update(__Table__ __table__);

  @Delete("DELETE FROM __table__ WHERE __primary_key__ = #{__primaryKey__}")
  void delete(String __primaryKey__);
}
