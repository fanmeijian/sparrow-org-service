package cn.sparrowmini.org.service.idconverter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cn.sparrowmini.org.model.relation.OrganizationPositionLevel.OrganizationPositionLevelPK;

@Component
public class OrganizationLevelIdConverter implements Converter<String, OrganizationPositionLevelPK> {

    @Override
    public OrganizationPositionLevelPK convert(String source) {
        String[] parts = source.split("_");
        OrganizationPositionLevelPK pk = new OrganizationPositionLevelPK();
        pk.setOrganizationId(parts[0]);
        pk.setPositionLevelId(parts[1]);

        return pk;
    }
}
