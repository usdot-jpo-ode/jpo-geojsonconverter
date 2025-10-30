package us.dot.its.jpo.geojsonconverter.pojos.common;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

@Getter
public enum ProcessedRequestImportanceLevel {
    REQUESTIMPORTANCELEVELUNKNOWN(0, "requestImportanceLevelUnKnown"),
    REQUESTIMPORTANCELEVEL1(1, "requestImportanceLevel1"),
    REQUESTIMPORTANCELEVEL2(2, "requestImportanceLevel2"),
    REQUESTIMPORTANCELEVEL3(3, "requestImportanceLevel3"),
    REQUESTIMPORTANCELEVEL4(4, "requestImportanceLevel4"),
    REQUESTIMPORTANCELEVEL5(5, "requestImportanceLevel5"),
    REQUESTIMPORTANCELEVEL6(6, "requestImportanceLevel6"),
    REQUESTIMPORTANCELEVEL7(7, "requestImportanceLevel7"),
    REQUESTIMPORTANCELEVEL8(8, "requestImportanceLevel8"),
    REQUESTIMPORTANCELEVEL9(9, "requestImportanceLevel9"),
    REQUESTIMPORTANCELEVEL10(10, "requestImportanceLevel10"),
    REQUESTIMPORTANCELEVEL11(11, "requestImportanceLevel11"),
    REQUESTIMPORTANCELEVEL12(12, "requestImportanceLevel12"),
    REQUESTIMPORTANCELEVEL13(13, "requestImportanceLevel13"),
    REQUESTIMPORTANCELEVEL14(14, "requestImportanceLevel14"),
    REQUESTIMPORTANCERESERVED(15, "requestImportanceReserved");

    private final int index;
    private final String name;

    private ProcessedRequestImportanceLevel(int index, String name) {
        this.index = index;
        this.name = name;
    }

    public static ProcessedRequestImportanceLevel fromName(String name) {
        if (StringUtils.isBlank(name)) {
            return null;
        }
        for (ProcessedRequestImportanceLevel enumValue : ProcessedRequestImportanceLevel.values()) {
            if (Objects.equals(enumValue.getName(), name)) {
                return enumValue;
            }
        }
        throw new IllegalArgumentException(name);
    }
}
