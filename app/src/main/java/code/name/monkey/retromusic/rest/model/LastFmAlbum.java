package code.name.monkey.retromusic.rest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class LastFmAlbum {
    @Expose
    private Album album;

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public static class Album {
        @Expose
        private Tags tags;
        @Expose
        private List<Image> image = new ArrayList<>();
        @Expose
        private Wiki wiki;

        public List<Image> getImage() {
            return image;
        }

        public void setImage(List<Image> image) {
            this.image = image;
        }

        public Wiki getWiki() {
            return wiki;
        }

        public void setWiki(Wiki wiki) {
            this.wiki = wiki;
        }

        public Tags getTags() {
            return tags;
        }

        public static class Image {
            @SerializedName("#text")
            @Expose
            private String Text;
            @Expose
            private String size;

            public String getText() {
                return Text;
            }

            public void setText(String Text) {
                this.Text = Text;
            }

            public String getSize() {
                return size;
            }

            public void setSize(String size) {
                this.size = size;
            }
        }

        public class Tags {
            @Expose
            private List<Tag> tag = null;

            public List<Tag> getTag() {
                return tag;
            }
        }

        public class Tag {
            @Expose
            private String name;

            @Expose
            private String url;

            public String getName() {
                return name;
            }

            public String getUrl() {
                return url;
            }
        }

        public class Wiki {
            @Expose
            private String content;

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }
        }
    }
}
