package org.cnc.cncbot.config;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CurrentWorldSchemaIdentifierResolver implements CurrentTenantIdentifierResolver {

    @Override
    public String resolveCurrentTenantIdentifier() {
    	return  DBContext.getSchema();
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }
}