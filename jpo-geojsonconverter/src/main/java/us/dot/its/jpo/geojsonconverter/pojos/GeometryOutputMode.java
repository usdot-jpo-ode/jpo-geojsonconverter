package us.dot.its.jpo.geojsonconverter.pojos;

public enum GeometryOutputMode {
    GEOJSON_ONLY("GEOJSON_ONLY"),
    WKT("WKT");

    GeometryOutputMode(String value) {}

    public static GeometryOutputMode findByName(String name) {
        for (GeometryOutputMode modeName : values()) {
            if (modeName.name().equalsIgnoreCase(name)) {
                return modeName;
            }
        }
        return null;
    }
}
