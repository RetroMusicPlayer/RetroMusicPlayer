package code.name.monkey.retromusic.activities.bugreport.model;


import code.name.monkey.retromusic.activities.bugreport.model.github.ExtraInfo;

public class Report {
    private final String title;

    private final String description;

    private final DeviceInfo deviceInfo;

    private final ExtraInfo extraInfo;

    public Report(String title, String description, DeviceInfo deviceInfo, ExtraInfo extraInfo) {
        this.title = title;
        this.description = description;
        this.deviceInfo = deviceInfo;
        this.extraInfo = extraInfo;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description + "\n\n"
                + "-\n\n"
                + deviceInfo.toMarkdown() + "\n\n"
                + extraInfo.toMarkdown();
    }
}
