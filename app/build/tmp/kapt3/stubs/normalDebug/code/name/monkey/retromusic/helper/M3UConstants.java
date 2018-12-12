package code.name.monkey.retromusic.helper;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\bf\u0018\u0000 \u00022\u00020\u0001:\u0001\u0002\u00a8\u0006\u0003"}, d2 = {"Lcode/name/monkey/retromusic/helper/M3UConstants;", "", "Companion", "app_normalDebug"})
public abstract interface M3UConstants {
    public static final code.name.monkey.retromusic.helper.M3UConstants.Companion Companion = null;
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\t\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u0014\u0010\u0003\u001a\u00020\u0004X\u0086D\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R\u0014\u0010\u0007\u001a\u00020\u0004X\u0086D\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u0006R\u0014\u0010\t\u001a\u00020\u0004X\u0086D\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u0006R\u0014\u0010\u000b\u001a\u00020\u0004X\u0086D\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\u0006\u00a8\u0006\r"}, d2 = {"Lcode/name/monkey/retromusic/helper/M3UConstants$Companion;", "", "()V", "DURATION_SEPARATOR", "", "getDURATION_SEPARATOR", "()Ljava/lang/String;", "ENTRY", "getENTRY", "EXTENSION", "getEXTENSION", "HEADER", "getHEADER", "app_normalDebug"})
    public static final class Companion {
        @org.jetbrains.annotations.NotNull()
        private static final java.lang.String EXTENSION = "m3u";
        @org.jetbrains.annotations.NotNull()
        private static final java.lang.String HEADER = "#EXTM3U";
        @org.jetbrains.annotations.NotNull()
        private static final java.lang.String ENTRY = "#EXTINF:";
        @org.jetbrains.annotations.NotNull()
        private static final java.lang.String DURATION_SEPARATOR = ",";
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getEXTENSION() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getHEADER() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getENTRY() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getDURATION_SEPARATOR() {
            return null;
        }
        
        private Companion() {
            super();
        }
    }
}