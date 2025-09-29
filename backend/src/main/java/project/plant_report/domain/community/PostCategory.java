package project.plant_report.domain.community;

public enum PostCategory {
    PLANT_STORY("식물 관련 이야기"),
    QNA("Q&A");

    private final String displayName;

    PostCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
