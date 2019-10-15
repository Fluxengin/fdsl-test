package jp.co.fluxengine.example.plugin.effector;

import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import jp.co.fluxengine.stateengine.annotation.DslName;
import jp.co.fluxengine.stateengine.annotation.Effector;
import jp.co.fluxengine.stateengine.annotation.Post;
import org.apache.commons.lang.ObjectUtils;

import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Effector("effector型変更#型変更の検証")
public class DslReplacementEffector {

    private static final Storage STORAGE = StorageOptions.getDefaultInstance().getService();

    private static final Pattern STORAGE_PREFIX_PATTERN = Pattern.compile("gs://(.*?)/(.*)");

    @DslName("storage_prefix")
    private String storagePrefix;

    @DslName("filename")
    private String filename;

    @DslName("contents")
    private Object contents;

    @Post
    public void post() {
        Matcher storagePrefixMatcher = STORAGE_PREFIX_PATTERN.matcher(storagePrefix);
        if (storagePrefixMatcher.matches()) {
            String bucketName = storagePrefixMatcher.group(1);
            String blobPrefix = storagePrefixMatcher.group(2);
            BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, blobPrefix + filename).setContentType("text/plain").build();
            String message = "class = " + contents.getClass() + "\nmessage = " + ObjectUtils.toString(contents);
            STORAGE.create(blobInfo, message.getBytes(StandardCharsets.UTF_8));
        } else {
            throw new RuntimeException("storage_prefix の書式が不正です: " + storagePrefix);
        }
    }
}
