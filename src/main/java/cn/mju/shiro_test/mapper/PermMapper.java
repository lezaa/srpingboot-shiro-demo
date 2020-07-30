package cn.mju.shiro_test.mapper;

import cn.mju.shiro_test.domain.Perm;
import org.springframework.stereotype.Repository;

import java.util.Set;


@Repository
public interface PermMapper {

    Set<Perm> getPermsByPermId(Integer permId);
}
