package code.name.monkey.retromusic.rest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class LastFmArtist {
    @Expose
    private Artist artist;

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public static class Artist {
        @Expose
        private List<Image> image = new ArrayList<>();
        @Expose
        private Bio bio;

        public List<Image> getImage() {
            return image;
        }

        public void setImage(List<Image> image) {
            this.image = image;
        }

        public Bio getBio() {
            return bio;
        }

        public void setBio(Bio bio) {
            this.bio = bio;
        }

        public class Bio {
            @Expose
            private String content;

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }
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
    }
}
