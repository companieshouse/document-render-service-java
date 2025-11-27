package uk.gov.companieshouse.documentrender.converter.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.logging.Logger;

@Component
public class GoTemplateParser implements Converter<String, String> {

    private final Logger logger;

    public GoTemplateParser(final Logger logger) {
        this.logger = logger;
    }

    @Override
    public String convert(final String source) {
        logger.trace("convert(source=%d bytes) method called." .formatted(source.length()));

        Map<String, String> placeholderMap = new HashMap<>();

        List<String> placeholders = extractPlaceholdersIncludingBraces(source);
        for(final String placeholder : placeholders) {
            Supplier<String> stripped = stripBraces(placeholder);
            logger.debug("*** Placeholder: %s -> Stripped: %s".formatted(placeholder, stripped));

            placeholderMap.put(placeholder, stripped.get());
        }

        return "";
    }

    private List<String> extractPlaceholdersIncludingBraces(final String input) {
        List<String> result = new ArrayList<>();
        String open = "{{";
        String close = "}}";

        int start = 0;
        while (true) {
            int openIndex = input.indexOf(open, start);
            if (openIndex == -1) {
                break;  // no more opening braces
            }

            int closeIndex = input.indexOf(close, openIndex + open.length());
            if (closeIndex == -1) {
                break;  // no matching closing braces
            }

            // include the braces in the result
            String placeholderWithBraces = input.substring(openIndex, closeIndex + close.length());
            result.add(placeholderWithBraces);

            // move start index so we search for the next one
            start = closeIndex + close.length();
        }

        return result;
    }

    /**
     * Strips leading "{{" and trailing "}}" from placeholder string.
     * If a string doesn’t start with "{{" or end with "}}", it’s included unchanged.
     */
    private Supplier<String> stripBraces(final String placeholder) {
        if (placeholder != null && placeholder.startsWith("{{") && placeholder.endsWith("}}") && placeholder.length() >= 4) {
            return placeholder.substring(2, placeholder.length() - 2)::trim;
        }

        assert placeholder != null;

        return placeholder::trim;
    }
}
