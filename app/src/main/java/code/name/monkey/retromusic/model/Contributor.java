package code.name.monkey.retromusic.model;

public class Contributor {
    private String name;
    private String summary;
    private String link;
    private String profile_image;

    public Contributor() {
    }

    public Contributor(String name, String summary, String link, String profile_image) {
        this.name = name;
        this.summary = summary;
        this.link = link;
        this.profile_image = profile_image;
    }

    public String getName() {
        return name;
    }

    public String getSummary() {
        return summary;
    }

    public String getLink() {
        return link;
    }

    public String getProfileImage() {
        return profile_image;
    }
}
