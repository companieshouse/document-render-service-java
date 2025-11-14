package uk.gov.companieshouse.documentrender.utils;

import java.util.Optional;

public class S3PathUtils {

    private S3PathUtils() {
        super();
    }

    /**
     * Checks if the given s3Path appears to end in a file (rather than a “folder” prefix).
     * If yes, returns an Optional containing the file’s extension (without the dot).
     * If not a file (or no extension), returns Optional.empty().
     */
    public static Optional<String> getFileExtension(final String s3Path) {
        if (s3Path == null) {
            return Optional.empty();
        }

        String path = s3Path.trim();

        // If it ends with slash, treat as folder/prefix => no file
        if (path.endsWith("/")) {
            return Optional.empty();
        }

        // Extract the last segment after last slash
        int lastSlash = path.lastIndexOf('/');
        String lastSegment = (lastSlash >= 0) ? path.substring(lastSlash + 1) : path;

        // If last segment contains a dot with some characters after it => treat as file with extension
        int dotIndex = lastSegment.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < lastSegment.length() - 1) {
            // extension is substring after dot
            String ext = lastSegment.substring(dotIndex + 1);
            return Optional.of(ext);
        }

        // Otherwise we treat as “file” maybe but no detectable extension => you could still decide to return empty
        return Optional.empty();
    }

    public static boolean hasFileExtension(final String s3Path) {
        return getFileExtension(s3Path).isPresent();
    }
}
