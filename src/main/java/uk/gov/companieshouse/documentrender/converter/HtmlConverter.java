package uk.gov.companieshouse.documentrender.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.documentrender.converter.parser.GoTemplateParser;
import uk.gov.companieshouse.logging.Logger;

@Component
public class HtmlConverter implements Converter<String, String> {

    private final Logger logger;

    public HtmlConverter(final Logger logger) {
        this.logger = logger;
    }

    @Override
    public String convert(final String source) {
        logger.trace("convert(source=%d bytes) method called." .formatted(source.length()));

        logger.debug("* Creating template parser for document...");
        GoTemplateParser parser = new GoTemplateParser(logger);
        return parser.convert(source);
    }
}
