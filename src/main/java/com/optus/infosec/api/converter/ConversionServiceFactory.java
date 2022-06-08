package com.optus.infosec.api.converter;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.convert.support.GenericConversionService;

import java.util.Set;

/**
 * @author SM
 *
 * Conversion Service Factory
 */
public class ConversionServiceFactory  implements FactoryBean<ConversionService>, InitializingBean {

    private Set<?> converters;

    private ConversionService conversionService;

    /**
     * Configure the set of custom converter objects that should be added: implementing
     * {@link org.springframework.core.convert.converter.Converter},
     * {@link org.springframework.core.convert.converter.ConverterFactory}, or
     * {@link org.springframework.core.convert.converter.GenericConverter}.
     */
    public void setConverters(Set<?> converters) {
        this.converters = converters;
    }

    public void afterPropertiesSet() {
        this.conversionService = createConversionService();
        org.springframework.core.convert.support.ConversionServiceFactory.registerConverters(this.converters, this.conversionService);
    }

    /**
     * Create the ConversionService instance returned by this factory bean.
     * <p>
     * Creates a simple {@link GenericConversionService} instance by default. Subclasses may override to customize the
     * ConversionService instance that gets created.
     */
    protected ConversionService createConversionService() {
        return new ConversionService();
    }

    // implementing FactoryBean

    public ConversionService getObject() {
        return this.conversionService;
    }

    public Class<? extends org.springframework.core.convert.ConversionService> getObjectType() {
        return ConversionService.class;
    }

    public boolean isSingleton() {
        return true;
    }
}
