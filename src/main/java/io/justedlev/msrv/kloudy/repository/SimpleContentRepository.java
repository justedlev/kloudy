package io.justedlev.msrv.kloudy.repository;

import io.justedlev.msrv.kloudy.configuration.properties.KloudyStoreConfigurationProperties;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.stereotype.Repository;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.DigestInputStream;
import java.security.MessageDigest;

@Slf4j
@Repository
public class SimpleContentRepository<I> implements ContentRepository<I> {
    @Setter(AccessLevel.PRIVATE)
    private Path bucket;

    @Autowired
    protected void setBucketFrom(KloudyStoreConfigurationProperties props) {
        setBucket(props.getBucket());
    }

    @SneakyThrows
    @Override
    public long set(I sid, InputStream in) {
        var location = bucket.resolve(String.valueOf(sid)).normalize();
        var sha256 = MessageDigest.getInstance("SHA-256");
        try (
                var din = new DigestInputStream(in, sha256);
                var out = Files.newOutputStream(location, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE)
        ) {
            var bytes = din.transferTo(out);
            var checksum = new String(Hex.encode(sha256.digest()));
            log.trace("File '{}' uploaded: {} bytes ", location, bytes);
            log.trace("File checksum: {}", checksum);

            return bytes;
        } catch (Throwable e) {
            Files.delete(location);
            log.warn("File '{}' deleted", location, e);
            throw e;
        }
    }

    @Override
    public Resource get(I sid) {
        return new FileSystemResource(bucket.resolve(String.valueOf(sid)).normalize());
    }

    @SneakyThrows
    @Override
    public boolean delete(I sid) {
        return Files.deleteIfExists(bucket.resolve(String.valueOf(sid)).normalize());
    }
}
