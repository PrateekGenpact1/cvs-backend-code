package com.cvsnewsletter.utility;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@UtilityClass
public class CvsUtility {

    private static final List<String> IMAGE_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "gif", "bmp", "webp");

    public static boolean isImageFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return false;
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            return false;
        }

        String extension = getFileExtension(originalFilename);
        return extension != null && IMAGE_EXTENSIONS.contains(extension.toLowerCase());
    }

    private static String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex == -1 || lastDotIndex == filename.length() - 1) {
            return null;
        }
        return filename.substring(lastDotIndex + 1);
    }

    public static boolean isValidOhrId(String ohrId) {
        return StringUtils.isNotBlank(ohrId) && ohrId.matches("\\d{9}");
    }

    public static boolean isValidMobileNumber(String mobileNumber) {
        return StringUtils.isNotBlank(mobileNumber) && mobileNumber.matches("\\d{10}");
    }

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd-MM-yyyy")
                    .withResolverStyle(ResolverStyle.SMART);

    public static boolean isValidDate(String dateStr) {
        if (dateStr == null || dateStr.isBlank()) {
            return false;
        }
        try {
            LocalDate.parse(dateStr, FORMATTER);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static List<String> safeSplitToList(String input) {
        if (input == null || input.trim().isEmpty()) {
            return Collections.emptyList();
        }
        return Arrays.asList(input.split(","));
    }

    public static String getOrDefault(String value) {
        return StringUtils.isNotBlank(value) ? value : StringUtils.EMPTY;
    }

    public static Boolean getOrDefault(Boolean value) {
        return (value != null) ? value : false;
    }

    public static String formatDate(LocalDate date, String pattern) {
        if (date == null) {
            return StringUtils.EMPTY;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return date.format(formatter);
    }

    public static String extractDesignation(String designationBand) {
        if (designationBand == null || designationBand.isBlank()) {
            return "";
        }

        // Remove leading/trailing spaces and special characters
        String cleaned = designationBand.trim();

        // Regex: remove patterns like "4A", "5B", "6C" etc. (number + letter band)
        cleaned = cleaned.replaceAll("\\b\\d+[A-G]\\b", "").trim();

        // Remove leftover dashes or extra spaces
        cleaned = cleaned.replaceAll("-", " ").trim();

        // Collapse multiple spaces into one
        cleaned = cleaned.replaceAll("\\s{2,}", " ");

        return cleaned;
    }

}
