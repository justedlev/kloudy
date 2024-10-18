package io.justedlev.msrv.kloudy.repository.specs;

import io.justedlev.msrv.kloudy.repository.entity.KloudyFile;
import io.justedlev.msrv.kloudy.repository.entity.KloudyFile_;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

@UtilityClass
public class KloydyFileSpecifications {
    public static final Specification<KloudyFile> EMPTY = Specification.where(null);

    @NonNull
    public static Specification<KloudyFile> freeSearch(@Nullable String text) {

        if (StringUtils.isBlank(text)) {
            return EMPTY;
        }

        return (root, cq, cb) -> {

            var pattern = cb.concat(cb.concat("%", cb.literal(text)), "%");

            return cb.or(
                    cb.like(root.get(KloudyFile_.filename), pattern),
                    cb.like(root.get(KloudyFile_.extension), pattern),
                    cb.like(root.get(KloudyFile_.type), pattern)
            );
        };
    }

}
